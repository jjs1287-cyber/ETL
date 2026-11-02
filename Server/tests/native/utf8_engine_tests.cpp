#include <konga/TransformationEngine/crom.h>
#include <konga/TransformationEngine/TextDocument.h>
#include <konga/TransformationEngine/TextParser.h>
#include <konga/TransformationEngine/XmlReader.h>
#include <konga/CharacterEncodingConversion/Utf8TextInput.h>
#include <konga/Utils/CrException.h>
#include <windows.h>
#include <fstream>
#include <iostream>
#include <iterator>

int main() {
    int checks=0;
    try {
        wchar_t directory[MAX_PATH],name[MAX_PATH];GetTempPathW(MAX_PATH,directory);GetTempFileNameW(directory,L"efe",0,name);
        struct Cleanup {std::wstring path;~Cleanup(){DeleteFileW(path.c_str());}} cleanup={name};
        const std::string path=etlforge::textPathUtf8(name);
        auto check=[&](bool value){if(!value)throw std::runtime_error("Engine UTF8 check "+std::to_string(checks+1)+" failed");++checks;};
        const std::string emoji="\xF0\x9F\x98\x80",accent="\xC3\xA9";
        CROM record(0,0,0,"record",0,CROM_t_Collection,CROM::t_CROM_Text);record.set_delimiter(0);
        CROM* a=new CROM(0,&record,0,"text",0,CROM_t_Attribute,CROM::t_CROM_Text);a->SetValueType("String");a->SetFieldPosition(1);a->SetFieldLength(2);a->SetValue((accent+emoji+"Z").c_str());a->SetNullFlag(false);
        CROM* b=new CROM(0,&record,a,"number",0,CROM_t_Attribute,CROM::t_CROM_Text);b->SetValueType("int");b->SetFieldPosition(4);b->SetFieldLength(3);b->SetValue("42");b->SetNullFlag(false);
        FILE* output=_wfopen(name,L"wb");if(!output)throw std::runtime_error("Cannot open test output");
        try {TextDocument::writeCromElement(output,&record,false,false,0);}catch(...){fclose(output);throw;}fclose(output);
        std::ifstream in(name,std::ios::binary);std::string bytes((std::istreambuf_iterator<char>(in)),{});in.close();
        if(bytes!=accent+emoji+"  42\n"){std::cerr<<"Fixed output hex:";for(unsigned char c:bytes)std::cerr<<" "<<std::hex<<static_cast<unsigned>(c);std::cerr<<std::dec<<"\\n";}check(bytes==accent+emoji+"  42\n");etlforge::requireUtf8TextFile(path);++checks;
        {TextParser reader(path,0,0,false,false,2,1);reader.parse_all();check(reader.getRoot()->getChildren().size()==1);check(reader.getRoot()->getChildren()[0]->getName()==emoji);}
        std::wstring xml=L"<?xml version='1.0' encoding='UTF-16'?><r>\u00e9\xD83D\xDE00</r>";
        {std::ofstream file(name,std::ios::binary|std::ios::trunc);file.write("\xFF\xFE",2);file.write(reinterpret_cast<const char*>(xml.data()),xml.size()*2);}
        {konga::XmlReader reader(path);reader.parse_all();std::string text=reader.toString();check(text.find(accent+emoji)!=std::string::npos);check(text.find("encoding='UTF-8'")!=std::string::npos);}
        std::cout<<checks<<" engine UTF8 checks passed: fixed-width writer, numeric alignment, multibyte identifier, UTF16 XML reader.\n";return 0;
    } catch(const konga::CrException& failure){std::cerr<<failure.GetErrorString()<<"\n";return 1;}
      catch(const std::exception& failure){std::cerr<<failure.what()<<"\n";return 1;}
}
