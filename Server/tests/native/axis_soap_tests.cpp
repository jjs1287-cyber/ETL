#include <konga/JavaBackend/JsonEngine/JsonEngine.hpp>
#include <axis/AxisException.hpp>
#include <memory>
#include <string>
#include <iostream>
#include <stdexcept>
int main(int argc,char** argv){
 try{
  if(argc!=2)throw std::runtime_error("Expected loopback SOAP fixture endpoint");
  JsonEngine client(argv[1]);
  char user[]="fixture-user",password[]="fixture-md5";
  std::unique_ptr<WsStartNewJsonSessionResult> result(client.startNewJsonSession(user,password));
  if(!result || !result->getSessionId() || std::string(result->getSessionId())!=u8"ETLForge-session-64-\u65e5\u672c")throw std::runtime_error("SOAP session/UTF-8 response did not deserialize");
  if(!result->getError() || result->getError()->getErrorCode()!=0)throw std::runtime_error("SOAP error DTO did not deserialize");
  bool fault=false;
  try{std::unique_ptr<WsStartNewJsonSessionResult> rejected(client.startNewJsonSession(user,password));}catch(const axiscpp::AxisException&){fault=true;}
  if(!fault)throw std::runtime_error("SOAP Fault was not propagated");
  std::cout<<"PASS: real generated SOAP client, x64 Axis/Xerces/HTTP DLLs, UTF-8 DTOs and SOAP Fault propagation\n";
  return 0;
 }catch(const axiscpp::AxisException& e){std::cerr<<e.what()<<'\n';return 1;}
 catch(const std::exception& e){std::cerr<<e.what()<<'\n';return 1;}
}
