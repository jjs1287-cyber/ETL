#include "AuditDatabase.h"
#include "AuditObjectReader.h"
#include "AuditDeployment.h"
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
 auto projects=db.query("SELECT guid,interchangeproject_name FROM guidsforinterchangeprojectstab g JOIN interchangeprojectstab p ON p.interchangeproject_id=g.item_id");
 auto events=*db.query("SELECT count(*)::text FROM audit.event")[0][0];
 for(const auto& project:projects){DeploymentCapture capture(db,*project[0]);capture.complete(1,"Rollback-only verification",*project[1]);connection.rollback();}
 require(*db.query("SELECT count(*)::text FROM audit.event")[0][0]==events,"Read-only probe changed audit event count");
 std::cout<<"PASS: stable production capture snapshots for "<<projects.size()<<" existing projects; all writes rolled back\n";return 0;
 }catch(const std::exception& error){std::cerr<<"FAIL: "<<error.what()<<'\n';return 1;}}