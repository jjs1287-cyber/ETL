#include "NativeJsonBackend.h"
#include "EtlForgeJsonConversion.h"
#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/ItemAccess/UserAccess.h>
#include <konga/DbRelations/EntityIdConstants.h>
#include <konga/Utils/Settings.h>
#include <konga/Utils/CrException.h>
#include <sqlext.h>
#include <memory>

namespace {
std::wstring wide(const std::string& value) {
    int size=MultiByteToWideChar(CP_UTF8,MB_ERR_INVALID_CHARS,value.data(),static_cast<int>(value.size()),0,0);
    if(!size && !value.empty())throw std::runtime_error("JSON path is not valid UTF-8.");
    std::wstring result(size,0);
    if(size)MultiByteToWideChar(CP_UTF8,MB_ERR_INVALID_CHARS,value.data(),static_cast<int>(value.size()),&result[0],size);
    return result;
}
class NativeJsonBackend final : public konga::JsonEngineWrapper {
    konga::DbConnection& connection;
    konga::UserInfo user;
    std::wstring schema(long id,bool source) {
        if(id<=0)throw std::runtime_error("Invalid JSON transformation ID.");
        konga::UserAccess access(user,connection);
        if(!access.HasReadPermission(id,TransformationEntityId))throw std::runtime_error("Permission denied for JSON transformation.");
        struct Statement {SQLHSTMT value=SQL_NULL_HSTMT;~Statement(){if(value!=SQL_NULL_HSTMT)SQLFreeHandle(SQL_HANDLE_STMT,value);}} statement;
        if(!SQL_SUCCEEDED(SQLAllocHandle(SQL_HANDLE_STMT,connection.GetConnectionHandle(),&statement.value)))throw std::runtime_error("Cannot allocate JSON schema query.");
        std::string sql="SELECT encode(convert_to("+std::string(source?"source_xml":"target_xml")+",'UTF8'),'hex') FROM transformationstab WHERE transformation_id="+std::to_string(id);
        if(!SQL_SUCCEEDED(SQLExecDirectA(statement.value,reinterpret_cast<SQLCHAR*>(&sql[0]),SQL_NTS)))throw std::runtime_error("Cannot query JSON transformation schema.");
        auto fetched=SQLFetch(statement.value);
        if(fetched==SQL_NO_DATA)throw std::runtime_error("Unknown JSON transformation ID.");
        if(!SQL_SUCCEEDED(fetched))throw std::runtime_error("Cannot read JSON transformation schema.");
        char hex[131073]={}; SQLLEN length=0;
        auto read=SQLGetData(statement.value,1,SQL_C_CHAR,hex,sizeof(hex),&length);
        if(read!=SQL_SUCCESS || length==SQL_NULL_DATA || length<=0 || length>=sizeof(hex) || length%2)throw std::runtime_error("Missing or invalid JSON transformation XSD path.");
        std::string path;path.reserve(static_cast<size_t>(length)/2);
        auto digit=[](char c)->int {if(c>='0'&&c<='9')return c-'0';if(c>='a'&&c<='f')return c-'a'+10;throw std::runtime_error("Invalid JSON schema path encoding.");};
        for(SQLLEN i=0;i<length;i+=2)path.push_back(static_cast<char>((digit(hex[i])<<4)|digit(hex[i+1])));
        return etlforge::resolveJsonSchemaPath(wide(konga::GetServerHome()),wide(path));
    }
    void convert(const std::string& input,const std::string& output,long id,bool source) {
        try {auto xsd=schema(id,source);etlforge::convertJsonAtomically(wide(input),xsd,wide(output),source);}
        catch(const std::exception& error){throw konga::CrException(std::string("Native JSON conversion failed: ")+error.what());}
    }
public:
    NativeJsonBackend(konga::DbConnection& connection,const konga::UserInfo& user):connection(connection),user(user){}
    void startNewSession() override {}
    void closeSession() override {}
    void jsonToXml(const std::string& input,const std::string& output,long id) override {convert(input,output,id,true);}
    void xmlToJson(const std::string& input,const std::string& output,long id) override {convert(input,output,id,false);}
};
}
namespace konga {
JsonEngineWrapper* createNativeJsonBackend(DbConnection& connection,const UserInfo& user){return new NativeJsonBackend(connection,user);}
}
