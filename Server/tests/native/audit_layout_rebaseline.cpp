#include "AuditDatabase.h"
#include "AuditObjectReader.h"
#include <fstream>
#include <iostream>
#include <sstream>
#include <stdexcept>
#include <future>
#include <chrono>

using namespace etlforge::audit;
int checks = 0;
void require(bool ok,const char* message) {++checks;if(!ok)throw std::runtime_error(message);}
struct Connection {
    SQLHENV environment = SQL_NULL_HENV;
    SQLHDBC handle = SQL_NULL_HDBC;
    Connection() {
        if(!SQL_SUCCEEDED(SQLAllocHandle(SQL_HANDLE_ENV,SQL_NULL_HANDLE,&environment)) ||
            !SQL_SUCCEEDED(SQLSetEnvAttr(environment,SQL_ATTR_ODBC_VERSION,reinterpret_cast<void*>(SQL_OV_ODBC3),0)) ||
            !SQL_SUCCEEDED(SQLAllocHandle(SQL_HANDLE_DBC,environment,&handle))) throw std::runtime_error("ODBC initialization failed");
        const char* connection = std::getenv("ETLFORGE_AUDIT_TEST_CONNECTION");
        if(!connection) throw std::runtime_error("Audit test connection not supplied");
        if(!SQL_SUCCEEDED(SQLDriverConnectA(handle,nullptr,reinterpret_cast<SQLCHAR*>(const_cast<char*>(connection)),SQL_NTS,nullptr,0,nullptr,SQL_DRIVER_NOPROMPT)))
            throw std::runtime_error("Audit test database connection failed");
        if(!SQL_SUCCEEDED(SQLSetConnectAttr(handle,SQL_ATTR_AUTOCOMMIT,reinterpret_cast<void*>(SQL_AUTOCOMMIT_OFF),0))) throw std::runtime_error("Cannot begin audit test transaction");
    }
    void commit() {if(!SQL_SUCCEEDED(SQLEndTran(SQL_HANDLE_DBC,handle,SQL_COMMIT)))throw std::runtime_error("Test commit failed");}
    void rollback() {SQLEndTran(SQL_HANDLE_DBC,handle,SQL_ROLLBACK);}
    ~Connection() {if(handle){rollback();SQLDisconnect(handle);SQLFreeHandle(SQL_HANDLE_DBC,handle);}if(environment)SQLFreeHandle(SQL_HANDLE_ENV,environment);}
};
int main() {try {
 Connection connection;Database db(connection.handle);
 db.query("CREATE TABLE audit.layout_v1_state_backup AS TABLE audit.object_state");
 db.query("CREATE TABLE audit.layout_v1_baseline_backup AS TABLE audit.project_baseline");
 db.query("REVOKE ALL ON audit.layout_v1_state_backup,audit.layout_v1_baseline_backup FROM PUBLIC");
 auto rows=db.query("SELECT project_guid::text,object_type::text,object_guid::text,protected_snapshot::text,visible_snapshot::text,is_deleted::text FROM audit.object_state FOR UPDATE");
 for(const auto& row:rows){
  if(*row[5]=="true"){db.query("UPDATE audit.object_state SET normalization_version='2' WHERE project_guid=CAST(? AS uuid) AND object_type=CAST(? AS integer) AND object_guid=CAST(? AS uuid)",{row[0],row[1],row[2]});continue;}
  auto envelope=boost::json::parse(*row[3]),visible=boost::json::parse(*row[4]);
  if(envelope.at("rulesVersion")!="1")throw std::runtime_error("Unexpected normalization version during layout migration");
  if(*row[1]=="6"){
   envelope.at("object").at("children").as_object().erase("operationgraphlayouttab");
   visible.at("children").as_object().erase("operationgraphlayouttab");
  }
  envelope.as_object()["rulesVersion"]="2";
  auto snapshot=restoreProtectedSnapshot(envelope,visible);
  db.query("UPDATE audit.object_state SET normalization_version='2',fingerprint=?,protected_snapshot=CAST(? AS jsonb),visible_snapshot=CAST(? AS jsonb) WHERE project_guid=CAST(? AS uuid) AND object_type=CAST(? AS integer) AND object_guid=CAST(? AS uuid)",{snapshot.fingerprint,snapshot.protectedCanonical,canonicalJson(snapshot.visible),row[0],row[1],row[2]});
 }
 db.query("UPDATE audit.project_baseline SET normalization_version='2'");
 connection.commit();std::cout<<"PASS: "<<rows.size()<<" audit comparison baselines upgraded; original baselines backed up; historical events unchanged\n";return 0;
 }catch(const std::exception& error){std::cerr<<"FAIL: "<<error.what()<<'\n';return 1;}}