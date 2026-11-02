#include <konga/Utils/Settings.h>
#include <konga/Utils/CrException.h>
#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/AutoDbCleanup.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/OperationLog/OperationInstance.h>
#include <konga/Messaging/OperationMessageUtils.h>
#include <iostream>
#include <cstdlib>
int main(int argc,char** argv) {
 try {
  konga::SetCommandLineOptions(argc,argv);
  if(argc!=2 && argc!=3) return 2;
  const long operationId=std::strtol(argv[1],0,10);
  if(operationId<1)return 2;
  if(argc==3) {
   std::cerr<<"Before full queue submission\n";
   const long long instance=konga::AddOperationToQueue(operationId,-1,-1,"ETLForge queue regression","Owned smoke operation",0);
   std::cout<<"PASS: native queue submission, instance "<<instance<<"\n";
   return instance>0?0:1;
  }
  konga::SmartDbPtr<konga::DbConnection> database(konga::GetBackEndDbConnection("TranDb"));
  konga::AutoRollbackTransaction transaction(&*database);
  std::cerr<<"Before operation instance insert\n";
  const long long instance=konga::NewOperationInstance(*database,operationId,"ETLForge queue regression",-1);
  std::cerr<<"After operation instance insert: "<<instance<<"\n";
  if(instance<1)return 1;
  std::cout<<"PASS: native operation instance creation (rolled back)\n";
  return 0;
 }catch(const konga::CrException& error){std::cerr<<error.GetErrorString()<<'\n';return 1;}
 catch(const std::exception& error){std::cerr<<error.what()<<'\n';return 1;}
}
