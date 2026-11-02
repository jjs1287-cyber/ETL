#include <konga/Utils/Settings.h>
#include <konga/Utils/CrException.h>
#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/ItemAccess/UserInfo.h>
#include <konga/JavaBackend/JsonEngine/JsonEngineWrapper.h>
#include <konga/JavaBackend/BulkLoadEngine/BulkLoadEngineWrapper.h>
#include <axis/AxisException.hpp>
#include <sqlext.h>
#include <iostream>
#include <memory>
#include <stdexcept>
namespace {
void checkSql(SQLRETURN result){if(!SQL_SUCCEEDED(result))throw std::runtime_error("Application ODBC smoke query failed");}
std::string scalar(SQLHDBC connection,const char* query){
 SQLHSTMT statement=SQL_NULL_HSTMT;checkSql(SQLAllocHandle(SQL_HANDLE_STMT,connection,&statement));
 try{
  checkSql(SQLExecDirectA(statement,reinterpret_cast<SQLCHAR*>(const_cast<char*>(query)),SQL_NTS));checkSql(SQLFetch(statement));
  char value[128]={};SQLLEN length=0;checkSql(SQLGetData(statement,1,SQL_C_CHAR,value,sizeof(value),&length));
  SQLFreeHandle(SQL_HANDLE_STMT,statement);return value;
 }catch(...){SQLFreeHandle(SQL_HANDLE_STMT,statement);throw;}
}
}
int main(int argc,char** argv){
 try{
  static_assert(sizeof(void*)==8,"Windows x64 only");
  konga::SetCommandLineOptions(argc,argv);
  konga::SmartDbPtr<konga::DbConnection> database(konga::GetBackEndDbConnection("TranDb"));
  SQLHDBC connection=database->GetConnectionHandle();
  if(scalar(connection,"SELECT current_user")!="etlforge_dev")throw std::runtime_error("Unexpected database role");
  if(scalar(connection,"SHOW standard_conforming_strings")!="off")throw std::runtime_error("PostgreSQL 18 session escaping compatibility was not initialized");
  database->ExecuteSql("CREATE TEMP TABLE etlforge_backend_smoke(value text, big_number bigint)");
  database->ExecuteSql(u8"INSERT INTO etlforge_backend_smoke VALUES('A\\\\B''\u65e5\u672c',4294967297)");
  if(scalar(connection,"SELECT value FROM etlforge_backend_smoke")!=u8"A\\B'\u65e5\u672c")throw std::runtime_error("Application SQL quoting/UTF-8 round-trip failed");
  if(scalar(connection,"SELECT big_number FROM etlforge_backend_smoke")!="4294967297")throw std::runtime_error("Application bigint round-trip failed");
  const konga::UserInfo admin=konga::GetAdminUserInfo(*database);
  if(!admin.IsAdministrator() || admin.GetLogin()!="Admin")throw std::runtime_error("Native application account/group lookup failed");
  std::unique_ptr<konga::JsonEngineWrapper> json(konga::JsonEngineWrapper::createJsonEngineWrapper(*database,admin));
  json->startNewSession();json->closeSession();
  std::unique_ptr<konga::BulkLoadEngineWrapper> bulk(konga::BulkLoadEngineWrapper::createBulkLoadEngineWrapper(*database,admin));
  bulk->startNewSession();bulk->closeSession();
  std::cout<<"PASS: production native database connection, restricted role, PostgreSQL 18 escaping, UTF-8/bigint, account/group lookup, real Java JSON and bulk-load sessions\n";
  return 0;
 }catch(const konga::CrException& error){std::cerr<<error.GetErrorString()<<'\n';return 1;}
 catch(const axiscpp::AxisException& error){std::cerr<<error.what()<<'\n';return 1;}
 catch(const std::exception& error){std::cerr<<error.what()<<'\n';return 1;}
}
