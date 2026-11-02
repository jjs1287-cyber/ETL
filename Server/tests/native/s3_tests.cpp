#include "../../src/native/s3/S3Client.h"
#include <boost/json.hpp>
#include <filesystem>
#include <fstream>
#include <iostream>
#include <functional>
namespace fs=std::filesystem;
using namespace etlforge::s3;
int checks=0;
void check(bool ok,const char* label){if(!ok)throw std::runtime_error(label);++checks;std::cout<<"PASS "<<label<<"\n";}
void rejects(std::function<void()> run,const char* label){bool failed=false;try{run();}catch(...){failed=true;}check(failed,label);}
std::string read(const fs::path& file){std::ifstream input(file,std::ios::binary);if(!input)throw std::runtime_error("Cannot read test fixture");return {std::istreambuf_iterator<char>(input),{}};}
void write(const fs::path& file,const std::string& data){std::ofstream out(file,std::ios::binary);out.write(data.data(),data.size());if(!out)throw std::runtime_error("Cannot write test fixture");}
int main(int argc,char** argv){try{
 check(encode("a b/+%?#&")=="a%20b%2F%2B%25%3F%23%26","RFC3986 query escaping");
 check(encode("folder/a b",true)=="folder/a%20b","object key slashes preserved");
 check(decode(encode(u8"café 世界😀+.xml"))==u8"café 世界😀+.xml","UTF-8 key roundtrip");
 check(decode("a+b%2Bc")=="a b+c","S3 listing form-encoded spaces and literal plus are distinct");
 rejects([]{decode("%xx");},"bad percent escape rejected");rejects([]{encode(std::string("\xc0\x80",2));},"malformed UTF-8 rejected");
 rejects([]{encode(std::string("\xed\xa0\x80",3));},"UTF-8 surrogate rejected");rejects([]{decode("%00");},"NUL key rejected");
 Configuration c;c.bucket="valid-bucket";c.region="us-east-1";c.accessKey="AKIA0000000000000000";c.secretKey="test";validate(c);check(true,"valid configuration");
 for(auto bad:{"http://other","a/b","../outside","127.0.0.1","a..b"}){auto copy=c;copy.bucket=bad;rejects([&]{validate(copy);},"invalid bucket rejected");}
 {auto copy=c;copy.region="us-east-1.evil.test";rejects([&]{validate(copy);},"region cannot inject a destination");}
 rejects([]{decryptProjectSecret("plaintext-key");},"plaintext credentials rejected");
 if(argc==1){std::cout<<checks<<" native checks passed\n";return 0;}
 if(argc!=4)throw std::runtime_error("Expected protected fixture, output directory, mode");
 auto config=boost::json::parse(read(fs::u8path(argv[1]))).as_object();auto value=[&](const char* n){return std::string(config.at(n).as_string());};
 c.bucket=value("bucket");c.region=value("region");c.accessKey=decryptProjectSecret(value("accessKeyProtected"));c.secretKey=decryptProjectSecret(value("secretKeyProtected"));
 auto p=config.if_contains("sessionTokenProtected");if(p)c.sessionToken=decryptProjectSecret(std::string(p->as_string()));Client client(c);
 auto prefix=value("prefix");if(prefix.rfind("etlforge-tests/",0)!=0||prefix.size()<35||prefix.back()!='/')throw std::runtime_error("Fixture prefix is not isolated");
 fs::path dir=fs::u8path(argv[2]);fs::create_directories(dir);std::string mode=argv[3];
 if(mode=="cleanup"){for(auto& o:client.list(prefix))client.remove(o.key);check(client.list(prefix).empty(),"isolated S3 fixture objects removed");}
 else if(mode=="verify"){
  client.download(prefix+"output/uploaded.bin",(dir/"uploaded.bin").u8string());check(read(dir/"uploaded.bin")==read(dir/"input.bin"),"operation target uploaded exact bytes");
  client.download(prefix+"output/combined.zip",(dir/"combined.zip").u8string());check(fs::file_size(dir/"combined.zip")>0,"compressed S3 operation output exists");
  check(client.list(prefix+"delete-after/").empty(),"successful operation deleted its source object");
  client.download(prefix+"xml/output.xml",(dir/"mapped-output.xml").u8string());auto mapped=read(dir/"mapped-output.xml");check(mapped.find(u8"café 世界😀")!=std::string::npos&&mapped.find('\0')==std::string::npos,"UTF-16 S3 XML transformed into UTF-8 S3 output");
 }else {
  const std::string payload=u8"ETLForge S3 UTF-8 — café 世界😀\r\n";std::string binary;for(int i=0;i<1048576;++i)binary+=static_cast<char>(i%256);
  write(dir/"input.bin",binary);write(dir/"unicode.txt",payload);client.upload(prefix+"input/payload.bin",(dir/"input.bin").u8string());
  client.upload(prefix+u8"input/nested/café 世界😀+%.txt",(dir/"unicode.txt").u8string());
  client.upload(prefix+"delete-after/payload.bin",(dir/"input.bin").u8string());
  std::u16string inputXml=u"<?xml version=\"1.0\" encoding=\"UTF-16\"?><rows><row><id>1</id><name>café 世界😀</name></row></rows>";std::string utf16="\xff\xfe";for(char16_t ch:inputXml){utf16+=static_cast<char>(ch&255);utf16+=static_cast<char>(ch>>8);}write(dir/"utf16-input.xml",utf16);client.upload(prefix+"xml/input.xml",(dir/"utf16-input.xml").u8string());
  check(client.list(prefix+"input/",true).size()==2,"recursive listing includes nested key");check(client.list(prefix+"input/",false).size()==1,"nonrecursive listing excludes nested key");
  auto paged=client.list(prefix+"input/",true,0,1);check(paged.size()==2,"continuation-token pagination preserves both objects");
  for(auto& object:paged){auto local=dir/"listed-object.bin";client.download(object.key,local.u8string(),object.etag);check(read(local)==(object.key.find("nested/")!=std::string::npos?payload:binary),"download exact key returned by paginated S3 listing");}
  auto list=client.list(prefix+"input/",true,1);check(list.size()==1,"listing limit honored");
  client.download(prefix+"input/payload.bin",(dir/"download.bin").u8string());check(read(dir/"download.bin")==binary,"1 MiB streaming binary byte equality");
  client.download(prefix+u8"input/nested/café 世界😀+%.txt",(dir/fs::u8path(u8"下载😀.txt")).u8string());check(read(dir/fs::u8path(u8"下载😀.txt"))==payload,"Unicode remote and local paths");
  rejects([&]{client.upload(prefix+"input/payload.bin",(dir/"unicode.txt").u8string(),false);},"overwrite disabled rejects existing object");
  client.download(prefix+"input/payload.bin",(dir/"unchanged.bin").u8string());check(read(dir/"unchanged.bin")==binary,"conditional upload preserves original");
  rejects([&]{client.download(prefix+"missing",(dir/"missing.bin").u8string());},"missing object reports failure");check(!fs::exists(dir/"missing.bin"),"failed download removes partial working file");
  rejects([&]{client.download(prefix+"input/payload.bin",(dir/"stale.bin").u8string(),"\"not-current\"");},"changed object ETag rejected");
  auto bad=c;bad.secretKey="incorrect-test-secret";rejects([&]{Client(bad).list(prefix);},"bad credentials rejected");
  auto cancelled=c;cancelled.cancelled=[] {return true;};rejects([&]{Client(cancelled).list(prefix);},"cancelled transfer rejected");
  client.upload(prefix+"delete-probe.txt",(dir/"unicode.txt").u8string());client.remove(prefix+"delete-probe.txt");check(client.list(prefix+"delete-probe.txt").empty(),"delete object");
 }
 std::cout<<checks<<" native S3 checks passed\n";return 0;
}catch(const std::exception& e){std::cerr<<"FAIL "<<e.what()<<"\n";return 1;}}
