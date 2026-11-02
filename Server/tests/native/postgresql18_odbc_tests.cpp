#include <windows.h>
#include <sql.h>
#include <sqlext.h>
#include <fstream>
#include <string>
#include <iostream>
#include <stdexcept>
struct Handles {
 SQLHENV environment=SQL_NULL_HENV;SQLHDBC connection=SQL_NULL_HDBC;SQLHSTMT statement=SQL_NULL_HSTMT;
 ~Handles(){if(statement)SQLFreeHandle(SQL_HANDLE_STMT,statement);if(connection){SQLDisconnect(connection);SQLFreeHandle(SQL_HANDLE_DBC,connection);}if(environment)SQLFreeHandle(SQL_HANDLE_ENV,environment);}
};
int main(int argc,char** argv){
 static_assert(sizeof(void*)==8 && sizeof(SQLLEN)==8,"Windows x64 ODBC required");
 if(argc!=2)return 10;
 std::ifstream input(argv[1],std::ios::binary);std::string password;std::getline(input,password);if(password.empty())return 11;
 Handles h;
 auto require=[&](SQLRETURN result,SQLSMALLINT type,SQLHANDLE handle,const char* operation){
  if(SQL_SUCCEEDED(result))return;
  SQLCHAR state[6]={},message[2048]={};SQLINTEGER code=0;SQLSMALLINT length=0;
  SQLGetDiagRecA(type,handle,1,state,&code,message,sizeof(message),&length);
  std::string detail(reinterpret_cast<char*>(message));for(size_t p=0;(p=detail.find(password,p))!=std::string::npos;){detail.replace(p,password.size(),"<redacted>");p+=10;}
  throw std::runtime_error(std::string(operation)+": "+detail);
 };
 try{
  require(SQLAllocHandle(SQL_HANDLE_ENV,SQL_NULL_HANDLE,&h.environment),SQL_HANDLE_ENV,h.environment,"Allocate environment");
  require(SQLSetEnvAttr(h.environment,SQL_ATTR_ODBC_VERSION,reinterpret_cast<SQLPOINTER>(static_cast<SQLULEN>(SQL_OV_ODBC3)),0),SQL_HANDLE_ENV,h.environment,"Set ODBC version");
  require(SQLAllocHandle(SQL_HANDLE_DBC,h.environment,&h.connection),SQL_HANDLE_ENV,h.environment,"Allocate connection");
  SQLSetConnectAttr(h.connection,SQL_ATTR_LOGIN_TIMEOUT,reinterpret_cast<SQLPOINTER>(static_cast<SQLULEN>(5)),0);
  std::string options="Driver={PostgreSQL Unicode(x64)};Server=127.0.0.1;Port=6543;Database=postgres;Uid=etlforge_admin;Pwd="+password+";SSLmode=disable;";
  require(SQLDriverConnectA(h.connection,nullptr,reinterpret_cast<SQLCHAR*>(&options[0]),SQL_NTS,nullptr,0,nullptr,SQL_DRIVER_NOPROMPT),SQL_HANDLE_DBC,h.connection,"Connect to isolated PostgreSQL");
  SQLCHAR version[64]={};SQLSMALLINT infoLength=0;
  require(SQLGetInfoA(h.connection,SQL_DBMS_VER,version,sizeof(version),&infoLength),SQL_HANDLE_DBC,h.connection,"Read database version");
  if(std::string(reinterpret_cast<char*>(version)).find("18.")!=0)throw std::runtime_error("Unexpected database version");
  require(SQLAllocHandle(SQL_HANDLE_STMT,h.connection,&h.statement),SQL_HANDLE_DBC,h.connection,"Allocate statement");
  for(const char* query : {"CREATE TEMP TABLE etlforge_odbc_smoke(id bigint,value text)","INSERT INTO etlforge_odbc_smoke VALUES(4294967297,'ETLForge')","SELECT id,value FROM etlforge_odbc_smoke"}){
   require(SQLExecDirectA(h.statement,reinterpret_cast<SQLCHAR*>(const_cast<char*>(query)),SQL_NTS),SQL_HANDLE_STMT,h.statement,"Execute smoke SQL");
  }
  long long id=0;char value[64]={};SQLLEN idLength=0,valueLength=0;
  require(SQLBindCol(h.statement,1,SQL_C_SBIGINT,&id,sizeof(id),&idLength),SQL_HANDLE_STMT,h.statement,"Bind bigint");
  require(SQLBindCol(h.statement,2,SQL_C_CHAR,value,sizeof(value),&valueLength),SQL_HANDLE_STMT,h.statement,"Bind text");
  require(SQLFetch(h.statement),SQL_HANDLE_STMT,h.statement,"Fetch row");
  if(id!=4294967297LL || std::string(value)!="ETLForge" || valueLength!=8)throw std::runtime_error("ODBC round-trip mismatch");
  std::cout<<"PASS: PostgreSQL "<<version<<", x64 ODBC, SCRAM authentication, SQL execution, bigint above 32-bit range and SQLLEN bindings\n";
 }catch(const std::exception& error){std::cerr<<error.what()<<"\n";return 1;}
}
