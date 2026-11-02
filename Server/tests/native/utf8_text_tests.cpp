#include <konga/CharacterEncodingConversion/Utf8TextInput.h>
#include <windows.h>
#include <fstream>
#include <stdexcept>
#include <unicode/ucnv.h>
#include <konga/CharacterEncodingConversion/Utf8Conv.h>
#include <konga/CharacterEncodingConversion/EncodingStringUtils.h>
#include <konga/WinUtils/WinStringUtils.h>
#include <memory>
#include <unicode/ustring.h>
#include <iostream>
#include <iterator>
#include <vector>
#include <functional>

int main() {
    int checks=0;
    try {
        wchar_t directory[MAX_PATH],file[MAX_PATH];GetTempPathW(MAX_PATH,directory);GetTempFileNameW(directory,L"eft",0,file);
        struct Cleanup {std::wstring path;~Cleanup(){DeleteFileW(path.c_str());}} cleanup={file};
        const std::string path=etlforge::textPathUtf8(file);
        auto write=[&](const std::string& bytes){std::ofstream out(file,std::ios::binary|std::ios::trunc);out.write(bytes.data(),bytes.size());};
        auto read=[](const std::string& name){std::ifstream in(etlforge::textPathWide(name).c_str(),std::ios::binary);return std::string(std::istreambuf_iterator<char>(in),{});};
        auto check=[&](bool condition){if(!condition)throw std::runtime_error("Check "+std::to_string(checks+1)+" failed");++checks;};
        auto encode=[](const std::string& text,const std::string& encoding) {
            UErrorCode status=U_ZERO_ERROR;int32_t size=ucnv_convert(encoding.c_str(),"UTF-8",0,0,text.data(),static_cast<int32_t>(text.size()),&status);
            status=U_ZERO_ERROR;std::string result(size,0);ucnv_convert(encoding.c_str(),"UTF-8",&result[0],size,text.data(),static_cast<int32_t>(text.size()),&status);
            if(U_FAILURE(status))throw std::runtime_error(u_errorName(status));return result;
        };
        const std::string world="caf\xC3\xA9 \xE2\x82\xAC \xE4\xB8\xAD\xE6\x96\x87 \xD0\x9F\xD1\x80\xD0\xB8\xD0\xB2\xD0\xB5\xD1\x82 \xF0\x9F\x98\x80\r\n";
        write(world);{etlforge::Utf8TextInput text(path);check(text.path()==path);check(read(text.path())==world);}
        const std::vector<std::string> unicode={"UTF-8","UTF-16LE","UTF-16BE","UTF-32LE","UTF-32BE"};
        const std::vector<std::string> bom={std::string("\xEF\xBB\xBF",3),std::string("\xFF\xFE",2),std::string("\xFE\xFF",2),std::string("\xFF\xFE\0\0",4),std::string("\0\0\xFE\xFF",4)};
        for(size_t i=0;i<unicode.size();++i){std::string bytes=bom[i]+encode(world,unicode[i]);write(bytes);std::string staged;{etlforge::Utf8TextInput text(path);staged=text.path();check(read(staged)==world);check(read(path)==bytes);}check(GetFileAttributesW(etlforge::textPathWide(staged).c_str())==INVALID_FILE_ATTRIBUTES);}
        const std::vector<std::pair<std::string,std::string>> legacy={
            {"windows-1252","caf\xC3\xA9 \xE2\x82\xAC"},{"ISO-8859-1","caf\xC3\xA9"},
            {"Shift_JIS","\xE6\x97\xA5\xE6\x9C\xAC"},{"GB18030",world},{"windows-1251","\xD0\x9F\xD1\x80\xD0\xB8\xD0\xB2\xD0\xB5\xD1\x82"}};
        for(const auto& sample:legacy){write(encode(sample.second,sample.first));etlforge::Utf8TextInput text(path,sample.first);check(read(text.path())==sample.second);}
        for(const auto& encoding:std::vector<std::string>{"UTF-16LE","UTF-16BE","UTF-32LE","UTF-32BE"}){std::string xml="<?xml version='1.0' encoding='"+encoding+"'?><r>"+world+"</r>";write(encode(xml,encoding));etlforge::Utf8TextInput text(path);std::string expected=xml;etlforge::xmlDeclarationToUtf8(expected);check(read(text.path())==expected);}
        std::string xml="<?xml version='1.0' encoding='windows-1252'?><r>caf\xC3\xA9</r>";write(encode(xml,"windows-1252"));{etlforge::Utf8TextInput text(path);std::string result=read(text.path());std::string expected=xml;etlforge::xmlDeclarationToUtf8(expected);check(result==expected);etlforge::xmlDeclarationToUtf8(result);check(result.find("encoding='UTF-8'")!=std::string::npos);}
        std::string large;for(int i=0;i<50000;++i)large+=world;
        write(encode(large,"UTF-16LE"));{etlforge::Utf8TextInput text(path,"UTF-16LE");check(read(text.path())==large);}
        write(large);{etlforge::Utf8TextInput text(path);check(text.path()==path);check(read(text.path())==large);}
        auto rejected=[&](const std::string& bytes,const std::string& encoding){write(bytes);bool failed=false;try{etlforge::Utf8TextInput text(path,encoding);}catch(const std::exception&){failed=true;}check(failed);};
        rejected("\xC0\xAF","UTF-8");rejected("\xED\xA0\x80","UTF-8");rejected("\xF4\x90\x80\x80","UTF-8");rejected("\xF0\x9F","UTF-8");rejected("\xE9","");rejected(std::string("\xFF\xFE\0\xD8",4),"");rejected(std::string("a\0b",3),"UTF-8");rejected("abc","unknown-encoding-123");
        write("");{etlforge::Utf8TextInput text(path);check(text.path()==path);}
        std::string cells="A\xC3\xA9\xF0\x9F\x98\x80Z";
        check(konga::EncodingStringUtils::Utf8CharacterSize(cells)==4);
        check(konga::EncodingStringUtils::Utf8SubString(cells,1,1)=="\xC3\xA9");
        check(konga::EncodingStringUtils::Utf8SubString(cells,2,1)=="\xF0\x9F\x98\x80");
        check(konga::EncodingStringUtils::Utf8SubString(cells,3,100)=="Z");
        BSTR wide=0;check(konga::AnsiToBstr(world,wide)==0);char* roundtrip=0;
        check(konga::BstrToAnsi(wide,roundtrip)==0);check(std::string(roundtrip)==world);SysFreeString(wide);delete[] roundtrip;
        check(konga::AnsiToBstr("\xC0\xAF",wide)!=0);
        std::unique_ptr<konga::Utf8Conv> legacyConverter(konga::Utf8Conv::createEncoderFor("UTF-16LE"));
        std::string encoded=legacyConverter->to(world);check(encoded==encode(world,"UTF-16LE"));check(legacyConverter->from(encoded)==world);
        etlforge::requireUtf8Text(world.data(),world.size());++checks;
        for(const std::string bad:std::vector<std::string>{std::string("\xC0\xAF",2),std::string("\xF0\x9F",2),std::string("a\0b",3)}){bool failed=false;try{etlforge::requireUtf8Text(bad.data(),bad.size());}catch(const std::exception&){failed=true;}check(failed);}
        write(world);etlforge::requireUtf8TextFile(path);++checks;
        write(encode(world,"UTF-16LE"));{bool failed=false;try{etlforge::requireUtf8TextFile(path);}catch(const std::exception&){failed=true;}check(failed);}
        std::cout<<checks<<" UTF-8 source conversion checks passed.\n";return 0;
    } catch(const std::exception& failure){std::cerr<<failure.what()<<"\n";return 1;}
}
