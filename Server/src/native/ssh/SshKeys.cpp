#include "SshKeys.h"
#include "PgpFiles.h"
#include <boost/json.hpp>
#include <windows.h>
#include <sddl.h>
#include <aclapi.h>
#include <objbase.h>
#include <filesystem>
#include <fstream>
#include <mutex>
#include <chrono>
#include <array>
#include <openssl/evp.h>
#include <openssl/pem.h>
#include <openssl/rand.h>
#include <sql.h>
#include <sqlext.h>

namespace etlforge { namespace ssh {
namespace {
namespace fs=std::filesystem;
std::mutex mutex;
std::string value(const boost::json::object& object,const char* name){auto item=object.if_contains(name);return item&&item->is_string()?std::string(item->as_string()):std::string();}
std::wstring wide(const std::string& s) {
    if(s.find('\0')!=std::string::npos)throw std::runtime_error("Invalid SSH path");
    int n=MultiByteToWideChar(CP_UTF8,MB_ERR_INVALID_CHARS,s.data(),static_cast<int>(s.size()),nullptr,0);
    if(!n)throw std::runtime_error("Invalid UTF-8 SSH path");
    std::wstring w(n,0);MultiByteToWideChar(CP_UTF8,MB_ERR_INVALID_CHARS,s.data(),static_cast<int>(s.size()),&w[0],n);return w;
}
struct Security {
    PSECURITY_DESCRIPTOR descriptor=nullptr;
    SECURITY_ATTRIBUTES attributes{sizeof(SECURITY_ATTRIBUTES),nullptr,FALSE};
    Security(){if(!ConvertStringSecurityDescriptorToSecurityDescriptorW(L"D:P(A;OICI;FA;;;SY)(A;OICI;FA;;;BA)",SDDL_REVISION_1,&descriptor,nullptr))throw std::runtime_error("Cannot protect SSH key storage");attributes.lpSecurityDescriptor=descriptor;}
    ~Security(){LocalFree(descriptor);}
};
struct ProcessLock {
    HANDLE handle=nullptr;
    ProcessLock(){Security security;handle=CreateMutexW(&security.attributes,FALSE,L"Global\\ETLForge.SshKeyStore");if(!handle)throw std::runtime_error("Cannot lock SSH key storage");DWORD result=WaitForSingleObject(handle,5000);if(result!=WAIT_OBJECT_0&&result!=WAIT_ABANDONED){CloseHandle(handle);handle=nullptr;throw std::runtime_error("SSH key storage is busy; retry shortly");}}
    ~ProcessLock(){if(handle){ReleaseMutex(handle);CloseHandle(handle);}}
};
fs::path directory(const std::string& root) {
    fs::path p(wide(root));fs::create_directories(p.parent_path());Security s;
    if(!CreateDirectoryW(p.c_str(),&s.attributes)&&GetLastError()!=ERROR_ALREADY_EXISTS)throw std::runtime_error("Cannot create SSH key storage");
    if(fs::is_symlink(p))throw std::runtime_error("SSH key storage cannot be a link");
    if(!SetFileSecurityW(p.c_str(),DACL_SECURITY_INFORMATION|PROTECTED_DACL_SECURITY_INFORMATION,s.descriptor))throw std::runtime_error("Cannot restrict SSH key storage permissions");
    for(auto& entry:fs::directory_iterator(p)){
      if(entry.path().extension()==".pem")DeleteFileW(entry.path().c_str());
      if(entry.is_directory()&&!entry.is_symlink()&&entry.path().filename().wstring().rfind(L"pgp-",0)==0){std::error_code error;auto age=fs::file_time_type::clock::now()-fs::last_write_time(entry.path(),error);if(!error&&age>std::chrono::hours(24))fs::remove_all(entry.path(),error);}
    }
    return p;
}
std::string guid(){GUID g; if(FAILED(CoCreateGuid(&g)))throw std::runtime_error("Cannot create SSH key ID");wchar_t w[40];StringFromGUID2(g,w,40);std::string s;for(int i=1;i<37;++i)s+=static_cast<char>(towlower(w[i]));return s;}
void validId(const std::string& id){if(id.size()!=36||id.find_first_not_of("0123456789abcdef-")!=std::string::npos)throw std::runtime_error("Invalid SSH key ID");}
std::wstring dbWide(const std::string& s){if(s.empty())return {};int n=MultiByteToWideChar(CP_UTF8,MB_ERR_INVALID_CHARS,s.data(),static_cast<int>(s.size()),nullptr,0);if(!n)throw std::runtime_error("Invalid UTF-8 key-vault value");std::wstring w(n,0);MultiByteToWideChar(CP_UTF8,MB_ERR_INVALID_CHARS,s.data(),static_cast<int>(s.size()),w.data(),n);return w;}
std::string dbUtf8(const std::wstring& w){if(w.empty())return {};int n=WideCharToMultiByte(CP_UTF8,WC_ERR_INVALID_CHARS,w.data(),static_cast<int>(w.size()),nullptr,0,nullptr,nullptr);if(!n)throw std::runtime_error("Invalid key-vault database text");std::string s(n,0);WideCharToMultiByte(CP_UTF8,WC_ERR_INVALID_CHARS,w.data(),static_cast<int>(w.size()),s.data(),n,nullptr,nullptr);return s;}
using Row=std::vector<std::string>;
std::vector<Row> query(SQLHDBC db,const std::string& sql,const std::vector<std::string>& args={}){
 SQLHSTMT st=SQL_NULL_HSTMT;if(!SQL_SUCCEEDED(SQLAllocHandle(SQL_HANDLE_STMT,db,&st)))throw std::runtime_error("Cannot allocate key-vault query");struct R{SQLHSTMT s;~R(){SQLFreeHandle(SQL_HANDLE_STMT,s);}} release{st};
 auto ws=dbWide(sql);if(!SQL_SUCCEEDED(SQLPrepareW(st,reinterpret_cast<SQLWCHAR*>(ws.data()),SQL_NTS)))throw std::runtime_error("Cannot prepare key-vault query");std::vector<std::wstring> values;std::vector<SQLLEN> lengths(args.size());values.reserve(args.size());for(auto& a:args)values.push_back(dbWide(a));for(size_t i=0;i<args.size();++i){lengths[i]=values[i].size()*sizeof(wchar_t);if(!SQL_SUCCEEDED(SQLBindParameter(st,static_cast<SQLUSMALLINT>(i+1),SQL_PARAM_INPUT,SQL_C_WCHAR,SQL_WLONGVARCHAR,std::max<size_t>(1,values[i].size()),0,values[i].data(),(values[i].size()+1)*sizeof(wchar_t),&lengths[i])))throw std::runtime_error("Cannot bind key-vault value");}
 if(!SQL_SUCCEEDED(SQLExecute(st)))throw std::runtime_error("Key-vault database request failed");SQLSMALLINT cols=0;SQLNumResultCols(st,&cols);std::vector<Row> out;while(SQLFetch(st)!=SQL_NO_DATA){Row row;for(SQLUSMALLINT c=1;c<=cols;++c){std::wstring all;for(;;){wchar_t b[4096]={};SQLLEN length=0;auto rc=SQLGetData(st,c,SQL_C_WCHAR,b,sizeof(b),&length);if(rc==SQL_NO_DATA||length==SQL_NULL_DATA)break;if(!SQL_SUCCEEDED(rc))throw std::runtime_error("Cannot read key-vault value");all+=b;if(rc==SQL_SUCCESS)break;}row.push_back(dbUtf8(all));}out.push_back(std::move(row));}return out;
}
std::string b64(const unsigned char* p,size_t n){std::string out(4*((n+2)/3),'\0');int size=EVP_EncodeBlock(reinterpret_cast<unsigned char*>(out.data()),p,static_cast<int>(n));out.resize(size);return out;}
std::vector<unsigned char> unb64(const std::string& s){if(s.empty()||s.size()%4)throw std::runtime_error("Invalid key-vault payload");std::vector<unsigned char> out(s.size());int n=EVP_DecodeBlock(out.data(),reinterpret_cast<const unsigned char*>(s.data()),static_cast<int>(s.size()));if(n<0)throw std::runtime_error("Invalid key-vault payload");if(s.back()=='=')--n;if(s.size()>1&&s[s.size()-2]=='=')--n;out.resize(n);return out;}
std::array<unsigned char,32> master(const std::string& secret){if(secret.size()<32)throw std::runtime_error("Security/KeyVaultMasterKey must contain at least 32 characters on every cluster node");std::array<unsigned char,32> key{};unsigned int n=0;EVP_Digest(secret.data(),secret.size(),key.data(),&n,EVP_sha256(),nullptr);return key;}
std::string seal(const std::string& plain,const std::string& secret){auto key=master(secret);std::array<unsigned char,12> nonce{};if(RAND_bytes(nonce.data(),nonce.size())!=1)throw std::runtime_error("Cannot create key-vault nonce");std::vector<unsigned char> cipher(plain.size()+16);EVP_CIPHER_CTX* raw=EVP_CIPHER_CTX_new();if(!raw)throw std::runtime_error("Cannot create key-vault cipher");std::unique_ptr<EVP_CIPHER_CTX,decltype(&EVP_CIPHER_CTX_free)> ctx(raw,EVP_CIPHER_CTX_free);int n=0,total=0;if(EVP_EncryptInit_ex(raw,EVP_aes_256_gcm(),nullptr,nullptr,nullptr)!=1||EVP_CIPHER_CTX_ctrl(raw,EVP_CTRL_GCM_SET_IVLEN,nonce.size(),nullptr)!=1||EVP_EncryptInit_ex(raw,nullptr,nullptr,key.data(),nonce.data())!=1||EVP_EncryptUpdate(raw,cipher.data(),&n,reinterpret_cast<const unsigned char*>(plain.data()),static_cast<int>(plain.size()))!=1)throw std::runtime_error("Cannot encrypt key-vault record");total=n;if(EVP_EncryptFinal_ex(raw,cipher.data()+total,&n)!=1)throw std::runtime_error("Cannot encrypt key-vault record");total+=n;std::array<unsigned char,16>tag{};EVP_CIPHER_CTX_ctrl(raw,EVP_CTRL_GCM_GET_TAG,tag.size(),tag.data());std::vector<unsigned char> envelope;envelope.insert(envelope.end(),nonce.begin(),nonce.end());envelope.insert(envelope.end(),tag.begin(),tag.end());envelope.insert(envelope.end(),cipher.begin(),cipher.begin()+total);return b64(envelope.data(),envelope.size());}
std::string open(const std::string& encoded,const std::string& secret){auto in=unb64(encoded);if(in.size()<28)throw std::runtime_error("Invalid key-vault envelope");auto key=master(secret);std::vector<unsigned char> plain(in.size());EVP_CIPHER_CTX* raw=EVP_CIPHER_CTX_new();if(!raw)throw std::runtime_error("Cannot create key-vault cipher");std::unique_ptr<EVP_CIPHER_CTX,decltype(&EVP_CIPHER_CTX_free)> ctx(raw,EVP_CIPHER_CTX_free);int n=0,total=0;if(EVP_DecryptInit_ex(raw,EVP_aes_256_gcm(),nullptr,nullptr,nullptr)!=1||EVP_CIPHER_CTX_ctrl(raw,EVP_CTRL_GCM_SET_IVLEN,12,nullptr)!=1||EVP_DecryptInit_ex(raw,nullptr,nullptr,key.data(),in.data())!=1||EVP_DecryptUpdate(raw,plain.data(),&n,in.data()+28,static_cast<int>(in.size()-28))!=1)throw std::runtime_error("Cannot decrypt key-vault record");total=n;EVP_CIPHER_CTX_ctrl(raw,EVP_CTRL_GCM_SET_TAG,16,in.data()+12);if(EVP_DecryptFinal_ex(raw,plain.data()+total,&n)!=1)throw std::runtime_error("Key-vault authentication failed; verify the cluster vault key");total+=n;return std::string(reinterpret_cast<char*>(plain.data()),total);}
boost::json::object load(SQLHDBC db,const std::string& secret,const std::string& id){validId(id);auto rows=query(db,"SELECT encrypted_payload FROM etlforge_security.key_vault WHERE key_id=CAST(? AS uuid)",{id});if(rows.empty())throw std::runtime_error("SSH/PGP key is missing from the database");return boost::json::parse(open(rows[0][0],secret)).as_object();}
std::string trim(std::string s){while(!s.empty()&&(s.back()=='\r'||s.back()=='\n'||s.back()==' '))s.pop_back();return s;}
boost::json::object metadata(const boost::json::object& o){return {{"type",value(o,"type").empty()?"ssh":value(o,"type")},{"hasPrivate",!value(o,"privateKey").empty()},{"id",value(o,"id")},{"name",value(o,"name")},{"fingerprint",value(o,"fingerprint")},{"publicKey",value(o,"publicKey")},{"updatedAt",value(o,"updatedAt")}};}
struct Handles {std::vector<std::pair<HANDLE,fs::path>> handles;~Handles(){for(auto& h:handles){CloseHandle(h.first);DeleteFileW(h.second.c_str());}}};
std::string temporary(const fs::path& dir,const std::string& data,Handles& handles){
    fs::path p=dir/(guid()+".pem");Security s;
    HANDLE h=CreateFileW(p.c_str(),GENERIC_READ|GENERIC_WRITE,FILE_SHARE_READ,&s.attributes,CREATE_NEW,FILE_ATTRIBUTE_TEMPORARY,nullptr);
    if(h==INVALID_HANDLE_VALUE)throw std::runtime_error("Cannot open protected temporary SSH key");
    handles.handles.push_back({h,p});DWORD n=0;if(!WriteFile(h,data.data(),static_cast<DWORD>(data.size()),&n,nullptr)||n!=data.size()||!FlushFileBuffers(h))throw std::runtime_error("Cannot prepare SSH key");
    auto result=p.u8string();bool ascii=true;for(unsigned char c:result)if(c>127)ascii=false;if(!ascii){wchar_t alias[32768];DWORD count=GetShortPathNameW(p.c_str(),alias,32768);if(!count||count>=32768)throw std::runtime_error("SSH key path requires a Windows short path alias");result.clear();for(DWORD i=0;i<count;++i){if(alias[i]>127)throw std::runtime_error("SSH key path requires an ASCII Windows short path alias");result+=static_cast<char>(alias[i]);}}
    return result;
}
}
KeyLease::~KeyLease(){if(!passphrase.empty())SecureZeroMemory(&passphrase[0],passphrase.size());}
std::string fingerprint(const std::string& key){
    auto a=key.find(' ');if(a==std::string::npos)throw std::runtime_error("Import an OpenSSH public key");auto b=key.find(' ',a+1);std::string type=key.substr(0,a),encoded=key.substr(a+1,b==std::string::npos?b:b-a-1);
    if(type!="ssh-ed25519"&&type!="ssh-rsa"&&type.rfind("ecdsa-sha2-",0)!=0)throw std::runtime_error("Supported SSH keys: Ed25519, RSA and ECDSA");
    if(encoded.empty()||encoded.size()>32768||encoded.size()%4||encoded.find_first_not_of("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=")!=std::string::npos)throw std::runtime_error("Invalid SSH public key");
    std::vector<unsigned char> bytes(encoded.size());int n=EVP_DecodeBlock(bytes.data(),reinterpret_cast<const unsigned char*>(encoded.data()),static_cast<int>(encoded.size()));if(n<0)throw std::runtime_error("Invalid SSH public key");if(encoded.back()=='=')--n;if(encoded.size()>1&&encoded[encoded.size()-2]=='=')--n;
    if(n<4)throw std::runtime_error("Invalid SSH public key");size_t len=(bytes[0]<<24)|(bytes[1]<<16)|(bytes[2]<<8)|bytes[3];if(len!=type.size()||len+4>static_cast<size_t>(n)||std::string(reinterpret_cast<char*>(bytes.data()+4),len)!=type)throw std::runtime_error("Public key type does not match its contents");
    unsigned char digest[32];unsigned int size=0;if(!EVP_Digest(bytes.data(),n,digest,&size,EVP_sha256(),nullptr))throw std::runtime_error("Cannot fingerprint SSH key");unsigned char out[48];int count=EVP_EncodeBlock(out,digest,size);std::string result(reinterpret_cast<char*>(out),count);while(result.back()=='=')result.pop_back();return "SHA256:"+result;
}
std::string validateHostFingerprint(const std::string& input){std::string s=input;if(s.rfind("SHA256:",0)==0)s.erase(0,7);while(!s.empty()&&s.back()=='=')s.pop_back();if(s.size()!=43||s.find_first_not_of("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/")!=std::string::npos)throw std::runtime_error("Enter the verified SFTP host SHA256 fingerprint");return s;}
std::string request(void* handle,const std::string& root,const std::string& vaultKey,const std::string& json,bool administrator){
    auto database=static_cast<SQLHDBC>(handle);
    std::lock_guard<std::mutex> lock(mutex);auto input=boost::json::parse(json).as_object();std::string action=value(input,"action");if(action!="ssh.list"&&!administrator)throw std::runtime_error("SSH key management requires an administrator");auto dir=directory(root);
    if(action=="ssh.save"){
        auto id=value(input,"id");if(id.empty())id=guid();validId(id);boost::json::object o;auto existing=query(database,"SELECT encrypted_payload FROM etlforge_security.key_vault WHERE key_id=CAST(? AS uuid)",{id});if(!existing.empty())o=boost::json::parse(open(existing[0][0],vaultKey)).as_object();
        auto name=trim(value(input,"name"));if(name.empty()||name.size()>128||name.find_first_of("\r\n")!=std::string::npos)throw std::runtime_error("Enter an SSH key name (1-128 UTF-8 bytes)");
        auto duplicate=query(database,"SELECT key_id::text FROM etlforge_security.key_vault WHERE key_name=? AND key_id<>CAST(? AS uuid)",{name,id});if(!duplicate.empty())throw std::runtime_error("An SSH key with this name already exists");
        auto type=value(input,"type");if(type.empty())type="ssh";if(type!="ssh"&&type!="pgp")throw std::runtime_error("Unknown key type");
        auto oldType=value(o,"type");if(!o.empty()&&(oldType.empty()?"ssh":oldType)!=type)throw std::runtime_error("Create a new key record to change its type");
        o["type"]=type;
        auto suppliedPass=value(input,"passphrase");if(suppliedPass.size()>1024||suppliedPass.find_first_of("\r\n")!=std::string::npos||suppliedPass.find('\0')!=std::string::npos)throw std::runtime_error("Key passphrase must be one line, maximum 1024 UTF-8 bytes");
        auto privateKey=value(input,"privateKey"),publicKey=value(input,"publicKey");
        if(type=="pgp"&&(!privateKey.empty()||o.empty()||value(o,"publicKey").empty()||(!publicKey.empty()&&publicKey!=value(o,"publicKey")))){
            auto material=privateKey.empty()?publicKey:privateKey;
            if(material.size()>65536||material.find("-----BEGIN PGP ")!=0||material.find("KEY BLOCK-----")==std::string::npos)throw std::runtime_error("Import an ASCII-armored OpenPGP key (maximum 64 KiB)");
            Handles handles;auto file=temporary(dir,material,handles);auto info=etlforge::pgp::inspect(pgpRuntime(root),dir.u8string(),file);
            o["privateKey"]=info.hasPrivate?material:"";o["passphrase"]=info.hasPrivate?value(input,"passphrase"):"";o["publicKey"]=info.publicKey;o["fingerprint"]=info.fingerprint;
        }else if(type=="ssh"&&!privateKey.empty()){
            if(privateKey.size()>65536||privateKey.find('\0')!=std::string::npos||privateKey.find("-----BEGIN ")!=0||privateKey.find("PRIVATE KEY-----")==std::string::npos||privateKey.find("-----END ")==std::string::npos)throw std::runtime_error("Import a PEM or OpenSSH private key (maximum 64 KiB)");
            o["privateKey"]=privateKey;o["passphrase"]=value(input,"passphrase");o["publicKey"]=trim(value(input,"publicKey"));o["fingerprint"]=fingerprint(value(o,"publicKey"));
        }
        if(type=="ssh"&&value(o,"privateKey").empty())throw std::runtime_error("Import both private and public key files");
        o["id"]=id;o["name"]=name;SYSTEMTIME t;GetSystemTime(&t);char date[32];sprintf_s(date,"%04u-%02u-%02uT%02u:%02u:%02uZ",t.wYear,t.wMonth,t.wDay,t.wHour,t.wMinute,t.wSecond);o["updatedAt"]=date;
        query(database,"INSERT INTO etlforge_security.key_vault(key_id,key_type,key_name,fingerprint,public_key,has_private,encrypted_payload) VALUES(CAST(? AS uuid),?,?,?,?,CAST(? AS boolean),?) ON CONFLICT(key_id) DO UPDATE SET key_type=EXCLUDED.key_type,key_name=EXCLUDED.key_name,fingerprint=EXCLUDED.fingerprint,public_key=EXCLUDED.public_key,has_private=EXCLUDED.has_private,encrypted_payload=EXCLUDED.encrypted_payload,encryption_version=1,updated_at=now()",{id,type,name,value(o,"fingerprint"),value(o,"publicKey"),value(o,"privateKey").empty()?"false":"true",seal(boost::json::serialize(o),vaultKey)});
    }else if(action=="ssh.delete") {auto id=value(input,"id");validId(id);if(query(database,"DELETE FROM etlforge_security.key_vault WHERE key_id=CAST(? AS uuid) RETURNING key_id::text",{id}).empty())throw std::runtime_error("SSH/PGP key is missing from the database");}
    else if(action!="ssh.list")throw std::runtime_error("Unknown SSH key action");
    boost::json::array keys;for(auto& row:query(database,"SELECT key_type,has_private::text,key_id::text,key_name,fingerprint,public_key,to_char(updated_at AT TIME ZONE 'UTC','YYYY-MM-DD\"T\"HH24:MI:SS\"Z\"') FROM etlforge_security.key_vault ORDER BY lower(key_name),key_id"))keys.push_back(boost::json::object{{"type",row[0]},{"hasPrivate",row[1]=="true"},{"id",row[2]},{"name",row[3]},{"fingerprint",row[4]},{"publicKey",row[5]},{"updatedAt",row[6]}});return boost::json::serialize(boost::json::object{{"keys",keys},{"canManage",administrator}});
}
std::shared_ptr<KeyLease> lease(void* handle,const std::string& root,const std::string& vaultKey,const std::string& id,const std::string& type){auto database=static_cast<SQLHDBC>(handle);std::lock_guard<std::mutex> lock(mutex);ProcessLock processLock;auto dir=directory(root);auto o=load(database,vaultKey,id);auto stored=value(o,"type");if((stored.empty()?"ssh":stored)!=type)throw std::runtime_error("Wrong key type selected for endpoint");auto result=std::make_shared<KeyLease>();auto handles=std::make_shared<Handles>();result->lifetime=handles;result->fingerprint=value(o,"fingerprint");result->privateFile=temporary(dir,value(o,"privateKey").empty()?value(o,"publicKey"):value(o,"privateKey"),*handles);result->publicFile=temporary(dir,value(o,"publicKey")+"\n",*handles);result->passphrase=value(o,"passphrase");return result;}
std::string pgpRuntime(const std::string& root){fs::path p(wide(root));while(!p.empty()){auto executable=p/L"runtime"/L"gnupg"/L"bin"/L"gpg.exe";if(fs::is_regular_file(executable))return executable.u8string();auto parent=p.parent_path();if(parent==p)break;p=parent;}throw std::runtime_error("Bundled x64 GnuPG runtime is missing");}
void cleanup(const std::string& root){std::lock_guard<std::mutex> lock(mutex);ProcessLock processLock;directory(root);}
}}
