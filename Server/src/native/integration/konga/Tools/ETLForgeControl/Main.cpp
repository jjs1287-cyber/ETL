#include <konga/Utils/Settings.h>
#include <konga/Server/Server.h>
#include <konga/Networking/CommandUtils.h>
#include <konga/Networking/Request.h>
#include <konga/Networking/Response.h>
#include <konga/Networking/Command.h>
#include <konga/Networking/Result.h>
#include <iostream>
#include <memory>
#include <cstdlib>
int main(int argc,char** argv){
 try{
  if(argc<4){std::cerr<<"Usage: ETLForgeControl <component> <port> <command> [arguments...]\n";return 2;}
  char* end=nullptr;long port=std::strtol(argv[2],&end,10);
  if(!end || *end || port<1 || port>65535){std::cerr<<"Invalid command port\n";return 2;}
  konga::SetCommandLineOptions(argc,argv);
  std::string user,password;konga::GetInternalAdminCredentials(user,password);
  konga::networking::Request request(user,password);
  auto* command=new konga::networking::Command(argv[3]);
  for(int i=4;i<argc;++i)command->AddArgument(argv[i]);
  request.AddCommand(command);
  std::unique_ptr<konga::networking::Response> response(konga::networking::SendRequest(request,port,argv[1]));
  if(response->HasExceptionOccurred())throw response->GetException();
  for(auto result=response->res_begin();result!=response->res_end();++result)
   for(auto value=(*result)->ret_val_begin();value!=(*result)->ret_val_end();++value)std::cout<<*value<<'\n';
  return 0;
 }catch(const konga::CrException& error){std::cerr<<error.GetErrorString()<<'\n';return 1;}
 catch(const std::exception& error){std::cerr<<error.what()<<'\n';return 1;}
}
