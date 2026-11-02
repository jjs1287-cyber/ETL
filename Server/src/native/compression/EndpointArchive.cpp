#include "EndpointArchive.h"
#include <archive.h>
#include <archive_entry.h>
#include <windows.h>
#include <filesystem>
#include <fstream>
#include <memory>
#include <set>
#include <algorithm>
#include <stdexcept>
#include <clocale>
namespace fs = std::filesystem;
namespace etlforge { namespace compression {
namespace {
struct Utf8Locale {
 int previousMode;std::string previousLocale;
 Utf8Locale(){previousMode=_configthreadlocale(_ENABLE_PER_THREAD_LOCALE);const char* current=setlocale(LC_CTYPE,nullptr);previousLocale=current?current:"C";if(!setlocale(LC_CTYPE,".UTF8")){_configthreadlocale(previousMode);throw std::runtime_error("Cannot activate thread-local UTF-8 archive encoding");}}
 ~Utf8Locale(){setlocale(LC_CTYPE,previousLocale.c_str());_configthreadlocale(previousMode);}
};
fs::path path(const std::string& s) {
 if(s.empty() || s.find('\0')!=std::string::npos) throw std::runtime_error("Invalid archive path");
 int n=MultiByteToWideChar(CP_UTF8,MB_ERR_INVALID_CHARS,s.data(),(int)s.size(),nullptr,0);
 if(!n) throw std::runtime_error("Archive path is not valid UTF-8");
 std::wstring w(n,0);MultiByteToWideChar(CP_UTF8,MB_ERR_INVALID_CHARS,s.data(),(int)s.size(),w.data(),n);return fs::path(w);
}
std::string name(const std::string& input) {
 std::string s=input;std::replace(s.begin(),s.end(),'\\','/');
 if(s.empty() || s[0]=='/' || s.find(':')!=std::string::npos)throw std::runtime_error("Unsafe absolute archive entry");
 path(s);
 size_t start=0;while(start<s.size()) {
  auto end=s.find('/',start);auto part=s.substr(start,end-start);
  if(part.empty()||part=="."||part==".."||part.back()=='.'||part.back()==' ')throw std::runtime_error("Unsafe archive entry path");
  for(unsigned char c:part)if(c<32||c=='<'||c=='>'||c=='"'||c=='|'||c=='?'||c=='*')throw std::runtime_error("Unsafe Windows archive entry");
  std::string base=part.substr(0,part.find('.'));std::transform(base.begin(),base.end(),base.begin(),[](unsigned char c){return (char)toupper(c);});
  if(base=="CON"||base=="PRN"||base=="AUX"||base=="NUL"||(base.size()==4&&(base.substr(0,3)=="COM"||base.substr(0,3)=="LPT")&&base[3]>='0'&&base[3]<='9'))throw std::runtime_error("Reserved Windows archive entry");
  if(end==std::string::npos)break;start=end+1;
 }
 return s;
}
std::wstring key(const std::string& s){auto w=path(s).wstring();CharLowerBuffW(w.data(),(DWORD)w.size());return w;}
void check(int result,archive* a){if(result<ARCHIVE_OK)throw std::runtime_error(archive_error_string(a)?archive_error_string(a):"Archive processing failed");}
}
std::string extension(const std::string& format){if(format=="zip")return ".zip";if(format=="tar.gz")return ".tar.gz";if(format=="gz")return ".gz";if(format=="7z")return ".7z";throw std::runtime_error("Unsupported target compression format");}
std::vector<File> extract(const std::string& archivePath,const std::string& directory,const std::string& password,const std::string& sourceName,unsigned long long byteLimit,size_t entryLimit){
 const unsigned long long maxBytes=(std::min)(byteLimit,2ULL*1024*1024*1024);const size_t maxEntries=(std::min)(entryLimit,static_cast<size_t>(10000));
 Utf8Locale utf8;
 auto destination=path(directory);if(fs::exists(destination))throw std::runtime_error("Extraction directory already exists");
 fs::create_directories(destination);
 try {
  std::unique_ptr<archive,decltype(&archive_read_free)> a(archive_read_new(),archive_read_free);
  check(archive_read_support_filter_all(a.get()),a.get());
  check(archive_read_support_format_zip(a.get()),a.get());check(archive_read_support_format_tar(a.get()),a.get());
  check(archive_read_support_format_7zip(a.get()),a.get());check(archive_read_support_format_rar(a.get()),a.get());check(archive_read_support_format_rar5(a.get()),a.get());
  check(archive_read_support_format_raw(a.get()),a.get());
  if(!password.empty())check(archive_read_add_passphrase(a.get(),password.c_str()),a.get());
  check(archive_read_open_filename_w(a.get(),path(archivePath).c_str(),1024*1024),a.get());
  std::vector<File> result;std::set<std::wstring> names;unsigned long long bytes=0;size_t entries=0;archive_entry* entry;int rc;
  std::vector<char> buffer(1024*1024);
  while((rc=archive_read_next_header(a.get(),&entry))!=ARCHIVE_EOF){
   check(rc,a.get());if(++entries>maxEntries)throw std::runtime_error("Archive exceeds 10000 entry limit");
   const char* p=archive_entry_pathname_utf8(entry);if(!p)throw std::runtime_error("Archive entry name cannot be decoded as UTF-8");
   std::string relative=p;
   if(archive_format(a.get())==ARCHIVE_FORMAT_RAW){
    if(archive_filter_code(a.get(),0)==ARCHIVE_FILTER_NONE)throw std::runtime_error("Source is not a supported compressed file");
    relative=path(sourceName.empty()?archivePath:sourceName).filename().u8string();auto dot=relative.find_last_of('.');if(dot!=std::string::npos)relative.resize(dot);if(relative.empty())relative="content";
   }
   while(relative.compare(0,2,"./")==0)relative.erase(0,2);
   if((relative.empty()||relative==".")&&archive_entry_filetype(entry)==AE_IFDIR)continue;
   while(!relative.empty()&&relative.back()=='/')relative.pop_back();relative=name(relative);
   if(archive_entry_symlink(entry)||archive_entry_hardlink(entry))throw std::runtime_error("Archive links are not allowed");
   auto type=archive_entry_filetype(entry);auto output=destination/path(relative);
   if(type==AE_IFDIR){fs::create_directories(output);continue;}
   if(type!=AE_IFREG)throw std::runtime_error("Archive contains a non-regular file");
   if(!names.insert(key(relative)).second)throw std::runtime_error("Duplicate archive entry name");
   auto declared=archive_entry_size(entry);if(declared>0&&(unsigned long long)declared>maxBytes-bytes)throw std::runtime_error("Archive exceeds 2 GiB extraction limit");
   fs::create_directories(output.parent_path());std::ofstream stream(output,std::ios::binary|std::ios::trunc);if(!stream)throw std::runtime_error("Cannot create extracted file");
   for(;;){auto count=archive_read_data(a.get(),buffer.data(),buffer.size());if(count<0)check((int)count,a.get());if(count==0)break;
    bytes+=(unsigned long long)count;if(bytes>maxBytes)throw std::runtime_error("Archive exceeds 2 GiB extraction limit");stream.write(buffer.data(),count);if(!stream)throw std::runtime_error("Cannot write extracted file");}
   stream.close();if(!stream)throw std::runtime_error("Cannot close extracted file");result.push_back({output.u8string(),relative});
  }
  check(archive_read_close(a.get()),a.get());if(result.empty())throw std::runtime_error("Archive contains no regular files");return result;
 }catch(...){std::error_code ignored;fs::remove_all(destination,ignored);throw;}
}
void pack(const std::vector<File>& files,const std::string& archivePath,const std::string& format,const std::string& password){
 Utf8Locale utf8;
 extension(format);if(files.empty())throw std::runtime_error("No files to compress");if(format=="gz"&&files.size()!=1)throw std::runtime_error("GZIP supports one file; choose TAR.GZ or ZIP for combined output");
 if(!password.empty()&&format!="zip")throw std::runtime_error("Password encryption is supported for ZIP targets only");
 auto output=path(archivePath);if(fs::exists(output))throw std::runtime_error("Archive staging file already exists");
 fs::create_directories(output.parent_path());
 try{
  std::unique_ptr<archive,decltype(&archive_write_free)> a(archive_write_new(),archive_write_free);
  if(format=="zip"){check(archive_write_set_format_zip(a.get()),a.get());check(archive_write_set_options(a.get(),"zip:hdrcharset=UTF-8"),a.get());if(!password.empty()){check(archive_write_set_passphrase(a.get(),password.c_str()),a.get());check(archive_write_set_options(a.get(),"zip:encryption=aes256"),a.get());}}
  else if(format=="7z")check(archive_write_set_format_7zip(a.get()),a.get());
  else {check(format=="gz"?archive_write_set_format_raw(a.get()):archive_write_set_format_pax_restricted(a.get()),a.get());check(archive_write_add_filter_gzip(a.get()),a.get());}
  check(archive_write_open_filename_w(a.get(),output.c_str()),a.get());std::set<std::wstring> names;std::vector<char> buffer(1024*1024);
  for(const auto& file:files){auto relative=name(file.name);if(!names.insert(key(relative)).second)throw std::runtime_error("Duplicate target archive entry name");auto input=path(file.path);
   if(!fs::is_regular_file(input)||fs::is_symlink(input))throw std::runtime_error("Archive input is not a regular file");
   std::unique_ptr<archive_entry,decltype(&archive_entry_free)> entry(archive_entry_new(),archive_entry_free);
   archive_entry_set_pathname_utf8(entry.get(),relative.c_str());archive_entry_set_size(entry.get(),fs::file_size(input));archive_entry_set_filetype(entry.get(),AE_IFREG);archive_entry_set_perm(entry.get(),0600);
   check(archive_write_header(a.get(),entry.get()),a.get());std::ifstream stream(input,std::ios::binary);if(!stream)throw std::runtime_error("Cannot read archive input");
   while(stream){stream.read(buffer.data(),buffer.size());auto count=stream.gcount();if(count&&archive_write_data(a.get(),buffer.data(),count)!=count)throw std::runtime_error("Cannot write archive data");}
   if(!stream.eof())throw std::runtime_error("Archive input read failed");check(archive_write_finish_entry(a.get()),a.get());
  }check(archive_write_close(a.get()),a.get());
 }catch(...){std::error_code ignored;fs::remove(output,ignored);throw;}
}
}}
