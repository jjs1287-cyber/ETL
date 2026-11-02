#include <winsock2.h>
#include <ws2tcpip.h>
#include <windows.h>
#include <winnetwk.h>
#include <objbase.h>
#include <libpq-fe.h>
#include <curl/curl.h>
#include "CloudSettings.h"
#include <algorithm>
#include <chrono>
#include <filesystem>
#include <fstream>
#include <iomanip>
#include <memory>
#include <sstream>

namespace fs=std::filesystem;
using namespace etlforge::cloud;
namespace {
HANDLE stopped=nullptr;SERVICE_STATUS_HANDLE serviceHandle=nullptr;SERVICE_STATUS serviceStatus{};std::string vaultKey;
std::wstring serviceName=L"ETLForge.Dev.CloudLogging";fs::path home;
std::wstring wide(const std::string& text) {
 if(text.empty())return {};int size=MultiByteToWideChar(CP_UTF8,MB_ERR_INVALID_CHARS,text.data(),static_cast<int>(text.size()),nullptr,0);
 if(!size)throw std::runtime_error("Invalid UTF-8 text");std::wstring result(size,0);MultiByteToWideChar(CP_UTF8,MB_ERR_INVALID_CHARS,text.data(),static_cast<int>(text.size()),result.data(),size);return result;
}
std::string utf8(const std::wstring& text) {int n=WideCharToMultiByte(CP_UTF8,WC_ERR_INVALID_CHARS,text.data(),static_cast<int>(text.size()),nullptr,0,nullptr,nullptr);std::string s(n,0);WideCharToMultiByte(CP_UTF8,WC_ERR_INVALID_CHARS,text.data(),static_cast<int>(text.size()),s.data(),n,nullptr,nullptr);return s;}
std::string limited(std::string text,size_t limit=1048576) {
 if(text.size()>limit){size_t end=limit;while(end && (static_cast<unsigned char>(text[end])&0xc0)==0x80)--end;text.resize(end);}return text;
}
void log(const std::string& message) {
 fs::create_directories(home/L"logs");SYSTEMTIME t;GetLocalTime(&t);std::ostringstream name;name<<"CloudLogging-"<<t.wYear<<std::setw(2)<<std::setfill('0')<<t.wMonth<<std::setw(2)<<t.wDay<<".log";
 std::ofstream out(home/L"logs"/wide(name.str()),std::ios::app|std::ios::binary);out<<t.wYear<<'-'<<t.wMonth<<'-'<<t.wDay<<' '<<t.wHour<<':'<<t.wMinute<<':'<<t.wSecond<<" "<<message<<"\r\n";
}
struct Result {PGresult* p;~Result(){PQclear(p);}std::string at(int row,int column)const{return PQgetisnull(p,row,column)?"":PQgetvalue(p,row,column);}int size()const{return PQntuples(p);} };
class Db {
 PGconn* p;
public:
 explicit Db(const std::string& connection):p(PQconnectdb(connection.c_str())){if(PQstatus(p)!=CONNECTION_OK){PQfinish(p);throw std::runtime_error("Cloud logging database connection failed");}PQsetClientEncoding(p,"UTF8");}
 ~Db(){PQfinish(p);}
 Result query(const std::string& sql,const std::vector<std::string>& parameters={}) {
  std::vector<const char*> args;for(auto& s:parameters)args.push_back(s.c_str());
  PGresult* r=PQexecParams(p,sql.c_str(),static_cast<int>(args.size()),nullptr,args.data(),nullptr,nullptr,0);
  auto status=PQresultStatus(r);if(status!=PGRES_TUPLES_OK && status!=PGRES_COMMAND_OK){std::string state=PQresultErrorField(r,PG_DIAG_SQLSTATE)?PQresultErrorField(r,PG_DIAG_SQLSTATE):"unknown";PQclear(r);throw std::runtime_error("Cloud logging database request failed (SQLSTATE "+state+")");}return {r};
 }
};
std::string guid() {GUID g;CoCreateGuid(&g);wchar_t text[40];StringFromGUID2(g,text,40);return utf8(std::wstring(text+1,36));}
std::pair<std::string,std::string> serverIdentity() {
 char host[256]{};gethostname(host,sizeof(host));std::string ip;addrinfo hints{},*addresses=nullptr;hints.ai_family=AF_INET;
 if(getaddrinfo(host,nullptr,&hints,&addresses)==0){for(auto* a=addresses;a;a=a->ai_next){char b[64]{};inet_ntop(AF_INET,&reinterpret_cast<sockaddr_in*>(a->ai_addr)->sin_addr,b,sizeof(b));if(!ip.empty())ip+=',';ip+=b;}freeaddrinfo(addresses);}return {host,ip};
}
std::string csv(const boost::json::object& payload) {
 const char* fields[]={"Operation Name","Project","Environment","Status","Started","Finished","Server ip","Server Name","Operation messages","Operation ID","Operation Instance ID","Upload ID","Messages Truncated","Server Time Zone"};
 auto quote=[](const std::string& s){std::string out="\"";for(char c:s){if(c=='\"')out+='\"';out+=c;}return out+'\"';};
 std::string header,line;for(auto f:fields){if(!header.empty()){header+=',';line+=',';}header+=quote(f);auto p=payload.if_contains(f);line+=quote(p&&p->is_string()?std::string(p->as_string()):p?boost::json::serialize(*p):"");}return header+"\r\n"+line+"\r\n";
}
struct Body {const std::string* data;size_t offset=0;};
size_t readBody(char* buffer,size_t size,size_t count,void* context){auto& b=*static_cast<Body*>(context);size_t n=std::min(size*count,b.data->size()-b.offset);memcpy(buffer,b.data->data()+b.offset,n);b.offset+=n;return n;}
size_t discard(char*,size_t size,size_t count,void*){return size*count;}
int progress(void*,curl_off_t,curl_off_t,curl_off_t,curl_off_t){return WaitForSingleObject(stopped,0)==WAIT_OBJECT_0?1:0;}
struct Transport {
 CURL* curl=curl_easy_init();boost::json::object config;std::wstring share;bool connectedShare=false;
 explicit Transport(boost::json::object settings):config(std::move(settings)) {
  if(!curl)throw std::runtime_error("Cannot initialize cloud transport");
  if(value(config,"type")=="smb" && !value(config,"login").empty()) {
   auto p=wide(value(config,"path"));std::replace(p.begin(),p.end(),L'/',L'\\');size_t server=p.find(L'\\',2),end=p.find(L'\\',server+1);share=p.substr(0,end);
   auto login=wide(value(config,"login")),password=wide(protect(value(config,"passwordProtected"),vaultKey,true));NETRESOURCEW resource{};resource.dwType=RESOURCETYPE_DISK;resource.lpRemoteName=share.data();
   DWORD status=WNetAddConnection2W(&resource,password.c_str(),login.c_str(),CONNECT_TEMPORARY);SecureZeroMemory(password.data(),password.size()*sizeof(wchar_t));
   if(status!=NO_ERROR){curl_easy_cleanup(curl);curl=nullptr;throw std::runtime_error("SMB authentication failed (Windows "+std::to_string(status)+")");}connectedShare=true;
  }
 }
 ~Transport(){if(curl)curl_easy_cleanup(curl);if(connectedShare)WNetCancelConnection2W(share.c_str(),0,FALSE);}
 void upload(const std::string& filename,const std::string& uploadId,const boost::json::object& payload) {
  auto kind=value(config,"type");auto data=kind=="http"?boost::json::serialize(payload):csv(payload);
  if(kind=="smb") {
   fs::path destination=wide(value(config,"path"));auto final=destination/wide(filename+".csv"),temp=destination/wide(filename+".part");
   HANDLE file=CreateFileW(temp.c_str(),GENERIC_WRITE,0,nullptr,CREATE_ALWAYS,FILE_ATTRIBUTE_NORMAL,nullptr);
   if(file==INVALID_HANDLE_VALUE)throw std::runtime_error("SMB file creation failed (Windows "+std::to_string(GetLastError())+")");
   DWORD written=0;BOOL ok=WriteFile(file,data.data(),static_cast<DWORD>(data.size()),&written,nullptr);if(ok)ok=FlushFileBuffers(file);DWORD error=GetLastError();CloseHandle(file);
   if(!ok || written!=data.size()){DeleteFileW(temp.c_str());throw std::runtime_error("SMB file write failed (Windows "+std::to_string(error)+")");}
   if(!MoveFileExW(temp.c_str(),final.c_str(),MOVEFILE_REPLACE_EXISTING|MOVEFILE_WRITE_THROUGH))throw std::runtime_error("SMB atomic publish failed (Windows "+std::to_string(GetLastError())+")");return;
  }
  curl_easy_reset(curl);Body body{&data};curl_easy_setopt(curl,CURLOPT_NOSIGNAL,1L);curl_easy_setopt(curl,CURLOPT_CONNECTTIMEOUT,10L);curl_easy_setopt(curl,CURLOPT_TIMEOUT,30L);
  curl_easy_setopt(curl,CURLOPT_WRITEFUNCTION,discard);curl_easy_setopt(curl,CURLOPT_NOPROGRESS,0L);curl_easy_setopt(curl,CURLOPT_XFERINFOFUNCTION,progress);
  curl_easy_setopt(curl,CURLOPT_FOLLOWLOCATION,0L);curl_easy_setopt(curl,CURLOPT_SSL_VERIFYPEER,1L);curl_easy_setopt(curl,CURLOPT_SSL_VERIFYHOST,2L);
  curl_easy_setopt(curl,CURLOPT_SSL_OPTIONS,static_cast<long>(CURLSSLOPT_NATIVE_CA));
  curl_slist* headers=nullptr;curl_slist* commands=nullptr;std::string secret,url;
  if(kind=="http") {
   url=value(config,"url");curl_easy_setopt(curl,CURLOPT_PROTOCOLS_STR,"http,https");curl_easy_setopt(curl,CURLOPT_POST,1L);curl_easy_setopt(curl,CURLOPT_POSTFIELDS,data.data());curl_easy_setopt(curl,CURLOPT_POSTFIELDSIZE_LARGE,static_cast<curl_off_t>(data.size()));
   headers=curl_slist_append(headers,"Content-Type: application/json; charset=utf-8");headers=curl_slist_append(headers,("Idempotency-Key: "+uploadId).c_str());
   secret=protect(value(config,"bearerProtected"),vaultKey,true);if(!secret.empty())headers=curl_slist_append(headers,("Authorization: Bearer "+secret).c_str());curl_easy_setopt(curl,CURLOPT_HTTPHEADER,headers);
   if(secret.empty() && !value(config,"login").empty()){secret=protect(value(config,"passwordProtected"),vaultKey,true);curl_easy_setopt(curl,CURLOPT_HTTPAUTH,CURLAUTH_BASIC);curl_easy_setopt(curl,CURLOPT_USERNAME,value(config,"login").c_str());curl_easy_setopt(curl,CURLOPT_PASSWORD,secret.c_str());}
  }else {
   auto path=value(config,"path");while(path.size()>1 && path.back()=='/')path.pop_back();
   if(path.find_first_of("\r\n\"'")!=std::string::npos)throw std::runtime_error("Invalid SFTP directory");
   auto remote=path+"/"+filename+".csv";char* escaped=curl_easy_escape(curl,(remote+".part").c_str(),0);std::string encoded=escaped?escaped:"";curl_free(escaped);
   auto host=value(config,"host");if(host.find(':')!=std::string::npos)host="["+host+"]";
   url="sftp://"+host+":"+std::to_string(config.at("port").as_int64())+"/"+encoded;
   secret=protect(value(config,"passwordProtected"),vaultKey,true);
   curl_easy_setopt(curl,CURLOPT_PROTOCOLS_STR,"sftp");curl_easy_setopt(curl,CURLOPT_USERNAME,value(config,"login").c_str());curl_easy_setopt(curl,CURLOPT_PASSWORD,secret.c_str());curl_easy_setopt(curl,CURLOPT_SSH_AUTH_TYPES,CURLSSH_AUTH_PASSWORD);
   auto fingerprint=value(config,"hostKeySha256");if(fingerprint.rfind("SHA256:",0)==0)fingerprint.erase(0,7);curl_easy_setopt(curl,CURLOPT_SSH_HOST_PUBLIC_KEY_SHA256,fingerprint.c_str());
   curl_easy_setopt(curl,CURLOPT_UPLOAD,1L);curl_easy_setopt(curl,CURLOPT_READFUNCTION,readBody);curl_easy_setopt(curl,CURLOPT_READDATA,&body);curl_easy_setopt(curl,CURLOPT_INFILESIZE_LARGE,static_cast<curl_off_t>(data.size()));
   commands=curl_slist_append(commands,("rename \""+remote+".part\" \""+remote+"\"").c_str());curl_easy_setopt(curl,CURLOPT_POSTQUOTE,commands);
  }
  curl_easy_setopt(curl,CURLOPT_URL,url.c_str());CURLcode result=curl_easy_perform(curl);long response=0;curl_easy_getinfo(curl,CURLINFO_RESPONSE_CODE,&response);
  curl_slist_free_all(headers);curl_slist_free_all(commands);SecureZeroMemory(secret.data(),secret.size());
  if(result!=CURLE_OK)throw std::runtime_error("Cloud "+kind+" upload failed (curl "+std::to_string(result)+")");
  if(kind=="http" && (response<200 || response>=300))throw std::runtime_error("Cloud HTTP upload failed (HTTP "+std::to_string(response)+")");
 }
};
void stage(Db& db,const boost::json::object& settings) {
 auto identity=serverIdentity();auto history=settings.if_contains("includeHistory");bool include=history&&history->as_bool();
 auto rows=db.query("SELECT l.operation_instance_id::text,l.operation_id::text,COALESCE(o.operation_name,'[deleted operation]'),COALESCE((SELECT string_agg(p.interchangeproject_name, ', ' ORDER BY p.interchangeproject_name) FROM interchangeprojectoperationstab x JOIN interchangeprojectstab p USING(interchangeproject_id) WHERE x.item_id=l.operation_id),'[deleted project]'),l.status::text,COALESCE(l.started_ts::text,''),l.status_ts::text,to_char(COALESCE(l.started_ts,l.entered_ts),'YYYYMMDD\"T\"HH24MISSMS'),current_setting('TimeZone') FROM operationlogtab l JOIN operationinstancetab i USING(operation_instance_id) LEFT JOIN operationstab o ON o.operation_id=l.operation_id WHERE l.status IN (1,3,4,5,6) AND i.cloud_upload_status<>1 AND ($1='true' OR l.status_ts >= (SELECT activated_at FROM cloudlogging.configuration WHERE singleton)) AND l.operation_id <> ALL(string_to_array($2,',')::integer[]) AND NOT EXISTS(SELECT 1 FROM cloudlogging.delivery d WHERE d.operation_instance_id=l.operation_instance_id) ORDER BY l.operation_instance_id LIMIT 100",{include?"true":"false",value(settings,"excludeOperationIds")});
 const char* statuses[]={"Pending","Cancelled","Running","Success","Success with information","Success with warning","Error"};
 for(int r=0;r<rows.size();++r) {
  auto id=rows.at(r,0);auto messages=db.query("WITH m AS (SELECT operation_message_id,message,COALESCE(sum(octet_length(message)+1) OVER(ORDER BY operation_message_id ROWS BETWEEN UNBOUNDED PRECEDING AND 1 PRECEDING),0) used FROM operationmessagetab WHERE operation_instance_id=$1::bigint) SELECT COALESCE(string_agg(left(message,GREATEST(0,1048576-used)::integer),E'\\n' ORDER BY operation_message_id),''),(SELECT COALESCE(sum(octet_length(message)+1),0)>1048576 FROM operationmessagetab WHERE operation_instance_id=$1::bigint)::text FROM m WHERE used<1048576",{id});
  auto upload=guid();std::ostringstream filename;filename<<std::setfill('0')<<std::setw(20)<<std::stoll(rows.at(r,1))<<'_'<<rows.at(r,7)<<'_'<<upload;
  auto raw=messages.at(0,0);bool truncated=raw.size()>1048576||messages.at(0,1)=="true";
  boost::json::object payload{{"Operation Name",rows.at(r,2)},{"Project",rows.at(r,3)},{"Environment",value(settings,"environment","dev")},{"Status",statuses[std::stoi(rows.at(r,4))]},{"Started",rows.at(r,5)},{"Finished",rows.at(r,6)},{"Server ip",identity.second},{"Server Name",identity.first},{"Operation messages",limited(std::move(raw))},{"Operation ID",rows.at(r,1)},{"Operation Instance ID",id},{"Upload ID",upload},{"Messages Truncated",truncated},{"Server Time Zone",rows.at(r,8)}};
  db.query("INSERT INTO cloudlogging.delivery(operation_instance_id,upload_id,filename,payload) VALUES($1::bigint,$2::uuid,$3,$4::jsonb) ON CONFLICT DO NOTHING",{id,upload,filename.str(),boost::json::serialize(payload)});
 }
}
void batch(Db& db,const std::string& uploaderId) {
 auto rows=db.query("SELECT settings::text,revision::text FROM cloudlogging.configuration WHERE singleton");if(!rows.size())return;auto config=boost::json::parse(rows.at(0,0)).as_object();if(!config.at("enabled").as_bool())return;validate(config);stage(db,config);
 std::unique_ptr<Transport> transport;std::string setupError;try{transport=std::make_unique<Transport>(config);}catch(const std::exception& e){setupError=e.what();}
 for(int processed=0;processed<100 && WaitForSingleObject(stopped,0)!=WAIT_OBJECT_0;++processed) {
  auto pending=db.query("WITH candidate AS (SELECT operation_instance_id FROM cloudlogging.delivery WHERE uploaded_at IS NULL AND attempts<5 AND next_attempt_at<=now() AND (lease_until IS NULL OR lease_until<now()) ORDER BY operation_instance_id LIMIT 1 FOR UPDATE SKIP LOCKED) UPDATE cloudlogging.delivery d SET lease_owner=$1::uuid,lease_generation=d.lease_generation+1,lease_until=now()+interval '2 minutes' FROM candidate c WHERE d.operation_instance_id=c.operation_instance_id RETURNING d.operation_instance_id::text,d.upload_id::text,d.filename,d.payload::text,d.attempts::text,d.lease_generation::text",{uploaderId});
  if(!pending.size())break;
  auto latest=db.query("SELECT settings->>'enabled',revision::text FROM cloudlogging.configuration WHERE singleton");
  if(!latest.size() || latest.at(0,0)!="true" || latest.at(0,1)!=rows.at(0,1)){db.query("UPDATE cloudlogging.delivery SET lease_owner=NULL,lease_until=NULL WHERE operation_instance_id=$1::bigint AND lease_owner=$2::uuid AND lease_generation=$3::bigint",{pending.at(0,0),uploaderId,pending.at(0,5)});break;}
  bool success=false;std::string error;
  try{if(!transport)throw std::runtime_error(setupError);transport->upload(pending.at(0,2),pending.at(0,1),boost::json::parse(pending.at(0,3)).as_object());success=true;}
  catch(const std::exception& e){error=e.what();int attempt=std::stoi(pending.at(0,4))+1;log("Operation instance "+pending.at(0,0)+", attempt "+std::to_string(attempt)+"/5: "+error+(attempt>=5?"; retry limit reached, automatic uploads stopped":""));}
  db.query("BEGIN");try {
   auto acknowledged=db.query("UPDATE cloudlogging.delivery SET attempts=attempts+1,uploaded_at=CASE WHEN $4='1' THEN now() ELSE NULL END,payload=CASE WHEN $4='1' THEN '{}'::jsonb ELSE payload END,last_error=NULLIF($5,''),next_attempt_at=now()+make_interval(secs=>LEAST(3600,30*power(2,LEAST(attempts,7)))::integer),lease_owner=NULL,lease_until=NULL WHERE operation_instance_id=$1::bigint AND lease_owner=$2::uuid AND lease_generation=$3::bigint RETURNING operation_instance_id::text",{pending.at(0,0),uploaderId,pending.at(0,5),success?"1":"2",error});
   if(acknowledged.size())db.query("UPDATE operationinstancetab SET cloud_upload_status=$2::smallint WHERE operation_instance_id=$1::bigint",{pending.at(0,0),success?"1":"2"});db.query("COMMIT");
  }catch(...){db.query("ROLLBACK");throw;}
 }
}
void report(DWORD state,DWORD error=NO_ERROR) {if(!serviceHandle)return;serviceStatus.dwServiceType=SERVICE_WIN32_OWN_PROCESS;serviceStatus.dwCurrentState=state;serviceStatus.dwControlsAccepted=state==SERVICE_RUNNING?SERVICE_ACCEPT_STOP|SERVICE_ACCEPT_SHUTDOWN:0;serviceStatus.dwWin32ExitCode=error;serviceStatus.dwWaitHint=state==SERVICE_STOP_PENDING?40000:0;SetServiceStatus(serviceHandle,&serviceStatus);}
DWORD WINAPI control(DWORD code,DWORD,void*,void*){if(code==SERVICE_CONTROL_STOP||code==SERVICE_CONTROL_SHUTDOWN){report(SERVICE_STOP_PENDING);SetEvent(stopped);}return NO_ERROR;}
int run(bool once) {
 std::ifstream settings(home/L"etlforge.conf",std::ios::binary);std::string configuration((std::istreambuf_iterator<char>(settings)),{});auto marker=configuration.find("KeyVaultMasterKey");if(marker==std::string::npos)throw std::runtime_error("Missing Security/KeyVaultMasterKey");auto equals=configuration.find('=',marker),end=configuration.find_first_of("\r\n",equals);vaultKey=configuration.substr(equals+1,end-equals-1);vaultKey.erase(0,vaultKey.find_first_not_of(" \t"));vaultKey.erase(vaultKey.find_last_not_of(" \t")+1);
 auto connectionFile=home/L"services"/L"CloudLogging.connection";std::ifstream input(connectionFile,std::ios::binary);std::string connection((std::istreambuf_iterator<char>(input)),{});if(connection.empty())throw std::runtime_error("Missing CloudLogging.connection configuration");
 HANDLE mutex=CreateMutexW(nullptr,FALSE,(L"Global\\ETLForgeCloudLogging-"+std::to_wstring(std::hash<std::wstring>{}(home.wstring()))).c_str());if(!mutex || WaitForSingleObject(mutex,0)!=WAIT_OBJECT_0)throw std::runtime_error("Cloud logging is already running for this instance");
 const auto uploaderId=guid();
 do{try{Db db(connection);batch(db,uploaderId);}catch(const std::exception& e){log(e.what());if(once){ReleaseMutex(mutex);CloseHandle(mutex);return 1;}}
  std::ofstream heartbeat(home/L"services"/L"CloudLogging.heartbeat",std::ios::binary|std::ios::trunc);heartbeat<<GetTickCount64()<<"\n";heartbeat.close();
 }while(!once && WaitForSingleObject(stopped,10000)==WAIT_TIMEOUT);
 ReleaseMutex(mutex);CloseHandle(mutex);return 0;
}
void WINAPI serviceMain(DWORD,wchar_t**) {serviceHandle=RegisterServiceCtrlHandlerExW(serviceName.c_str(),control,nullptr);report(SERVICE_START_PENDING);try{report(SERVICE_RUNNING);int result=run(false);report(SERVICE_STOPPED,result?ERROR_SERVICE_SPECIFIC_ERROR:NO_ERROR);}catch(const std::exception& e){log(e.what());report(SERVICE_STOPPED,ERROR_SERVICE_SPECIFIC_ERROR);}}
}
int wmain(int argc,wchar_t** argv) {
 bool once=false,console=false,health=false;for(int i=1;i<argc;++i){std::wstring arg=argv[i];if(arg==L"--home"&&i+1<argc)home=argv[++i];else if(arg==L"--name"&&i+1<argc)serviceName=argv[++i];else if(arg==L"--once"){once=true;console=true;}else if(arg==L"--health"){health=true;console=true;}else if(arg==L"--console")console=true;}
 if(health){try{auto stamp=fs::last_write_time(home/L"services"/L"CloudLogging.heartbeat");return fs::file_time_type::clock::now()-stamp<std::chrono::seconds(45)?0:1;}catch(...){return 1;}}
 if(home.empty())return 2;stopped=CreateEventW(nullptr,TRUE,FALSE,nullptr);WSADATA data;WSAStartup(MAKEWORD(2,2),&data);curl_global_init(CURL_GLOBAL_DEFAULT);int result=0;
 if(console){try{result=run(once);}catch(const std::exception& e){log(e.what());result=1;}}else{SERVICE_TABLE_ENTRYW table[]={{serviceName.data(),serviceMain},{nullptr,nullptr}};if(!StartServiceCtrlDispatcherW(table))result=static_cast<int>(GetLastError());}
 curl_global_cleanup();WSACleanup();CloseHandle(stopped);return result;
}
