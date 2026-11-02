#include <konga/Utils/Settings.h>
#include <konga/Utils/CrException.h>
#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/AutoDbCleanup.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>
#include <konga/OperationContext/DefaultOperationDataElementHolder.h>
#include <konga/DataElement/DataElement.h>
#include <xercesc/util/PlatformUtils.hpp>
#include <iostream>
namespace konga {std::string SalesforceStudioCredential(const std::string&,bool);void PrepareSalesforceOperationSession(long,DbConnection&,OperationDataElementHolder&,const UserInfo&);}
int main(int argc,char** argv){
 try{
  static_assert(sizeof(void*)==8,"x64 only");
  
  konga::SetCommandLineOptions(argc,argv);
  konga::SmartDbPtr<konga::DbConnection> database(konga::GetBackEndDbConnection("TranDb"));
  konga::AutoRollbackTransaction transaction(&*database);
  database->ExecuteSql("INSERT INTO salesforcequeriestab (salesforcequery_id,salesforcequery_name,operation_id) VALUES (42424242,'ETLForge rolled-back OAuth test',42424242)");
  database->ExecuteSql("INSERT INTO SalesforceQueryPropertiesTab (salesforcequery_id,keyname,keyvalue) VALUES (42424242,'connector_guid','42420000-0000-4000-8000-000000000002')");
  konga::UserInfo admin=konga::GetAdminUserInfo(*database);
  konga::DefaultOperationDataElementHolder variables(admin);
  konga::SmartDbPtr<konga::DynamicDbRecordSet> settings(konga::DynamicDbRecordSet::MakeInstance(&*database,"SELECT instance_url,client_id,client_secret FROM SalesforceConnectorsTab WHERE salesforceconnector_id=(SELECT item_id FROM GuidsForSalesforceConnectorsTab WHERE guid='42420000-0000-4000-8000-000000000002')"));settings->Open();if(!settings->Next())throw konga::CrException("Missing isolated test connector.");
  const char* keys[]={"OAuthTestUrl","OAuthTestClientId","OAuthTestSecret"};
  for(unsigned i=0;i<3;i++){std::string value=settings->GetFieldValue(i);if(i==2)value=konga::SalesforceStudioCredential(value,false);konga::DataElement* variable=variables.getOperationDataElement(keys[i]);variable->setType(konga::DataElement::t_string);variable->setValue(value.c_str());}
  settings.CloseAndDelete();
  std::string encrypted=konga::SalesforceStudioCredential("[OAuthTestSecret]",true);
  database->ExecuteSql("UPDATE SalesforceConnectorsTab SET instance_url='[OAuthTestUrl]',client_id='[OAuthTestClientId]',client_secret='"+encrypted+"' WHERE salesforceconnector_id=(SELECT item_id FROM GuidsForSalesforceConnectorsTab WHERE guid='42420000-0000-4000-8000-000000000002')");
  konga::PrepareSalesforceOperationSession(42424242,*database,variables,admin);
  int error=0;
  std::string token=(const char*)variables.getOperationDataElement("__Salesforce.SessionID.42420000-0000-4000-8000-000000000002")->GetString(error);
  if(error||token.empty())throw konga::CrException("Native OAuth session variable was not initialized.");
  std::cout<<"PASS: x64 native operation session bridge -> Java backend -> Salesforce OAuth; runtime variables resolved including encrypted secret; fixture rows rolled back\n";
  return 0;
 }catch(const konga::CrException&){std::cerr<<"FAIL: native OAuth session bridge; inspect configuration and backend diagnostics\n";return 1;}
 catch(...){std::cerr<<"FAIL: native OAuth session bridge unexpected exception\n";return 1;}
}