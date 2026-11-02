from pathlib import Path
p=Path('C:/ETL/Server/src/native/ssh/PgpFiles.cpp');s=p.read_text(encoding='utf-8-sig');s=s.replace('path=fs::path(wide(root))/(std::wstring(L"pgp-")+s);','std::wstring token;for(int i=1;i<37;++i)if(s[i]!=L\'-\')token+=s[i];path=fs::path(wide(root))/(std::wstring(L"pgp-")+token);');p.write_text(s,encoding='utf-8')
p=Path('C:/ETL/Server/src/native/ssh/SshKeys.cpp');s=p.read_text();s=s.replace('for(auto& entry:fs::directory_iterator(p))if(entry.path().extension()==".pem")DeleteFileW(entry.path().c_str());','''for(auto& entry:fs::directory_iterator(p)){
      if(entry.path().extension()==".pem")DeleteFileW(entry.path().c_str());
      if(entry.is_directory()&&!entry.is_symlink()&&entry.path().filename().wstring().rfind(L"pgp-",0)==0){std::error_code error;auto age=fs::file_time_type::clock::now()-fs::last_write_time(entry.path(),error);if(!error&&age>std::chrono::hours(24))fs::remove_all(entry.path(),error);}
    }''');s=s.replace('#include <mutex>','#include <mutex>\n#include <chrono>');p.write_text(s)
