#include "Utf8TextInput.h"
#include <windows.h>
#include <unicode/ucnv.h>
#include <unicode/ucnv_err.h>
#include <fstream>
#include <string>
#include <stdexcept>
#include <algorithm>
#include <cctype>

namespace etlforge {
void requireUtf8Text(const char* bytes,size_t size) {
    unsigned remaining=0,low=0x80,high=0xBF;
    for(size_t i=0;i<size;++i) {
        unsigned c=static_cast<unsigned char>(bytes[i]);
        if(remaining) {
            if(c<low || c>high)throw std::runtime_error("Text output contains invalid UTF-8.");
            --remaining;low=0x80;high=0xBF;
        } else if(c<=0x7F) {
            if(c==0)throw std::runtime_error("Text output contains NUL characters; use binary output for binary data.");
        } else if(c>=0xC2 && c<=0xDF)remaining=1;
        else if(c>=0xE0 && c<=0xEF){remaining=2;low=c==0xE0?0xA0:0x80;high=c==0xED?0x9F:0xBF;}
        else if(c>=0xF0 && c<=0xF4){remaining=3;low=c==0xF0?0x90:0x80;high=c==0xF4?0x8F:0xBF;}
        else throw std::runtime_error("Text output contains invalid UTF-8.");
    }
    if(remaining)throw std::runtime_error("Text output contains incomplete UTF-8.");
}
std::string xmlDeclaredEncoding(const std::string& text,size_t* valueStart,size_t* valueEnd) {
    if(text.compare(0,5,"<?xml")!=0)return "";
    size_t end=text.find("?>"),p=text.find("encoding");
    if(end==std::string::npos || p==std::string::npos || p>end)return "";
    p+=8;while(p<end && std::isspace(static_cast<unsigned char>(text[p])))++p;
    if(p>=end || text[p++]!='=')return "";
    while(p<end && std::isspace(static_cast<unsigned char>(text[p])))++p;
    if(p>=end || (text[p]!='\'' && text[p]!='"'))return "";
    char quote=text[p++];size_t last=text.find(quote,p);
    if(last==std::string::npos || last>end)return "";
    if(valueStart)*valueStart=p;if(valueEnd)*valueEnd=last;
    return text.substr(p,last-p);
}
void xmlDeclarationToUtf8(std::string& text) {
    size_t start=0,end=0;
    if(!xmlDeclaredEncoding(text,&start,&end).empty())text.replace(start,end-start,"UTF-8");
}
std::wstring textPathWide(const std::string& path) {
    int n=MultiByteToWideChar(CP_UTF8,MB_ERR_INVALID_CHARS,path.data(),static_cast<int>(path.size()),0,0);
    if(!n && !path.empty())throw std::runtime_error("Text file path is not valid UTF-8.");
    std::wstring result(n,0);
    if(n)MultiByteToWideChar(CP_UTF8,MB_ERR_INVALID_CHARS,path.data(),static_cast<int>(path.size()),&result[0],n);
    return result;
}
std::string textPathUtf8(const std::wstring& path) {
    int n=WideCharToMultiByte(CP_UTF8,WC_ERR_INVALID_CHARS,path.data(),static_cast<int>(path.size()),0,0,0,0);
    if(!n && !path.empty())throw std::runtime_error("Cannot encode text file path.");
    std::string result(n,0);
    if(n)WideCharToMultiByte(CP_UTF8,WC_ERR_INVALID_CHARS,path.data(),static_cast<int>(path.size()),&result[0],n,0,0);
    return result;
}
class Utf8TextInputImpl {
    std::string filename;
    std::wstring temporary;
    Utf8TextInputImpl(const Utf8TextInputImpl&);
    Utf8TextInputImpl& operator=(const Utf8TextInputImpl&);
    static bool validUtf8(std::ifstream& input) {
        unsigned remaining=0,low=0x80,high=0xBF;char buffer[65536];
        while(input.read(buffer,sizeof(buffer)) || input.gcount()) {
            for(std::streamsize i=0;i<input.gcount();++i) {
                unsigned c=static_cast<unsigned char>(buffer[i]);
                if(remaining) {
                    if(c<low || c>high)return false;
                    --remaining;low=0x80;high=0xBF;
                } else if(c<=0x7F) {
                    if(c==0)return false;
                } else if(c>=0xC2 && c<=0xDF)remaining=1;
                else if(c>=0xE0 && c<=0xEF) {remaining=2;low=c==0xE0?0xA0:0x80;high=c==0xED?0x9F:0xBF;}
                else if(c>=0xF0 && c<=0xF4) {remaining=3;low=c==0xF0?0x90:0x80;high=c==0xF4?0x8F:0xBF;}
                else return false;
            }
        }
        if(input.bad())throw std::runtime_error("Cannot read text source.");
        return remaining==0;
    }
    void convert(std::ifstream& input,const std::string& encoding,size_t skip) {
        UErrorCode status=U_ZERO_ERROR;
        UConverter* converter=ucnv_open(encoding.c_str(),&status);
        if(U_FAILURE(status))throw std::runtime_error("Unsupported source encoding: "+encoding);
        struct ConverterOwner {UConverter* p;~ConverterOwner(){ucnv_close(p);}} owner={converter};
        ucnv_setToUCallBack(converter,UCNV_TO_U_CALLBACK_STOP,0,0,0,&status);
        wchar_t tempDirectory[MAX_PATH+1],tempFile[MAX_PATH+1];
        DWORD n=GetTempPathW(MAX_PATH,tempDirectory);
        if(!n || n>=MAX_PATH || !GetTempFileNameW(tempDirectory,L"efu",0,tempFile))
            throw std::runtime_error("Cannot create UTF-8 source staging file.");
        temporary=tempFile;filename=textPathUtf8(temporary);
        std::ofstream output(temporary.c_str(),std::ios::binary|std::ios::trunc);
        if(!output)throw std::runtime_error("Cannot open UTF-8 source staging file.");
        input.clear();input.seekg(skip,std::ios::beg);
        if(ucnv_getType(converter)==UCNV_UTF8) {
            if(!validUtf8(input))throw std::runtime_error("Source contains invalid UTF-8.");
            input.clear();input.seekg(skip,std::ios::beg);
            char bytes[65536];
            while(input.read(bytes,sizeof(bytes)) || input.gcount())output.write(bytes,input.gcount());
            if(input.bad())throw std::runtime_error("Cannot read UTF-8 source.");
            output.close();if(!output)throw std::runtime_error("Cannot write UTF-8 source staging file.");
            return;
        }
        UConverter* utf8Converter=ucnv_open("UTF-8",&status);
        if(U_FAILURE(status))throw std::runtime_error("Cannot initialize UTF-8 output converter.");
        ConverterOwner utf8Owner={utf8Converter};
        ucnv_setFromUCallBack(utf8Converter,UCNV_FROM_U_CALLBACK_STOP,0,0,0,&status);
        char sourceBuffer[65536],utf8[65536];UChar pivot[32768];
        UChar* pivotSource=pivot;UChar* pivotTarget=pivot;
        bool finished=false,firstOutput=true;
        while(!finished) {
            input.read(sourceBuffer,sizeof(sourceBuffer));
            if(input.bad())throw std::runtime_error("Cannot read text source.");
            const char* source=sourceBuffer;const char* end=source+input.gcount();finished=input.eof();
            do {
                char* target=utf8;status=U_ZERO_ERROR;
                ucnv_convertEx(utf8Converter,converter,&target,utf8+sizeof(utf8),&source,end,
                    pivot,&pivotSource,&pivotTarget,pivot+32768,false,finished,&status);
                bool more=status==U_BUFFER_OVERFLOW_ERROR;
                if(U_FAILURE(status) && !more)throw std::runtime_error("Invalid text source for encoding "+encoding+": "+u_errorName(status));
                if(std::find(utf8,target,'\0')!=target)throw std::runtime_error("NUL characters are not supported in text sources; use binary file functions for binary data.");
                if(firstOutput && target!=utf8) {
                    std::string prefix(utf8,target);xmlDeclarationToUtf8(prefix);
                    output.write(prefix.data(),prefix.size());firstOutput=false;
                } else output.write(utf8,target-utf8);
                if(!more)break;
            } while(true);
        }
        output.close();if(!output)throw std::runtime_error("Cannot write UTF-8 source staging file.");
    }
public:
    static void validateFile(const std::string& path) {
        std::ifstream input(textPathWide(path).c_str(),std::ios::binary);
        if(!input)throw std::runtime_error("Cannot open UTF-8 target for validation.");
        if(!validUtf8(input))throw std::runtime_error("Text target contains invalid UTF-8.");
    }
    explicit Utf8TextInputImpl(const std::string& path,const std::string& configured="") : filename(path) {
        try {
            std::ifstream input(textPathWide(path).c_str(),std::ios::binary);
            if(!input)throw std::runtime_error("Cannot open text source.");
            unsigned char head[4]={};input.read(reinterpret_cast<char*>(head),4);size_t count=static_cast<size_t>(input.gcount());
            input.clear();input.seekg(0);
            std::string encoding=configured;size_t skip=0;
            if(count>=4 && head[0]==0xFF && head[1]==0xFE && head[2]==0 && head[3]==0){encoding="UTF-32LE";skip=4;}
            else if(count>=4 && head[0]==0 && head[1]==0 && head[2]==0xFE && head[3]==0xFF){encoding="UTF-32BE";skip=4;}
            else if(count>=2 && head[0]==0xFF && head[1]==0xFE){encoding="UTF-16LE";skip=2;}
            else if(count>=2 && head[0]==0xFE && head[1]==0xFF){encoding="UTF-16BE";skip=2;}
            else if(count>=3 && head[0]==0xEF && head[1]==0xBB && head[2]==0xBF){encoding="UTF-8";skip=3;}
            if(skip==0 && count==4) {
                if(head[0]==0 && head[1]=='<' && head[2]==0 && head[3]=='?')encoding="UTF-16BE";
                else if(head[0]=='<' && head[1]==0 && head[2]=='?' && head[3]==0)encoding="UTF-16LE";
                else if(head[0]==0 && head[1]==0 && head[2]==0 && head[3]=='<')encoding="UTF-32BE";
                else if(head[0]=='<' && head[1]==0 && head[2]==0 && head[3]==0)encoding="UTF-32LE";
                else {
                    char declaration[1024];input.read(declaration,sizeof(declaration));
                    const std::string declared=xmlDeclaredEncoding(std::string(declaration,static_cast<size_t>(input.gcount())));
                    if(!declared.empty())encoding=declared;
                    input.clear();input.seekg(0);
                }
            }
            if(encoding.empty()) {
                if(validUtf8(input))return;
                throw std::runtime_error("Source is not UTF-8 and has no Unicode BOM. Set etlforge.source.text.character_encoding to its actual encoding (for example windows-1252 or Shift_JIS).");
            }
            std::string normalized=encoding;std::transform(normalized.begin(),normalized.end(),normalized.begin(),[](unsigned char c){return static_cast<char>(std::toupper(c));});
            if((normalized=="UTF-8" || normalized=="UTF8") && !skip) {
                if(validUtf8(input))return;
                throw std::runtime_error("Source contains invalid UTF-8.");
            }
            convert(input,encoding,skip);
        } catch(...) {if(!temporary.empty())DeleteFileW(temporary.c_str());throw;}
    }
    ~Utf8TextInputImpl(){if(!temporary.empty())DeleteFileW(temporary.c_str());}
    const std::string& path()const{return filename;}
};
}

namespace etlforge {
void requireUtf8TextFile(const std::string& path){Utf8TextInputImpl::validateFile(path);}
Utf8TextInput::Utf8TextInput(const std::string& path,const std::string& configured) : implementation(new Utf8TextInputImpl(path,configured)) {}
Utf8TextInput::~Utf8TextInput(){delete implementation;}
const std::string& Utf8TextInput::path() const{return implementation->path();}
}
