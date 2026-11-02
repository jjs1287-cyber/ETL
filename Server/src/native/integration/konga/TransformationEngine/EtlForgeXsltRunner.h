#pragma once
#include <windows.h>
#include <objbase.h>
#include <string>
#include <vector>
#include <fstream>
#include <sstream>
#include <stdexcept>

namespace etlforge {
inline std::wstring xsltWide(const std::string& value) {
    int count = MultiByteToWideChar(CP_UTF8, MB_ERR_INVALID_CHARS, value.data(), static_cast<int>(value.size()), 0, 0);
    if (!count && !value.empty()) throw std::runtime_error("XSLT received invalid UTF-8.");
    std::wstring result(count, 0);
    if (count) MultiByteToWideChar(CP_UTF8, MB_ERR_INVALID_CHARS, value.data(), static_cast<int>(value.size()), &result[0], count);
    return result;
}
inline std::wstring xsltQuote(const std::wstring& value) {
    std::wstring result=L"\""; size_t slashes=0;
    for(wchar_t c:value){
        if(c==L'\\'){++slashes;continue;}
        result.append(slashes*(c==L'"'?2:1),L'\\'); slashes=0;
        if(c==L'"')result+=L'\\'; result+=c;
    }
    result.append(slashes*2,L'\\'); return result+L'"';
}
class XsltTemporaryFiles {
    std::wstring directory;
    std::vector<std::wstring> paths;
public:
    XsltTemporaryFiles(){
        wchar_t temp[MAX_PATH+1]; DWORD size=GetTempPathW(MAX_PATH,temp);
        if(!size || size>=MAX_PATH)throw std::runtime_error("Cannot locate XSLT temporary directory.");
        GUID id; if(FAILED(CoCreateGuid(&id)))throw std::runtime_error("Cannot allocate XSLT execution ID.");
        wchar_t text[40]; StringFromGUID2(id,text,40);
        directory=std::wstring(temp)+L"ETLForge-XSLT-"+text;
        if(!CreateDirectoryW(directory.c_str(),0))throw std::runtime_error("Cannot create XSLT temporary directory.");
    }
    ~XsltTemporaryFiles(){for(const auto& p:paths)DeleteFileW(p.c_str());RemoveDirectoryW(directory.c_str());}
    std::wstring path(const std::wstring& name){auto p=directory+L"\\"+name;paths.push_back(p);return p;}
    void write(const std::wstring& path,const std::string& value){
        std::ofstream stream(path.c_str(),std::ios::binary); stream.write(value.data(),value.size());
        if(!stream)throw std::runtime_error("Cannot write XSLT input.");
    }
    std::string read(const std::wstring& path){
        std::ifstream stream(path.c_str(),std::ios::binary);
        if(!stream)throw std::runtime_error("XSLT did not produce output.");
        std::ostringstream value;value<<stream.rdbuf();return value.str();
    }
};
inline std::string executeXslt(const std::string& java, const std::string& classpath,
    const std::string& stylesheet, const std::vector<std::string>& inputs,
    const std::string& nativeWorker="C:/ETL/Server/runtime/xslt-native/ETLForgeXsltWorker.exe", bool useJava=false, DWORD timeoutMs=120000) {
    if(inputs.empty())throw std::runtime_error("RunXSLT requires at least one input document.");
    XsltTemporaryFiles files;
    auto style=files.path(L"stylesheet.xsl");files.write(style,stylesheet);
    auto output=files.path(L"output.xml");auto error=files.path(L"error.txt");
    files.path(L"output.xml.catalog.xml");
    const std::string executablePath=useJava?java:nativeWorker;
    std::wstring command=xsltQuote(xsltWide(executablePath));
    if(useJava)command+=L" -Dfile.encoding=UTF-8 -Xmx256m -cp "+xsltQuote(xsltWide(classpath))+
        L" org.etlforge.integration.xslt.EtlForgeXsltRuntime";
    command+=L" "+xsltQuote(style);
    for(size_t i=0;i<inputs.size();++i){auto input=files.path(L"input"+std::to_wstring(i)+L".xml");files.write(input,inputs[i]);command+=L" "+xsltQuote(input);}
    command+=L" "+xsltQuote(output)+L" "+xsltQuote(error);
    std::vector<wchar_t> buffer(command.begin(),command.end());buffer.push_back(0);
    STARTUPINFOW start={};start.cb=sizeof(start);PROCESS_INFORMATION process={};
    auto executable=xsltWide(executablePath);
    if(!CreateProcessW(executable.c_str(),buffer.data(),0,0,FALSE,CREATE_NO_WINDOW|CREATE_SUSPENDED,0,0,&start,&process))
        throw std::runtime_error("Unable to start the ETLForge XSLT runtime.");
    HANDLE job=CreateJobObjectW(0,0);
    JOBOBJECT_EXTENDED_LIMIT_INFORMATION limits={};
    limits.BasicLimitInformation.LimitFlags=JOB_OBJECT_LIMIT_KILL_ON_JOB_CLOSE|JOB_OBJECT_LIMIT_PROCESS_MEMORY;
    limits.ProcessMemoryLimit=1024ULL*1024*1024;
    if(!job || !SetInformationJobObject(job,JobObjectExtendedLimitInformation,&limits,sizeof(limits)) || !AssignProcessToJobObject(job,process.hProcess)){
        TerminateProcess(process.hProcess,1);WaitForSingleObject(process.hProcess,10000);
        CloseHandle(process.hThread);CloseHandle(process.hProcess);if(job)CloseHandle(job);
        throw std::runtime_error("Cannot apply XSLT worker resource limits.");
    }
    if(ResumeThread(process.hThread)==static_cast<DWORD>(-1)){
        CloseHandle(job);CloseHandle(process.hThread);CloseHandle(process.hProcess);
        throw std::runtime_error("Cannot resume XSLT worker.");
    }
    CloseHandle(process.hThread);
    DWORD wait=WaitForSingleObject(process.hProcess,timeoutMs),code=1;
    if(wait!=WAIT_OBJECT_0){TerminateProcess(process.hProcess,1);WaitForSingleObject(process.hProcess,10000);CloseHandle(process.hProcess);CloseHandle(job);throw std::runtime_error("XSLT execution timed out or failed while waiting.");}
    GetExitCodeProcess(process.hProcess,&code);CloseHandle(process.hProcess);CloseHandle(job);
    if(code){std::string message;try{message=files.read(error);}catch(...){}throw std::runtime_error("XSLT transformation failed: "+message);}
    return files.read(output);
}
}
