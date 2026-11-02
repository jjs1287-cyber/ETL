#define WIN32_LEAN_AND_MEAN
#include <winsock2.h>
#include <windows.h>
#include <iphlpapi.h>
#include <string>
#include <vector>
#include <iostream>
#include <stdexcept>
#include <algorithm>

namespace {
struct Handle {
 HANDLE value=nullptr;
 explicit Handle(HANDLE input=nullptr):value(input){}
 ~Handle(){if(value && value!=INVALID_HANDLE_VALUE)CloseHandle(value);}
 Handle(const Handle&)=delete;Handle& operator=(const Handle&)=delete;
};
struct Failure {DWORD code; explicit Failure(DWORD input):code(input){}};
void require(bool condition){if(!condition)throw Failure(GetLastError());}
std::wstring serviceName,configuration;
SERVICE_STATUS_HANDLE statusHandle=nullptr;
SERVICE_STATUS status={};
HANDLE stopEvent=nullptr;
std::wstring setting(const wchar_t* key,const wchar_t* fallback=L""){
 wchar_t buffer[32768]={};
 DWORD length=GetPrivateProfileStringW(L"Service",key,fallback,buffer,32768,configuration.c_str());
 if(length>=32766)throw Failure(ERROR_INSUFFICIENT_BUFFER);
 return buffer;
}
DWORD number(const wchar_t* key,DWORD fallback,DWORD maximum){
 std::wstring text=setting(key);if(text.empty())return fallback;
 wchar_t* end=nullptr;unsigned long value=std::wcstoul(text.c_str(),&end,10);
 if(!end || *end || value>maximum)throw Failure(ERROR_INVALID_PARAMETER);
 return value;
}
void report(DWORD state,DWORD exitCode=0,DWORD waitHint=0){
 if(!statusHandle)return;
 status.dwServiceType=SERVICE_WIN32_OWN_PROCESS;
 status.dwCurrentState=state;
 status.dwControlsAccepted=state==SERVICE_RUNNING?(SERVICE_ACCEPT_STOP|SERVICE_ACCEPT_SHUTDOWN):0;
 status.dwWin32ExitCode=exitCode?ERROR_SERVICE_SPECIFIC_ERROR:NO_ERROR;
 status.dwServiceSpecificExitCode=exitCode;
 status.dwWaitHint=waitHint;
 status.dwCheckPoint=(state==SERVICE_START_PENDING || state==SERVICE_STOP_PENDING)?status.dwCheckPoint+1:0;
 SetServiceStatus(statusHandle,&status);
}
DWORD WINAPI control(DWORD code,DWORD,void*,void*){
 if(code==SERVICE_CONTROL_STOP || code==SERVICE_CONTROL_SHUTDOWN)SetEvent(stopEvent);
 return NO_ERROR;
}
BOOL WINAPI consoleControl(DWORD code){
 if(code==CTRL_C_EVENT || code==CTRL_BREAK_EVENT || code==CTRL_CLOSE_EVENT){SetEvent(stopEvent);return TRUE;}
 return FALSE;
}
HANDLE file(const std::wstring& path,bool input){
 SECURITY_ATTRIBUTES attributes={sizeof(attributes),nullptr,TRUE};
 const std::wstring selected=path.empty()?L"NUL":path;
 HANDLE result=CreateFileW(selected.c_str(),input?GENERIC_READ:FILE_APPEND_DATA,FILE_SHARE_READ|FILE_SHARE_WRITE,&attributes,
  path.empty()?OPEN_EXISTING:OPEN_ALWAYS,FILE_ATTRIBUTE_NORMAL,nullptr);
 require(result!=INVALID_HANDLE_VALUE);return result;
}
void launch(const std::wstring& executable,const std::wstring& arguments,bool suspended,PROCESS_INFORMATION& process){
 if(executable.empty())throw Failure(ERROR_INVALID_PARAMETER);
 Handle input(file(L"",true)),output(file(setting(L"Stdout"),false)),error(file(setting(L"Stderr"),false));
 STARTUPINFOEXW startup={};startup.StartupInfo.cb=sizeof(startup);
 startup.StartupInfo.dwFlags=STARTF_USESTDHANDLES;
 startup.StartupInfo.hStdInput=input.value;startup.StartupInfo.hStdOutput=output.value;startup.StartupInfo.hStdError=error.value;
 SIZE_T size=0;InitializeProcThreadAttributeList(nullptr,1,0,&size);
 std::vector<unsigned char> memory(size);
 startup.lpAttributeList=reinterpret_cast<LPPROC_THREAD_ATTRIBUTE_LIST>(memory.data());
 require(InitializeProcThreadAttributeList(startup.lpAttributeList,1,0,&size)!=FALSE);
 HANDLE inherited[]={input.value,output.value,error.value};
 if(!UpdateProcThreadAttribute(startup.lpAttributeList,0,PROC_THREAD_ATTRIBUTE_HANDLE_LIST,inherited,sizeof(inherited),nullptr,nullptr)){
  DWORD code=GetLastError();DeleteProcThreadAttributeList(startup.lpAttributeList);throw Failure(code);
 }
 std::wstring command=L"\""+executable+L"\" "+arguments;
 std::vector<wchar_t> writable(command.begin(),command.end());writable.push_back(0);
 const std::wstring directory=setting(L"WorkingDirectory");
 BOOL created=CreateProcessW(executable.c_str(),writable.data(),nullptr,nullptr,TRUE,
  CREATE_NO_WINDOW|EXTENDED_STARTUPINFO_PRESENT|(suspended?CREATE_SUSPENDED:0),nullptr,directory.empty()?nullptr:directory.c_str(),&startup.StartupInfo,&process);
 DWORD code=GetLastError();DeleteProcThreadAttributeList(startup.lpAttributeList);
 if(!created)throw Failure(code);
}
bool listening(DWORD processId,DWORD port){
 DWORD size=0;
 DWORD result=GetExtendedTcpTable(nullptr,&size,FALSE,AF_INET,TCP_TABLE_OWNER_PID_LISTENER,0);
 if(result!=ERROR_INSUFFICIENT_BUFFER)return false;
 std::vector<unsigned char> buffer(size);
 if(GetExtendedTcpTable(buffer.data(),&size,FALSE,AF_INET,TCP_TABLE_OWNER_PID_LISTENER,0)!=NO_ERROR)return false;
 auto* table=reinterpret_cast<MIB_TCPTABLE_OWNER_PID*>(buffer.data());
 for(DWORD i=0;i<table->dwNumEntries;++i){const auto& row=table->table[i];if(row.dwOwningPid==processId && ntohs(static_cast<u_short>(row.dwLocalPort))==port)return true;}
 return false;
}
DWORD run(){
 Handle event(CreateEventW(nullptr,TRUE,FALSE,nullptr));require(event.value!=nullptr);stopEvent=event.value;
 Handle job(CreateJobObjectW(nullptr,nullptr));require(job.value!=nullptr);
 JOBOBJECT_EXTENDED_LIMIT_INFORMATION limits={};limits.BasicLimitInformation.LimitFlags=JOB_OBJECT_LIMIT_KILL_ON_JOB_CLOSE;
 require(SetInformationJobObject(job.value,JobObjectExtendedLimitInformation,&limits,sizeof(limits))!=FALSE);
 const std::wstring instance=setting(L"Home");
 if(!instance.empty())require(SetEnvironmentVariableW(L"ETLFORGE_HOME",instance.c_str())!=FALSE);
 const std::wstring axis=setting(L"AxisHome");
 if(!axis.empty())require(SetEnvironmentVariableW(L"AXISCPP_DEPLOY",axis.c_str())!=FALSE);
 const std::wstring binaryPath=setting(L"BinaryDirectory");
 if(!binaryPath.empty()){
  DWORD size=GetEnvironmentVariableW(L"PATH",nullptr,0);std::vector<wchar_t> oldPath(size?size:1);
  if(size)GetEnvironmentVariableW(L"PATH",oldPath.data(),size);
  require(SetEnvironmentVariableW(L"PATH",(binaryPath+L";"+oldPath.data()).c_str())!=FALSE);
 }
 report(SERVICE_START_PENDING,0,30000);
 PROCESS_INFORMATION child={};launch(setting(L"Executable"),setting(L"Arguments"),true,child);
 Handle process(child.hProcess),thread(child.hThread);
 if(!AssignProcessToJobObject(job.value,process.value)){
  DWORD code=GetLastError();TerminateProcess(process.value,code);throw Failure(code);
 }
 require(ResumeThread(thread.value)!=static_cast<DWORD>(-1));
 DWORD readyPort=number(L"ReadyPort",0,65535),startupTimeout=number(L"StartupTimeoutSeconds",45,3600);
 ULONGLONG deadline=GetTickCount64()+static_cast<ULONGLONG>(startupTimeout)*1000;
 while(readyPort && !listening(child.dwProcessId,readyPort)){
  if(WaitForSingleObject(process.value,0)==WAIT_OBJECT_0){DWORD code=0;GetExitCodeProcess(process.value,&code);return code?code:ERROR_PROCESS_ABORTED;}
  if(GetTickCount64()>=deadline)throw Failure(ERROR_SERVICE_REQUEST_TIMEOUT);
  if(WaitForSingleObject(stopEvent,200)==WAIT_OBJECT_0)break;
  report(SERVICE_START_PENDING,0,30000);
 }
 report(SERVICE_RUNNING);
 HANDLE waits[]={process.value,stopEvent};DWORD reason=WaitForMultipleObjects(2,waits,FALSE,INFINITE);
 if(reason==WAIT_OBJECT_0){DWORD code=0;require(GetExitCodeProcess(process.value,&code)!=FALSE);return code;}
 require(reason==WAIT_OBJECT_0+1);
 DWORD timeout=number(L"StopTimeoutSeconds",30,3600)*1000;
 report(SERVICE_STOP_PENDING,0,timeout+5000);
 const std::wstring stopper=setting(L"StopExecutable");
 if(!stopper.empty()){
  try{
   PROCESS_INFORMATION stop={};launch(stopper,setting(L"StopArguments"),false,stop);
   Handle stopProcess(stop.hProcess),stopThread(stop.hThread);
   if(WaitForSingleObject(stopProcess.value,timeout)==WAIT_TIMEOUT)TerminateProcess(stopProcess.value,ERROR_TIMEOUT);
  }catch(const Failure&){                                                      }
 }
 if(WaitForSingleObject(process.value,timeout)==WAIT_TIMEOUT){
  require(TerminateJobObject(job.value,ERROR_TIMEOUT)!=FALSE);
  WaitForSingleObject(process.value,5000);
 }
 return 0;
}
void WINAPI serviceMain(DWORD,wchar_t**){
 statusHandle=RegisterServiceCtrlHandlerExW(serviceName.c_str(),control,nullptr);
 if(!statusHandle)return;
 DWORD code=0;try{code=run();}catch(const Failure& error){code=error.code;}catch(...){code=ERROR_UNHANDLED_EXCEPTION;}
 report(SERVICE_STOPPED,code);
}
}
int wmain(int argc,wchar_t** argv){
 SetErrorMode(SEM_FAILCRITICALERRORS|SEM_NOGPFAULTERRORBOX|SEM_NOOPENFILEERRORBOX);
 bool console=false;
 for(int i=1;i<argc;++i){std::wstring argument=argv[i];if(argument==L"--console")console=true;else if(argument==L"--name" && i+1<argc)serviceName=argv[++i];else if(argument==L"--config" && i+1<argc)configuration=argv[++i];else return ERROR_INVALID_PARAMETER;}
 if(configuration.empty() || (!console && serviceName.empty())){std::wcerr<<L"Usage: ETLForgeServiceHost --name <service> --config <absolute INI path> [--console]\n";return ERROR_INVALID_PARAMETER;}
 if(GetFileAttributesW(configuration.c_str())==INVALID_FILE_ATTRIBUTES)return ERROR_FILE_NOT_FOUND;
 if(console){SetConsoleCtrlHandler(consoleControl,TRUE);try{return static_cast<int>(run());}catch(const Failure& error){std::cerr<<"Service host error "<<error.code<<'\n';return static_cast<int>(error.code);}}
 SERVICE_TABLE_ENTRYW table[]={{&serviceName[0],serviceMain},{nullptr,nullptr}};
 if(!StartServiceCtrlDispatcherW(table))return static_cast<int>(GetLastError());
 return 0;
}
