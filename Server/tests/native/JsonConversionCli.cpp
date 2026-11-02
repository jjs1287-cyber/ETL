#include <konga/NativeJson/EtlForgeJsonConversion.h>
#include <iostream>
#include <chrono>
#include <vector>
#include <algorithm>
#include <windows.h>
#include <psapi.h>
#include <filesystem>
#include <fstream>
#include <sstream>

int wmain(int argc, wchar_t** argv) {
    if(argc==3 && std::wstring(argv[1])==L"contracts"){
      try {
        namespace fs=std::filesystem;fs::path root(argv[2]);fs::create_directories(root/L"日本");
        auto write=[](const fs::path& p,const std::string& s){std::ofstream f(p,std::ios::binary);f<<s;if(!f)throw std::runtime_error("Fixture write failed");};
        auto read=[](const fs::path& p){std::ifstream f(p,std::ios::binary);std::ostringstream s;s<<f.rdbuf();return s.str();};
        int checks=0;auto require=[&](bool ok){if(!ok)throw std::runtime_error("JSON runtime contract failed");++checks;};
        auto schema=root/L"日本/schema.xsd",input=root/L"input.json",output=root/L"output.xml";
        write(schema,"<xs:schema xmlns:xs='http://www.w3.org/2001/XMLSchema'><xs:element name='json'><xs:complexType><xs:sequence><xs:element name='name' type='xs:string'/></xs:sequence></xs:complexType></xs:element></xs:schema>");
        require(etlforge::resolveJsonSchemaPath(root.wstring(),L"日本/schema.xsd")==fs::canonical(schema).wstring());
        for(const auto& relative:{schema.wstring(),std::wstring(L"../outside.xsd"),std::wstring(L"missing.xsd"),std::wstring(L"日本")}){bool rejected=false;try{etlforge::resolveJsonSchemaPath(root.wstring(),relative);}catch(const std::exception&){rejected=true;}require(rejected);}
        write(input,u8"{\"name\":\"café 日本 😀\"}");etlforge::convertJsonAtomically(input.wstring(),schema.wstring(),output.wstring(),true);require(read(output).find(u8"café 日本 😀")!=std::string::npos);
        const std::string original=read(output);
        for(const std::string& bad:{std::string("{broken"),std::string("{\"name\":\"\\u0000\"}"),std::string("{\"name\":\"\\uFFFE\"}"),std::string("{\"name\":\"")+char(0xff)+"\"}"}){
            write(input,bad);bool rejected=false;try{etlforge::convertJsonAtomically(input.wstring(),schema.wstring(),output.wstring(),true);}catch(const std::exception&){rejected=true;}require(rejected);require(read(output)==original);require(read(input)==bad);
        }
        write(input,"<!DOCTYPE x [<!ENTITY e SYSTEM 'file:///private'>]><x>&e;</x>");bool rejected=false;try{etlforge::convertJsonAtomically(input.wstring(),schema.wstring(),output.wstring(),false);}catch(const std::exception&){rejected=true;}require(rejected);require(read(output)==original);
        for(const auto& entry:fs::directory_iterator(root))require(entry.path().extension()!=L".part");
        std::cout<<"PASS: "<<checks<<" native JSON path, Unicode, malformed input, atomic output and cleanup checks\n";return 0;
      }catch(const std::exception& error){std::cerr<<error.what()<<'\n';return 1;}
    }
    if (argc != 5) { std::cerr << "Usage: JsonConversionCli json-to-xml|xml-to-json|bench-json-to-xml|bench-xml-to-json input xsd output\n"; return 2; }
    try {
        if(std::wstring(argv[1]).find(L"bench-")==0){
            bool source=std::wstring(argv[1])==L"bench-json-to-xml";
            auto convert=[&]{etlforge::convertJsonAtomically(argv[2],argv[3],argv[4],source);};
            convert();std::vector<double> samples;
            for(int i=0;i<7;i++){auto start=std::chrono::steady_clock::now();convert();samples.push_back(std::chrono::duration<double,std::milli>(std::chrono::steady_clock::now()-start).count());}
            std::sort(samples.begin(),samples.end());PROCESS_MEMORY_COUNTERS counters={};GetProcessMemoryInfo(GetCurrentProcess(),&counters,sizeof(counters));
            std::cout<<samples[3]<<","<<counters.PeakWorkingSetSize<<"\n";return 0;
        }
        if (std::wstring(argv[1]) == L"json-to-xml") etlforge::convertJsonFileToXml(argv[2], argv[3], argv[4]);
        else if (std::wstring(argv[1]) == L"xml-to-json") etlforge::convertXmlFileToJson(argv[2], argv[3], argv[4]);
        else return 2;
        return 0;
    } catch (const std::exception& error) { std::cerr << error.what() << '\n'; return 1; }
    catch (...) { std::cerr << "XML conversion failed\n"; return 1; }
}
