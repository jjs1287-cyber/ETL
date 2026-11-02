from pathlib import Path
r=Path('C:/ETL/Server'); d=r/'src/native/integration/konga/OperationObjects'
def edit(p,old,new):
 s=p.read_text();assert old in s,str(p)+' missing '+old[:50];p.write_text(s.replace(old,new))
edit(d/'DataLocation.h','long getId() const;', '''long getId() const;
        const KeyValues& compressionProperties() const { return m_extendedProperties; }
        std::string compressionPassword() const;''')
p=d/'DataLocation.cpp';p.write_text(p.read_text()+'''\nstd::string DataLocation::compressionPassword() const {
 const std::string secret=m_extendedProperties.GetString("compression_password", "");
 return secret.empty()?secret:replace_de(DecryptString(secret));
}
''')
edit(d/'ProcessedFiles.h','bool hasError() const {','''std::string archiveEntryName;
        std::string archiveTargetName;
        bool hasError() const {''')
p=d/'ProcessedFiles.cpp';s=p.read_text();idx=s.index('ProcessedFile::getRemoteSourceLeafName()');print(s[idx:idx+180])
edit(d/'ProcessedFiles.cpp','ProcessedFile::getRemoteSourceLeafName() const {','ProcessedFile::getRemoteSourceLeafName() const {\n if (!archiveEntryName.empty()) return GetLeafPath(archiveEntryName);')
edit(d/'FileTypeTargetLocation.cpp','if ( renamer.hasSubstitutions() ) {','if (!file.archiveTargetName.empty()) return file.archiveTargetName;\n\tif ( renamer.hasSubstitutions() ) {')
edit(d/'SourceDataLocation.h','long getIgnoreFirstLines() const;','void finishSourceFiles(const OperationEngineContext& context, ProcessedFiles& files) const;\n\t\tlong getIgnoreFirstLines() const;')
edit(d/'SourceDataLocation.cpp','#include "OperationEngineException.h"','#include "OperationEngineException.h"\n#include "EndpointCompression.h"')
edit(d/'SourceDataLocation.cpp','const long ignoreFirst = getIgnoreFirstLines();','expandEndpointArchives(*this, files);\n\tconst long ignoreFirst = getIgnoreFirstLines();')
p=d/'SourceDataLocation.cpp';p.write_text(p.read_text()+'''
void SourceDataLocation::finishSourceFiles(const OperationEngineContext& context, ProcessedFiles& files) const {
 ProcessedFiles original;std::map<std::string,size_t> archives;
 for(const auto& file:files.getProcessedFilesByRef()) {
  if(file.archiveEntryName.empty()){original.add(file);continue;}
  auto found=archives.find(file.getRemoteSourceLocation());
  if(found==archives.end()){archives[file.getRemoteSourceLocation()]=original.getProcessedFilesByRef().size();original.add(file);}
  else if(file.hasError())original.getProcessedFilesByRef()[found->second].addError(file.getError());
 }
 doPostTransformation(context,original);
 for(auto& file:files.getProcessedFilesByRef())for(const auto& result:original.getProcessedFilesByRef())
  if(file.getRemoteSourceLocation()==result.getRemoteSourceLocation()&&result.hasWarning())file.addWarning(result.getWarning());
}
''')
edit(r/'src/native/integration/konga/OperationEngine/OperationEngine.cpp','pSourceLocation->doPostTransformation(*m_pContext, files);','pSourceLocation->finishSourceFiles(*m_pContext, files);')
edit(d/'TargetDataLocation.cpp','#include "TargetDataLocation.h"','#include "TargetDataLocation.h"\n#include "EndpointCompression.h"')
# Compression must occur after any pre-target plugins.
edit(d/'TargetDataLocation.cpp','''if ( m_pPluginSelector == 0 )
		return;
	konga::ExecutePipelinePluginAtSourceOrTarget(false,''','''if ( m_pPluginSelector != 0 )
	konga::ExecutePipelinePluginAtSourceOrTarget(false,''')
edit(d/'TargetDataLocation.cpp','*(this->m_pPluginSelector), 0);','*(this->m_pPluginSelector), 0);\n compressEndpointFiles(*this,files);')
# Undo accidental removal of the post hook's early return (only pre hook needs no early return).
s=(d/'TargetDataLocation.cpp').read_text();(d/'TargetDataLocation.cpp').write_text(s)
cm=r/'cmake/RemainingCore.cmake';cm.write_text(cm.read_text()+'''
add_library(etlforge_endpoint_archive STATIC src/native/compression/EndpointArchive.cpp)
target_compile_features(etlforge_endpoint_archive PUBLIC cxx_std_17)
target_compile_definitions(etlforge_endpoint_archive PRIVATE NOMINMAX)
target_include_directories(etlforge_endpoint_archive PUBLIC "${CMAKE_CURRENT_SOURCE_DIR}/src/native/compression" PRIVATE C:/ETL/Tools/vcpkg/installed/x64-windows/include)
target_link_libraries(etlforge_endpoint_archive PUBLIC C:/ETL/Tools/vcpkg/installed/x64-windows/lib/archive.lib)
target_link_libraries(etlforge_operationobjects_probe PUBLIC etlforge_endpoint_archive)
target_sources(etlforge_operationobjects_probe PRIVATE src/native/integration/konga/OperationObjects/EndpointCompression.cpp)
''')
