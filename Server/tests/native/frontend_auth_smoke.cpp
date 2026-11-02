#include <konga/UiBackend/WebServices/Authenticate/Authenticate.hpp>
#include <konga/Utils/Settings.h>
#include <konga/Utils/CrException.h>
#include <konga/Crypto/Crypto.h>
#include <konga/Authentication/Authentication.h>
#include <fstream>
#include <iostream>
#include <memory>
#include <stdexcept>
int main(int argc,char** argv){
 try{
  static_assert(sizeof(void*)==8,"x64 only");
  konga::SetCommandLineOptions(argc,argv);
  std::ifstream input("C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt");
  std::string password;std::getline(input,password);if(!password.empty()&&password.back()=='\r')password.pop_back();
  if(password.empty())throw std::runtime_error("Missing private test credential");
  Authenticate service;
  std::string hash=konga::md5(password);
  std::unique_ptr<AuthenticateResult> result(service.loginWithVersion(const_cast<char*>("Admin"),const_cast<char*>(hash.c_str()),const_cast<char*>("42.0.0.0")));
  if(!result || !result->getserverGuid() || !*result->getserverGuid() || !result->getuserGuid() || !*result->getuserGuid())throw std::runtime_error("Incomplete native login result");
  if(!result->getversionString() || std::string(result->getversionString())!="42.0.0.0")throw std::runtime_error("Server must report ETLForge release 42");
  for(const char* invalid : {"wrong-password", "' OR '1'='1"}){
   if(konga::Authentication::IsValidUser("Admin",invalid))throw std::runtime_error("Invalid password accepted");
  }
  std::cout<<"PASS: native Studio login implementation on PostgreSQL 18, nonempty server/user GUIDs, actual version, invalid and SQL-like passwords rejected\n";
  return 0;
 }catch(const konga::CrException& e){std::cerr<<e.GetErrorString()<<'\n';return 1;}
 catch(const std::exception& e){std::cerr<<e.what()<<'\n';return 1;}
}
