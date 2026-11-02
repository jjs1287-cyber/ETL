#include <konga/UiBackend/WebServices/CheckoutInterchangeData/CheckoutInterchangeData.hpp>
#include <konga/Utils/Settings.h>
#include <konga/Utils/CrException.h>
#include <konga/Crypto/Crypto.h>
#include <fstream>
#include <iostream>
#include <memory>
#include <stdexcept>
using namespace checkout_interchange_data;
int main(int argc,char** argv){try{
 konga::SetCommandLineOptions(argc,argv);
 std::ifstream input("C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt");std::string password;std::getline(input,password);if(!password.empty()&&password.back()=='\r')password.pop_back();if(password.empty())throw std::runtime_error("No private credential");
 std::string hash=konga::md5(password);CheckoutInterchangeData service;
 std::cerr<<"Calling native operation tree"<<std::endl;
 std::unique_ptr<InterchangeDataHolder> result(service.getOperationTree(const_cast<char*>("Admin"),const_cast<char*>(hash.c_str())));
 if(!result)throw std::runtime_error("Null operation tree");
 std::cout<<"PASS: native checkout operation-tree implementation on PostgreSQL 18"<<std::endl;return 0;
 }catch(const konga::CrException& e){std::cerr<<e.GetErrorString()<<std::endl;return 1;}catch(const std::exception& e){std::cerr<<e.what()<<std::endl;return 1;}}
