#define NOMINMAX
#include <windows.h>
#include <psapi.h>
#include "NativeJavaScript.h"
#include <chrono>
#include <iostream>
#include <vector>
#include <algorithm>
#include <stdexcept>
using namespace etlforge::javascript;
double cpu(){FILETIME a,b,k,u;GetProcessTimes(GetCurrentProcess(),&a,&b,&k,&u);ULARGE_INTEGER kernel,user;kernel.LowPart=k.dwLowDateTime;kernel.HighPart=k.dwHighDateTime;user.LowPart=u.dwLowDateTime;user.HighPart=u.dwHighDateTime;return (kernel.QuadPart+user.QuadPart)/10000.0;}
int main(){try{
 const std::string source="SetScriptResult(1+2*3);";
 std::cout<<"mode,repeat,iterations,wall_ms,cpu_ms\n";
 for(int mode=0;mode<2;mode++)for(int repeat=0;repeat<5;repeat++){
  auto start=std::chrono::steady_clock::now();double before=cpu();
  for(int row=0;row<1000;row++){
   if(mode==0)Invocation::validateSyntax(source+"// cold "+std::to_string(repeat*1000+row));
   else {Invocation instance;if(instance.evaluate(source).number!=7)throw std::runtime_error("Benchmark result differs");}
  }
  double elapsed=std::chrono::duration<double,std::milli>(std::chrono::steady_clock::now()-start).count();
  std::cout<<(mode==0?"cold_compile_fresh_realm":"cached_execute_fresh_realm")<<','<<repeat<<",1000,"<<elapsed<<','<<cpu()-before<<'\n';
 }
 PROCESS_MEMORY_COUNTERS memory{};memory.cb=sizeof(memory);if(!GetProcessMemoryInfo(GetCurrentProcess(),&memory,sizeof(memory)))throw std::runtime_error("Cannot query benchmark memory");
 std::cout<<"peak_working_set_bytes,"<<memory.PeakWorkingSetSize<<"\n";return 0;
 }catch(const std::exception& e){std::cerr<<e.what()<<'\n';return 1;}}
