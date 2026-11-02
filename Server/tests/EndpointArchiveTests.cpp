#include "EndpointArchive.h"
#include <filesystem>
#include <fstream>
#include <iostream>
#include <functional>
#include <stdexcept>
#include <clocale>
namespace fs=std::filesystem;
static int checks=0;
void check(bool ok,const char* text){if(!ok)throw std::runtime_error(text);++checks;std::cout<<"PASS "<<text<<"\n";}
void reject(std::function<void()> f,const char* text){bool failed=false;try{f();}catch(const std::exception&){failed=true;}check(failed,text);}
std::string read(const std::string& p){std::ifstream f(fs::u8path(p),std::ios::binary);return std::string(std::istreambuf_iterator<char>(f),{});}
int main(int argc,char** argv){try{
 if(argc!=2)throw std::runtime_error("fixture directory required");auto root=fs::u8path(argv[1]);fs::create_directories(root);std::string content="caf\xc3\xa9 \xe4\xb8\x96\xe7\x95\x8c \xf0\x9f\x98\x80\r\n";content+=std::string("\0\xff\x01",3);
 auto file=(root/u8"input_\u4e16\u754c.bin").u8string();{std::ofstream f(fs::u8path(file),std::ios::binary);f.write(content.data(),content.size());}
 using namespace etlforge::compression;
 std::string initialLocale=setlocale(LC_CTYPE,nullptr);
 for(auto format:{"zip","tar.gz","gz","7z"}){
  auto archive=(root/(std::string("roundtrip")+extension(format))).u8string();pack({{file,u8"folder/\u4e16\u754c.bin"}},archive,format,"");
  auto files=extract(archive,(root/(std::string("extracted-")+format)).u8string(),"");check(std::string(format)=="gz"||files[0].name==u8"folder/\u4e16\u754c.bin","Unicode member name preserved");check(files.size()==1&&read(files[0].path)==content,(std::string(format)+" binary/Unicode round trip").c_str());
 }
 check(initialLocale==setlocale(LC_CTYPE,nullptr),"caller locale unchanged");
 auto rar=extract((root/"reference.rar").u8string(),(root/"rar-out").u8string(),"");check(rar.size()==1&&rar[0].name=="test.bin"&&read(rar[0].path).size()==1200,"RAR5 compressed input auto-detected");
 auto encrypted=(root/"encrypted.zip").u8string();pack({{file,"one.bin"},{file,"two.bin"}},encrypted,"zip","test-password");
 auto out=extract(encrypted,(root/"encrypted-good").u8string(),"test-password");check(out.size()==2&&read(out[1].path)==content,"AES256 encrypted ZIP multi-file round trip");
 reject([&]{extract(encrypted,(root/"byte-limit").u8string(),"test-password","",1,10000);},"extraction byte budget enforced");
 reject([&]{extract(encrypted,(root/"entry-limit").u8string(),"test-password","",1024*1024,1);},"extraction entry budget enforced");
 reject([&]{extract(encrypted,(root/"encrypted-bad").u8string(),"wrong");},"wrong password rejected");check(!fs::exists(root/"encrypted-bad"),"failed extraction leaves no files");
 reject([&]{extract(encrypted,(root/"encrypted-empty").u8string(),"");},"missing password rejected");
 reject([&]{pack({{file,"a"},{file,"b"}},(root/"bad.gz").u8string(),"gz","");},"combined GZIP rejected");
 reject([&]{pack({{file,"a"}},(root/"bad.7z").u8string(),"7z","secret");},"unsupported 7z encryption rejected");
 reject([&]{pack({{file,"../escape"}},(root/"bad.zip").u8string(),"zip","");},"unsafe output entry rejected");check(!fs::exists(root/"bad.zip"),"failed archive creation removed");
 reject([&]{pack({{file,"a"},{file,"A"}},(root/"duplicate.zip").u8string(),"zip","");},"case-insensitive duplicate rejected");
 reject([&]{extract(file,(root/"notarchive").u8string(),"");},"ordinary file rejected when compression enabled");
 for(auto fixture:{"traversal.zip","absolute.zip","symlink.zip","case.zip","reserved.zip","oversize.tar"}){
  reject([&]{extract((root/fixture).u8string(),(root/(std::string(fixture)+"-out")).u8string(),"");},fixture);
 }
 auto fixture=(root/"leading-dot.tar.gz").u8string();auto dot=extract(fixture,(root/"leading-dot-out").u8string(),"");check(dot.size()==1&&read(dot[0].path)=="hello","common ./ TAR paths accepted");
 std::cout<<"Passed "<<checks<<" native archive checks\n";return 0;
 }catch(const std::exception& e){std::cerr<<e.what()<<"\n";return 1;}}
