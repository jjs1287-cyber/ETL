from pathlib import Path
p=Path('C:/ETL/Server/src/native/ssh/PgpFiles.h');p.write_text('''#pragma once
#include <string>
namespace etlforge { namespace pgp {
struct KeyInfo {std::string fingerprint,publicKey;bool hasPrivate=false;};
KeyInfo inspect(const std::string& runtime,const std::string& protectedRoot,const std::string& keyFile);
void transform(const std::string& runtime,const std::string& protectedRoot,const std::string& keyFile,const std::string& fingerprint,const std::string& passphrase,const std::string& input,const std::string& output,bool decrypt,bool armor=false);
}}
''',encoding='utf-8')
p=p.with_suffix('.cpp');p.write_text(r'''#include "PgpFiles.h"
#include <windows.h>
#include <sddl.h>
#include <objbase.h>
#include <filesystem>
#include <fstream>
#include <sstream>
#include <vector>
#include <stdexcept>
namespace etlforge { namespace pgp { namespace {
namespace fs=std::filesystem;
std::wstring wide(const std::string& s){if(s.empty()||s.find('\0')!=std::string::npos)throw std::runtime_error("Invalid PGP path");int n=MultiByteToWideChar(CP_UTF8,MB_ERR_INVALID_CHARS,s.data(),(int)s.size(),nullptr,0);if(!n)throw std::runtime_error("Invalid UTF-8 PGP path");std::wstring r(n,0);MultiByteToWideChar(CP_UTF8,MB_ERR_INVALID_CHARS,s.data(),(int)s.size(),r.data(),n);return r;}
std::wstring quote(const std::wstring& value){std::wstring r=L"\"";size_t slashes=0;for(wchar_t c:value){if(c==L'\\'){++slashes;continue;}if(c==L'\"')r.append(slashes*2+1,L'\\');else r.append(slashes,L'\\');slashes=0;r+=c;}r.append(slashes*2,L'\\');return r+L"\"";}
std::string read(const fs::path& file,size_t maximum=1048576){if(fs::file_size(file)>maximum)throw std::runtime_error("PGP metadata exceeds limit");std::ifstream f(file,std::ios::binary);if(!f)throw std::runtime_error("Cannot read PGP metadata");return {std::istreambuf_iterator<char>(f),{}};}
struct Handle {HANDLE h=nullptr;~Handle(){if(h&&h!=INVALID_HANDLE_VALUE)CloseHandle(h);}operator HANDLE()const{return h;}};
struct Workspace {
 fs::path path,runtime;
 Workspace(const std::string& root,const std::string& executable):runtime(wide(executable)){GUID id;if(FAILED(CoCreateGuid(&id)))throw std::runtime_error("Cannot create PGP workspace ID");wchar_t s[40];StringFromGUID2(id,s,40);path=fs::path(wide(root))/(std::wstring(L"pgp-")+s);PSECURITY_DESCRIPTOR sd=nullptr;if(!ConvertStringSecurityDescriptorToSecurityDescriptorW(L"D:P(A;OICI;FA;;;SY)(A;OICI;FA;;;BA)",SDDL_REVISION_1,&sd,nullptr))throw std::runtime_error("Cannot protect PGP workspace");SECURITY_ATTRIBUTES sa{sizeof(sa),sd,FALSE};bool ok=CreateDirectoryW(path.c_str(),&sa);LocalFree(sd);if(!ok)throw std::runtime_error("Cannot create private PGP workspace");}
 ~Workspace();
 std::string run(std::vector<std::string> args,const std::string& pass="",bool conf=false){
  if(pass.size()>4096||pass.find_first_of("\r\n\0",0,3)!=std::string::npos)throw std::runtime_error("Invalid PGP passphrase");
  fs::path exe=conf?runtime.parent_path()/L"gpgconf.exe":runtime;
  std::wstring cmd=quote(exe.wstring());std::vector<std::string> flags=conf?std::vector<std::string>{"--homedir",path.u8string()}:std::vector<std::string>{"--no-options","--homedir",path.u8string(),"--batch","--no-tty","--yes","--no-auto-key-retrieve","--auto-key-locate","clear","--pinentry-mode","loopback","--passphrase-fd","0"};flags.insert(flags.end(),args.begin(),args.end());for(auto& a:flags)cmd+=L" "+quote(wide(a));
  SECURITY_ATTRIBUTES sa{sizeof(sa),nullptr,TRUE};Handle input,writer,log,job,process,thread;
  if(!CreatePipe(&input.h,&writer.h,&sa,0)||!SetHandleInformation(writer,HANDLE_FLAG_INHERIT,0))throw std::runtime_error("Cannot create PGP input pipe");
  auto report=path/L"process.log";log.h=CreateFileW(report.c_str(),GENERIC_WRITE,FILE_SHARE_READ,&sa,CREATE_ALWAYS,FILE_ATTRIBUTE_NORMAL,nullptr);if(log.h==INVALID_HANDLE_VALUE)throw std::runtime_error("Cannot create protected PGP status");
  job.h=CreateJobObjectW(nullptr,nullptr);JOBOBJECT_EXTENDED_LIMIT_INFORMATION limits{};limits.BasicLimitInformation.LimitFlags=JOB_OBJECT_LIMIT_KILL_ON_JOB_CLOSE;if(!job.h||!SetInformationJobObject(job,JobObjectExtendedLimitInformation,&limits,sizeof(limits)))throw std::runtime_error("Cannot isolate PGP process");
  STARTUPINFOW si{};si.cb=sizeof(si);si.dwFlags=STARTF_USESTDHANDLES;si.hStdInput=input;si.hStdOutput=log;si.hStdError=log;PROCESS_INFORMATION pi{};
  if(!CreateProcessW(exe.c_str(),cmd.data(),nullptr,nullptr,TRUE,CREATE_NO_WINDOW|CREATE_SUSPENDED,nullptr,path.c_str(),&si,&pi))throw std::runtime_error("Cannot start bundled x64 GnuPG");process.h=pi.hProcess;thread.h=pi.hThread;
  if(!AssignProcessToJobObject(job,process)){TerminateProcess(process,1);throw std::runtime_error("Cannot contain PGP process");}ResumeThread(thread);
  CloseHandle(input.h);input.h=nullptr;std::string line=pass+"\n";DWORD n=0;BOOL sent=WriteFile(writer,line.data(),(DWORD)line.size(),&n,nullptr);SecureZeroMemory(line.data(),line.size());CloseHandle(writer.h);writer.h=nullptr;
  DWORD wait=WaitForSingleObject(process,120000);if(wait!=WAIT_OBJECT_0){TerminateJobObject(job,1);WaitForSingleObject(process,5000);throw std::runtime_error("PGP process timed out");}
  DWORD code=1;GetExitCodeProcess(process,&code);CloseHandle(log.h);log.h=nullptr;auto text=read(report);if(code!=0)throw std::runtime_error("PGP operation failed; check key, passphrase, expiry and file integrity");return text;
 }
};
Workspace::~Workspace(){try{run({"--kill","all"},"",true);}catch(...){}std::error_code error;fs::remove_all(path,error);}
KeyInfo list(Workspace& w){auto text=w.run({"--with-colons","--fixed-list-mode","--fingerprint","--list-keys"});std::istringstream in(text);std::string line;KeyInfo key;int count=0;bool primary=false;while(std::getline(in,line)){if(line.rfind("pub:",0)==0){++count;primary=true;}else if(primary&&line.rfind("fpr:",0)==0){std::vector<std::string> fields;std::stringstream parts(line);std::string f;while(std::getline(parts,f,':'))fields.push_back(f);if(fields.size()>9)key.fingerprint=fields[9];primary=false;}}
 if(count!=1||key.fingerprint.empty())throw std::runtime_error("Import exactly one OpenPGP primary key per record");
 auto privateText=w.run({"--with-colons","--list-secret-keys"});key.hasPrivate=privateText.find("sec:")!=std::string::npos;
 auto file=w.path/L"public.asc";w.run({"--armor","--output",file.u8string(),"--export",key.fingerprint});key.publicKey=read(file,65536);return key;}
}
KeyInfo inspect(const std::string& runtime,const std::string& root,const std::string& keyFile){Workspace w(root,runtime);w.run({"--import",keyFile});return list(w);}
void transform(const std::string& runtime,const std::string& root,const std::string& keyFile,const std::string& fingerprint,const std::string& pass,const std::string& input,const std::string& output,bool decrypt,bool armor){
 if(fingerprint.size()<40||fingerprint.find_first_not_of("0123456789ABCDEFabcdef")!=std::string::npos)throw std::runtime_error("Invalid OpenPGP fingerprint");auto destination=fs::path(wide(output));if(fs::exists(destination))throw std::runtime_error("PGP output already exists");
 Workspace w(root,runtime);w.run({"--import",keyFile},pass);auto info=list(w);if(info.fingerprint!=fingerprint)throw std::runtime_error("PGP key fingerprint mismatch");if(decrypt&&!info.hasPrivate)throw std::runtime_error("Source decryption requires a PGP private key");auto temporary=w.path/L"result.bin";
 std::vector<std::string> args={"--status-fd","1","--output",temporary.u8string()};
 if(decrypt){args.insert(args.end(),{"--decrypt",input});}else{if(armor)args.push_back("--armor");args.insert(args.end(),{"--trust-model","always","--cipher-algo","AES256","--compress-algo","none","--recipient",fingerprint,"--encrypt",input});}
 auto status=w.run(args,pass);if(decrypt&&status.find("[GNUPG:] DECRYPTION_OKAY")==std::string::npos)throw std::runtime_error("PGP decryption did not verify successfully");
 if(!MoveFileExW(temporary.c_str(),destination.c_str(),MOVEFILE_WRITE_THROUGH))throw std::runtime_error("Cannot publish PGP output");
}
}}
''',encoding='utf-8')
print('Created native PGP runner')
