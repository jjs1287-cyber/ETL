#include "EndpointCompression.h"
#include "../../../ssh/SshKeys.h"
#include "../../../ssh/PgpFiles.h"
#include <konga/Utils/Settings.h>
#include <konga/Db/DbUtils/DbUtils.h>
#include "EndpointArchive.h"
#include "DataLocation.h"
#include "SourceDataLocation.h"
#include "TargetDataLocation.h"
#include "FileTypeTargetLocation.h"
#include "ProcessedFiles.h"
#include "RenameFile.h"
#include "OperationEngineException.h"
#include <windows.h>
#include <objbase.h>
#include <filesystem>
#include <sstream>
#include <set>
#include <algorithm>
namespace fs=std::filesystem;
namespace konga {
namespace {
std::string staging(const std::string& file){
 GUID id;if(FAILED(CoCreateGuid(&id)))throw std::runtime_error("Cannot allocate archive staging ID");
 char value[80];sprintf_s(value,"etlforge_archive_%08lx%04x%04x%02x%02x%02x%02x%02x%02x%02x%02x",id.Data1,id.Data2,id.Data3,id.Data4[0],id.Data4[1],id.Data4[2],id.Data4[3],id.Data4[4],id.Data4[5],id.Data4[6],id.Data4[7]);
 return (fs::u8path(file).parent_path()/value).u8string();
}
bool enabled(const DataLocation* data){return data&&data->compressionProperties().GetBool("compression_enabled",false);}
}
void decryptEndpointFiles(const SourceDataLocation& source,ProcessedFiles& files){
 const auto* data=dynamic_cast<const DataLocation*>(&source);if(!data||!data->compressionProperties().GetBool("pgp_enabled",false))return;
 try{auto root=konga::GetServerHomeWithSlash()+"private/ssh-keys";konga::SmartDbPtr<konga::DbConnection> db(konga::GetBackEndDbConnection("TranDb"));auto key=etlforge::ssh::lease(db->GetConnectionHandle(),root,konga::GetOptionAsString("Security","KeyVaultMasterKey",""),data->compressionProperties().GetString("pgp_key_id",""),"pgp");ProcessedFiles updated=files;
 for(auto& file:updated.getProcessedFilesByRef()){if(file.hasError()||file.isInHistory())continue;auto output=staging(file.getLocalSourceLocation())+".clear";etlforge::pgp::transform(etlforge::ssh::pgpRuntime(root),root,key->privateFile,key->fingerprint,key->passphrase,file.getLocalSourceLocation(),output,true);auto name=file.getRemoteSourceLeafName();auto dot=name.find_last_of('.');if(dot!=std::string::npos){auto ext=name.substr(dot);std::transform(ext.begin(),ext.end(),ext.begin(),::tolower);if(ext==".pgp"||ext==".gpg"||ext==".asc")name.resize(dot);}file.archiveEntryName=name;file.setLocalSourceLocation(output);file.setSourceSize(fs::file_size(fs::u8path(output)));}files=updated;
 }catch(const std::exception& e){throw OperationEngineException(std::string("Source PGP decryption failed: ")+e.what());}
}
void encryptEndpointFiles(const TargetDataLocation& target,ProcessedFiles& files){
 const auto* data=dynamic_cast<const DataLocation*>(&target);if(!data||!data->compressionProperties().GetBool("pgp_enabled",false))return;
 try{auto fileTarget=dynamic_cast<const FileTypeTargetLocation*>(&target);if(!fileTarget)throw std::runtime_error("PGP encryption requires a file target");auto root=konga::GetServerHomeWithSlash()+"private/ssh-keys";konga::SmartDbPtr<konga::DbConnection> db(konga::GetBackEndDbConnection("TranDb"));auto key=etlforge::ssh::lease(db->GetConnectionHandle(),root,konga::GetOptionAsString("Security","KeyVaultMasterKey",""),data->compressionProperties().GetString("pgp_key_id",""),"pgp");bool armor=data->compressionProperties().GetBool("pgp_armor",false);ProcessedFiles updated=files;RenameFile renamer(fileTarget->getFileName());
 for(auto& file:updated.getProcessedFilesByRef()){if(file.hasError())throw std::runtime_error("Cannot encrypt output from a failed file");if(file.isInHistory()||file.getLocalTargetLocation().empty())continue;auto output=staging(file.getLocalTargetLocation())+(armor?".asc":".pgp");etlforge::pgp::transform(etlforge::ssh::pgpRuntime(root),root,key->privateFile,key->fingerprint,key->passphrase,file.getLocalTargetLocation(),output,false,armor);auto name=file.archiveTargetName;if(name.empty()){auto leaf=file.getRemoteSourceLeafName();name=renamer.hasSubstitutions()?renamer.getRenamedFileName(leaf):renamer.getPattern();if(name.empty())name=leaf;}auto suffix=armor?".asc":".pgp";if(name.size()<4||name.substr(name.size()-4)!=suffix)name+=suffix;file.archiveTargetName=name;file.setLocalTargetLocation(output);file.setTargetSize(fs::file_size(fs::u8path(output)));}files=updated;
 }catch(const std::exception& e){throw OperationEngineException(std::string("Target PGP encryption failed: ")+e.what());}
}
void expandEndpointArchives(const SourceDataLocation& source,ProcessedFiles& files){
 decryptEndpointFiles(source,files);
 const auto* data=dynamic_cast<const DataLocation*>(&source);if(!enabled(data))return;
 try{
  ProcessedFiles expanded;unsigned long long total=0;size_t count=0;
  for(const auto& file:files.getProcessedFilesByRef()){
   if(file.hasError()||file.isInHistory()){expanded.add(file);continue;}
   for(const auto& entry:etlforge::compression::extract(file.getLocalSourceLocation(),staging(file.getLocalSourceLocation()),data->compressionPassword(),file.getRemoteSourceLeafName(),2ULL*1024*1024*1024-total,10000-count)){
    total+=fs::file_size(fs::u8path(entry.path));if(total>2ULL*1024*1024*1024||++count>10000)throw std::runtime_error("Operation archive extraction exceeds 2 GiB or 10000 files");
    ProcessedFile item=file;item.archiveEntryName=entry.name;item.setLocalSourceLocation(entry.path);item.setSourceSize(fs::file_size(fs::u8path(entry.path)));expanded.add(item);
   }
  }
  files=expanded;
 }catch(const std::exception& e){throw OperationEngineException(std::string("Source archive extraction failed: ")+e.what());}
}
void compressEndpointFiles(const TargetDataLocation& target,ProcessedFiles& files){
 const auto* data=dynamic_cast<const DataLocation*>(&target);if(!enabled(data))return;
 try{
  const auto* fileTarget=dynamic_cast<const FileTypeTargetLocation*>(&target);if(!fileTarget)throw std::runtime_error("Compression requires a file target");
  const auto& props=data->compressionProperties();auto format=props.GetString("compression_format","zip");auto mode=props.GetString("compression_mode","combined");
  auto suffix=etlforge::compression::extension(format);if(mode!="combined"&&mode!="individual")throw std::runtime_error("Unknown compression output mode");
  auto& records=files.getProcessedFilesByRef();std::vector<etlforge::compression::File> inputs;std::vector<size_t> indexes;RenameFile renamer(fileTarget->getFileName());
  for(size_t i=0;i<records.size();i++){
   auto& file=records[i];if(file.hasError())throw std::runtime_error("Cannot compress output from a failed file");if(file.isInHistory()||file.getLocalTargetLocation().empty())continue;
   std::string leaf=file.getRemoteSourceLeafName();if(leaf.empty())leaf=file.getLocalSourceLeafName();
   auto entry=renamer.hasSubstitutions()?renamer.getRenamedFileName(leaf):renamer.getPattern();if(entry.empty())entry=leaf;
   inputs.push_back({file.getLocalTargetLocation(),entry});indexes.push_back(i);
  }
  if(inputs.empty())return;ProcessedFiles updated=files;auto& result=updated.getProcessedFilesByRef();
  if(mode=="combined"){
   auto archiveName=props.GetString("compression_archive_name","output");if(archiveName.empty())archiveName="output";
   if(fs::u8path(archiveName).filename().u8string()!=archiveName)throw std::runtime_error("Archive filename must not contain a directory");
   if(archiveName.size()<suffix.size()||archiveName.substr(archiveName.size()-suffix.size())!=suffix)archiveName+=suffix;
   auto output=staging(inputs[0].path)+suffix;etlforge::compression::pack(inputs,output,format,data->compressionPassword());
   for(auto index:indexes)result[index].setLocalTargetLocation("");
   auto& first=result[indexes[0]];first.setLocalTargetLocation(output);first.archiveTargetName=archiveName;first.setTargetSize(fs::file_size(fs::u8path(output)));
  }else{
   std::set<std::wstring> names;
   for(size_t i=0;i<inputs.size();i++){
    auto output=staging(inputs[i].path)+suffix;auto archiveName=inputs[i].name+suffix;
    auto lower=fs::u8path(archiveName).wstring();CharLowerBuffW(lower.data(),static_cast<DWORD>(lower.size()));
    if(!names.insert(lower).second)throw std::runtime_error("Duplicate compressed output name; include [file] or [unique] in the target filename");
    etlforge::compression::pack({inputs[i]},output,format,data->compressionPassword());
    auto& item=result[indexes[i]];item.setLocalTargetLocation(output);item.archiveTargetName=archiveName;item.setTargetSize(fs::file_size(fs::u8path(output)));
   }
  }
  files=updated;
 }catch(const std::exception& e){throw OperationEngineException(std::string("Target archive creation failed: ")+e.what());}
}
}
