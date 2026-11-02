#include "S3Client.h"
#include <curl/curl.h>
#include <openssl/evp.h>
#include <openssl/crypto.h>
#include <openssl/rand.h>
#include <boost/property_tree/ptree.hpp>
#include <boost/property_tree/xml_parser.hpp>
#include <algorithm>
#include <chrono>
#include <filesystem>
#include <fstream>
#include <memory>
#include <regex>
#include <set>
#include <sstream>
#include <stdexcept>
#include <thread>
#include <random>

namespace etlforge { namespace s3 {
namespace {
namespace fs=std::filesystem;
using Tree=boost::property_tree::ptree;
std::string text(const boost::json::object& o,const char* name,const char* fallback="") {
    auto p=o.if_contains(name);return p&&p->is_string()?std::string(p->as_string()):fallback;
}
void checkText(const std::string& value) {
    for(std::size_t i=0;i<value.size();) {
        unsigned char c=value[i++];if(c==0)throw std::runtime_error("S3 text contains NUL");
        if(c<128)continue;int n=c>=0xc2&&c<=0xdf?1:c>=0xe0&&c<=0xef?2:c>=0xf0&&c<=0xf4?3:-1;
        if(n<0||i+n>value.size())throw std::runtime_error("Invalid UTF-8 S3 text");
        unsigned char first=value[i];if((c==0xe0&&first<0xa0)||(c==0xed&&first>=0xa0)||(c==0xf0&&first<0x90)||(c==0xf4&&first>=0x90))throw std::runtime_error("Invalid UTF-8 S3 text");
        while(n--)if((static_cast<unsigned char>(value[i++])&0xc0)!=0x80)throw std::runtime_error("Invalid UTF-8 S3 text");
    }
}
Tree xml(const std::string& input) {
    if(input.find("<!DOCTYPE")!=std::string::npos||input.find("<!ENTITY")!=std::string::npos)throw std::runtime_error("S3 XML declarations are not allowed");
    checkText(input);Tree tree;std::istringstream stream(input);
    try {boost::property_tree::read_xml(stream,tree,boost::property_tree::xml_parser::no_comments);}
    catch(...) {throw std::runtime_error("Invalid S3 XML response");}return tree;
}
struct Transfer {
    std::string body;
    std::ifstream input;
    std::ofstream output;
    const Configuration* config=nullptr;
    bool writeFailure=false;
};
size_t receive(char* p,size_t size,size_t count,void* data) {
    auto& t=*static_cast<Transfer*>(data);auto n=size*count;
    if(t.output.is_open()){if(t.body.size()<4096)t.body.append(p,std::min<std::size_t>(n,4096-t.body.size()));t.output.write(p,n);return t.output?n:0;}
    if(n>4*1024*1024-t.body.size()){t.writeFailure=true;return 0;}t.body.append(p,n);return n;
}
size_t send(char* p,size_t size,size_t count,void* data) {
    auto& t=*static_cast<Transfer*>(data);t.input.read(p,size*count);
    return t.input.bad()?CURL_READFUNC_ABORT:static_cast<size_t>(t.input.gcount());
}
int progress(void* data,curl_off_t,curl_off_t,curl_off_t,curl_off_t) {
    auto& t=*static_cast<Transfer*>(data);
    try{return t.config->cancelled&&t.config->cancelled()?1:0;}catch(...){return 1;}
}
bool retryable(CURLcode code,long http) {
    return http==408||http==429||http==500||http==502||http==503||http==504||code==CURLE_COULDNT_CONNECT||code==CURLE_OPERATION_TIMEDOUT||code==CURLE_RECV_ERROR||code==CURLE_SEND_ERROR||code==CURLE_PARTIAL_FILE;
}
std::string errorCode(const std::string& response) {
    try {auto code=xml(response).get<std::string>("Error.Code","");if(std::regex_match(code,std::regex("[A-Za-z0-9]{1,80}")))return code;}catch(...){}return "RequestFailed";
}
void keyCheck(const std::string& key) {
    checkText(key);if(key.empty()||key.size()>1024)throw std::runtime_error("S3 object key must be 1 to 1024 UTF-8 bytes");
    std::istringstream parts(key);std::string part;while(std::getline(parts,part,'/'))if(part=="."||part=="..")throw std::runtime_error("S3 object keys containing standalone . or .. path segments are not supported");
}
}
std::string decryptProjectSecret(const std::string& encrypted) {
    if(encrypted.empty())return "";
    if(encrypted.size()>32768||encrypted.size()%4||encrypted.find_first_not_of("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=")!=std::string::npos)throw std::runtime_error("Invalid encrypted S3 credential");
    std::vector<unsigned char> bytes(encrypted.size());int size=EVP_DecodeBlock(bytes.data(),reinterpret_cast<const unsigned char*>(encrypted.data()),static_cast<int>(encrypted.size()));
    if(size<=0)throw std::runtime_error("Invalid encrypted S3 credential");if(encrypted.back()=='=')--size;if(encrypted.size()>1&&encrypted[encrypted.size()-2]=='=')--size;
    const std::string passphrase="ybd?)5!j}X3^Ki${>Dq}Sz$/Le50c[";
    const unsigned char salt[]={33,59,46,10,9,32,59,58};unsigned char key[EVP_MAX_KEY_LENGTH],iv[EVP_MAX_IV_LENGTH];
    EVP_BytesToKey(EVP_aes_128_cbc(),EVP_md5(),salt,reinterpret_cast<const unsigned char*>(passphrase.data()),static_cast<int>(passphrase.size()),1,key,iv);
    std::unique_ptr<EVP_CIPHER_CTX,decltype(&EVP_CIPHER_CTX_free)> ctx(EVP_CIPHER_CTX_new(),EVP_CIPHER_CTX_free);
    std::vector<unsigned char> clear(size+EVP_MAX_BLOCK_LENGTH);int count=0,last=0;
    bool ok=ctx&&EVP_DecryptInit_ex(ctx.get(),EVP_aes_128_cbc(),nullptr,key,iv)==1&&EVP_DecryptUpdate(ctx.get(),clear.data(),&count,bytes.data(),size)==1&&EVP_DecryptFinal_ex(ctx.get(),clear.data()+count,&last)==1;
    OPENSSL_cleanse(key,sizeof(key));OPENSSL_cleanse(iv,sizeof(iv));
    if(!ok){OPENSSL_cleanse(clear.data(),clear.size());throw std::runtime_error("Cannot decrypt S3 credential; re-enter it in the endpoint");}
    std::string result(reinterpret_cast<const char*>(clear.data()),count+last);OPENSSL_cleanse(clear.data(),clear.size());return result;
}
std::string encode(const std::string& text,bool slash) {
    checkText(text);static const char digits[]="0123456789ABCDEF";std::string out;
    for(unsigned char c:text){if((c>='a'&&c<='z')||(c>='A'&&c<='Z')||(c>='0'&&c<='9')||c=='-'||c=='_'||c=='.'||c=='~'||(slash&&c=='/'))out+=c;else {out+='%';out+=digits[c>>4];out+=digits[c&15];}}return out;
}
std::string decode(const std::string& text) {
    std::string out;auto digit=[](char c){return c>='0'&&c<='9'?c-'0':c>='a'&&c<='f'?c-'a'+10:c>='A'&&c<='F'?c-'A'+10:-1;};
    for(std::size_t i=0;i<text.size();++i){if(text[i]=='+')out+=' ';else if(text[i]!='%')out+=text[i];else {if(i+2>=text.size()||digit(text[i+1])<0||digit(text[i+2])<0)throw std::runtime_error("Invalid S3 key encoding");out+=static_cast<char>(digit(text[i+1])*16+digit(text[i+2]));i+=2;}}checkText(out);return out;
}
void validate(const Configuration& c) {
    if(c.bucket.size()<3||c.bucket.size()>63||!std::regex_match(c.bucket,std::regex("[a-z0-9][a-z0-9.-]*[a-z0-9]"))||c.bucket.find("..")!=std::string::npos||std::regex_match(c.bucket,std::regex("[0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+")))throw std::runtime_error("Enter a general-purpose S3 bucket name, not a URL");
    if(!std::regex_match(c.region,std::regex("[a-z]{2}(-[a-z]+)+-[0-9]+")))throw std::runtime_error("Enter an AWS region such as us-east-1");
    if(!std::regex_match(c.accessKey,std::regex("[A-Z0-9]{16,128}"))||c.secretKey.empty()||c.secretKey.size()>256||c.secretKey.find_first_of("\r\n\0",0,3)!=std::string::npos)throw std::runtime_error("Enter valid S3 access-key credentials");
    if(c.sessionToken.size()>16384||c.sessionToken.find_first_of("\r\n\0",0,3)!=std::string::npos)throw std::runtime_error("Invalid S3 session token");
}
Client::Client(Configuration c):config_(std::move(c)){validate(config_);static const auto initialized=curl_global_init(CURL_GLOBAL_DEFAULT);if(initialized!=CURLE_OK)throw std::runtime_error("Cannot initialize S3 transport");}
Client::~Client(){if(!config_.secretKey.empty())OPENSSL_cleanse(&config_.secretKey[0],config_.secretKey.size());if(!config_.sessionToken.empty())OPENSSL_cleanse(&config_.sessionToken[0],config_.sessionToken.size());}
std::string Client::request(const std::string& method,const std::string& key,const std::string& query,const std::string& file,const std::vector<std::string>& extra) const {
    std::string suffix=config_.region.rfind("cn-",0)==0?"amazonaws.com.cn":"amazonaws.com";
    std::string origin=config_.bucket.find('.')==std::string::npos?config_.bucket+".s3."+config_.region+"."+suffix:"s3."+config_.region+"."+suffix;
    std::string path=(config_.bucket.find('.')==std::string::npos?"/":"/"+config_.bucket+"/")+encode(key,true);
    std::string url="https://"+origin+path+(query.empty()?"":"?"+query);
    std::string signing="aws:amz:"+config_.region+":s3";
    for(int attempt=0;attempt<3;++attempt) {
        if(config_.cancelled&&config_.cancelled())throw std::runtime_error("S3 transfer cancelled");
        Transfer t;t.config=&config_;curl_off_t length=0;
        if(method=="PUT"){t.input.open(fs::u8path(file),std::ios::binary);if(!t.input)throw std::runtime_error("Cannot open S3 upload file");length=fs::file_size(fs::u8path(file));}
        if(method=="GET"&&!file.empty()){t.output.open(fs::u8path(file),std::ios::binary|std::ios::trunc);if(!t.output)throw std::runtime_error("Cannot open S3 download working file");}
        std::unique_ptr<CURL,decltype(&curl_easy_cleanup)> curl(curl_easy_init(),curl_easy_cleanup);if(!curl)throw std::runtime_error("Cannot initialize S3 request");
        curl_slist* raw=nullptr;
        auto add=[&](const std::string& h){auto next=curl_slist_append(raw,h.c_str());if(!next){curl_slist_free_all(raw);raw=nullptr;throw std::bad_alloc();}raw=next;};
        add("x-amz-content-sha256: UNSIGNED-PAYLOAD");add("Expect:");if(!config_.sessionToken.empty())add("x-amz-security-token: "+config_.sessionToken);for(auto& h:extra)add(h);
        std::unique_ptr<curl_slist,decltype(&curl_slist_free_all)> headers(raw,curl_slist_free_all);
        char diagnostic[CURL_ERROR_SIZE]={0};curl_easy_setopt(curl.get(),CURLOPT_ERRORBUFFER,diagnostic);
        curl_easy_setopt(curl.get(),CURLOPT_URL,url.c_str());
        curl_easy_setopt(curl.get(),CURLOPT_AWS_SIGV4,signing.c_str());curl_easy_setopt(curl.get(),CURLOPT_USERNAME,config_.accessKey.c_str());curl_easy_setopt(curl.get(),CURLOPT_PASSWORD,config_.secretKey.c_str());
        curl_easy_setopt(curl.get(),CURLOPT_HTTPHEADER,headers.get());curl_easy_setopt(curl.get(),CURLOPT_CUSTOMREQUEST,method.c_str());
        curl_easy_setopt(curl.get(),CURLOPT_FOLLOWLOCATION,0L);curl_easy_setopt(curl.get(),CURLOPT_PROTOCOLS_STR,"https");curl_easy_setopt(curl.get(),CURLOPT_SSL_VERIFYPEER,1L);curl_easy_setopt(curl.get(),CURLOPT_SSL_VERIFYHOST,2L);
        curl_easy_setopt(curl.get(),CURLOPT_SSL_OPTIONS,static_cast<long>(CURLSSLOPT_NATIVE_CA));
        curl_easy_setopt(curl.get(),CURLOPT_CONNECTTIMEOUT,15L);curl_easy_setopt(curl.get(),CURLOPT_TIMEOUT,3600L);curl_easy_setopt(curl.get(),CURLOPT_LOW_SPEED_LIMIT,1L);curl_easy_setopt(curl.get(),CURLOPT_LOW_SPEED_TIME,60L);curl_easy_setopt(curl.get(),CURLOPT_NOSIGNAL,1L);
        curl_easy_setopt(curl.get(),CURLOPT_WRITEFUNCTION,receive);curl_easy_setopt(curl.get(),CURLOPT_WRITEDATA,&t);
        curl_easy_setopt(curl.get(),CURLOPT_XFERINFOFUNCTION,progress);curl_easy_setopt(curl.get(),CURLOPT_XFERINFODATA,&t);curl_easy_setopt(curl.get(),CURLOPT_NOPROGRESS,0L);
        if(method=="PUT"){curl_easy_setopt(curl.get(),CURLOPT_UPLOAD,1L);curl_easy_setopt(curl.get(),CURLOPT_READFUNCTION,send);curl_easy_setopt(curl.get(),CURLOPT_READDATA,&t);curl_easy_setopt(curl.get(),CURLOPT_INFILESIZE_LARGE,length);}
        CURLcode result=curl_easy_perform(curl.get());long http=0;curl_easy_getinfo(curl.get(),CURLINFO_RESPONSE_CODE,&http);
        bool fileOk=true;if(t.output.is_open()){t.output.close();fileOk=bool(t.output);}
        if(result==CURLE_OK&&http>=200&&http<300&&fileOk)return t.body;
        if(!file.empty()&&method=="GET"){std::error_code ignored;fs::remove(fs::u8path(file),ignored);}
        if(result==CURLE_ABORTED_BY_CALLBACK)throw std::runtime_error("S3 transfer cancelled");
        if(!fileOk||t.writeFailure)throw std::runtime_error("S3 response exceeds its limit or cannot be written");
        if(attempt<2&&retryable(result,http)) {for(int i=0;i<(1<<attempt)*10;++i){if(config_.cancelled&&config_.cancelled())throw std::runtime_error("S3 transfer cancelled");std::this_thread::sleep_for(std::chrono::milliseconds(100));}continue;}
        std::string detail=diagnostic;
        for(const auto* secret:{&config_.accessKey,&config_.secretKey,&config_.sessionToken})if(!secret->empty()){std::size_t pos=0;while((pos=detail.find(*secret,pos))!=std::string::npos){detail.replace(pos,secret->size(),"[REDACTED]");pos+=10;}}
        throw std::runtime_error("S3 "+method+" failed: HTTP "+std::to_string(http)+" "+(result==CURLE_OK?errorCode(t.body):curl_easy_strerror(result))+(detail.empty()?"":" ("+detail+")")+". Check bucket, region and permissions.");
    }throw std::runtime_error("S3 request failed");
}
std::vector<Object> Client::list(const std::string& prefix,bool recursive,std::size_t limit,std::size_t pageSize) const {
    if(pageSize<1||pageSize>1000)throw std::runtime_error("Invalid S3 listing page size");
    checkText(prefix);std::vector<Object> out;std::string token;std::set<std::string> seen;
    do {
        std::string q="encoding-type=url&list-type=2&max-keys="+std::to_string(limit?std::min<std::size_t>(pageSize,limit-out.size()):pageSize)+"&prefix="+encode(prefix);
        if(!recursive)q+="&delimiter=%2F";if(!token.empty())q+="&continuation-token="+encode(token);
        auto root=xml(request("GET","",q)).get_child("ListBucketResult");
        for(auto& node:root)if(node.first=="Contents") {auto& t=node.second;Object o;o.key=decode(t.get<std::string>("Key"));o.size=t.get<std::uint64_t>("Size");o.etag=t.get<std::string>("ETag","");o.modified=t.get<std::string>("LastModified","");
            if(o.key.compare(0,prefix.size(),prefix)!=0)throw std::runtime_error("S3 returned an object outside the requested prefix");
            if(!o.key.empty()&&o.key.back()!='/')out.push_back(o);if(out.size()>100000)throw std::runtime_error("S3 source matches more than 100000 objects; narrow the folder/prefix");if(limit&&out.size()>=limit)return out;}
        if(!root.get<bool>("IsTruncated",false))break;
        token=root.get<std::string>("NextContinuationToken","");if(token.empty()||!seen.insert(token).second)throw std::runtime_error("S3 returned an invalid continuation token");
    }while(true);return out;
}
void Client::download(const std::string& key,const std::string& file,const std::string& etag) const {keyCheck(key);std::vector<std::string> headers;if(!etag.empty())headers.push_back("If-Match: "+etag);request("GET",key,"",file,headers);}
void Client::upload(const std::string& key,const std::string& file,bool overwrite) const {keyCheck(key);if(fs::file_size(fs::u8path(file))>5ULL*1024*1024*1024)throw std::runtime_error("S3 single-object upload supports up to 5 GiB; multipart upload is not yet enabled");request("PUT",key,"",file,overwrite?std::vector<std::string>{}:std::vector<std::string>{"If-None-Match: *"});}
void Client::remove(const std::string& key,const std::string& etag) const {keyCheck(key);std::vector<std::string> headers;if(!etag.empty())headers.push_back("If-Match: "+etag);request("DELETE",key,"","",headers);}
std::string request(const std::string& json) {
    auto o=boost::json::parse(json).as_object();if(text(o,"action")!="s3.test")throw std::runtime_error("Unknown S3 action");
    Configuration c;c.bucket=text(o,"bucket");c.region=text(o,"region");c.accessKey=decryptProjectSecret(text(o,"accessKeyProtected"));c.secretKey=decryptProjectSecret(text(o,"secretKeyProtected"));
    if(text(o,"authentication")=="temporary"){c.sessionToken=decryptProjectSecret(text(o,"sessionTokenProtected"));if(c.sessionToken.empty())throw std::runtime_error("Temporary S3 credentials require a session token");}
    Client client(std::move(c));boost::json::array files;for(auto& f:client.list(text(o,"prefix"),false,100))files.push_back(boost::json::value(f.key));
    if(text(o,"direction")=="target"){
        unsigned char random[16];if(RAND_bytes(random,sizeof(random))!=1)throw std::runtime_error("Cannot create an S3 connection-test ID");
        std::string id;const char digits[]="0123456789abcdef";for(auto b:random){id+=digits[b>>4];id+=digits[b&15];}
        auto directory=fs::temp_directory_path()/fs::u8path("etlforge-s3-test-"+id);
        if(!fs::create_directory(directory))throw std::runtime_error("Cannot create an S3 connection-test working folder");
        struct Cleanup{fs::path directory;~Cleanup(){std::error_code ignored;fs::remove_all(directory,ignored);}} cleanup{directory};
        auto local=directory/"probe.txt";{std::ofstream output(local,std::ios::binary);output<<"ETLForge S3 connection test\n";output.close();if(!output)throw std::runtime_error("Cannot write S3 connection-test working file");}
        std::string key=text(o,"prefix")+".etlforge-connection-test-"+id+".txt";
        try{client.upload(key,local.u8string(),false);}catch(...){try{client.remove(key);}catch(...){}throw;}
        client.remove(key);
    }
    return boost::json::serialize(boost::json::object{{"files",files},{"message",text(o,"direction")=="target"?"S3 listing, upload and test-object deletion succeeded.":"S3 bucket listing succeeded."}});
}
}}

