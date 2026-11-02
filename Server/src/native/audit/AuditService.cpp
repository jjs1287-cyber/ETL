#include "AuditService.h"
#include "../cluster/ClusterAdmin.h"
#include "../ssh/SshKeys.h"
#include "../s3/S3Client.h"
#include <konga/Utils/Settings.h>
#include "AuditDatabase.h"
#include "../cloudlogging/CloudAdmin.h"
#include <algorithm>
#include <cstring>
#include <konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.h>
#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/AutoDbCleanup.h>

namespace {
class AuditSession : public konga::WsImplBaseAuthenticator {
public:
    AuditSession(const char* user,const char* password,bool requireAdmin=true):WsImplBaseAuthenticator(user,password) {
        if(requireAdmin&&!m_UserInfo.IsAdministrator())throw std::runtime_error("Audit administration requires an administrator account.");
    }
    bool administrator()const{return m_UserInfo.IsAdministrator();}
    long long id()const{return m_UserInfo.GetId();}
    std::string name()const{return m_UserInfo.GetLogin();}
};
}
std::string etlforgeAuditRequest(const char* user,const char* password,const char* payload) {
    if(!payload || strlen(payload)>131072)throw std::runtime_error("Invalid audit request size.");
    auto request=boost::json::parse(payload).as_object();
    auto requestedAction=request.if_contains("action");
    bool listKeys=requestedAction&&requestedAction->is_string()&&requestedAction->as_string()=="ssh.list";
    bool s3Test=requestedAction&&requestedAction->is_string()&&requestedAction->as_string()=="s3.test";
    AuditSession session(user,password,!listKeys&&!s3Test);
    if(s3Test)return etlforge::s3::request(payload);
    auto text=[&](const char* field)->std::string {auto item=request.if_contains(field);return item ? std::string(item->as_string()) : "";};
    auto number=[&](const char* field,int fallback)->int {auto item=request.if_contains(field);if(!item)return fallback;auto value=item->as_int64();if(value<0 || value>1000000)throw std::runtime_error("Invalid audit filter.");return static_cast<int>(value);};
    konga::SmartDbPtr<konga::DbConnection> connection;
    connection.Attach(konga::GetBackEndDbConnection("TranDb"));
    konga::AutoRollbackTransaction transaction(connection);
    etlforge::audit::Database database(connection->GetConnectionHandle());
    if(requestedAction&&requestedAction->is_string()&&requestedAction->as_string().starts_with("ssh.")) {
        auto result=etlforge::ssh::request(connection->GetConnectionHandle(),konga::GetServerHomeWithSlash()+"private/ssh-keys",konga::GetOptionAsString("Security","KeyVaultMasterKey",""),payload,session.administrator());
        transaction.Commit();return result;
    }
    if(text("action").rfind("cloud.",0)==0) {
        auto result=etlforge::cloud::request(database,request,konga::GetOptionAsString("Security","KeyVaultMasterKey",""));
        transaction.Commit();return boost::json::serialize(result);
    }
    if(text("action").rfind("cluster.",0)==0) {
        boost::json::object sharedConfiguration;
        for(const char* key:{"Driver","JdbcDriverClass","JdbcDriverName","Server","Port","User","Password","TranDb","MsgDb","PDb","IDb","ClientEncoding"})
            sharedConfiguration[key]=konga::GetOptionAsString("DbInfo",key,"");
        auto result=etlforge::cluster::request(database,request,konga::GetOptionAsString("Security","KeyVaultMasterKey",""),sharedConfiguration);
        transaction.Commit();return boost::json::serialize(result);
    }
    etlforge::audit::Store store(database);
    boost::json::array projects;
    std::vector<std::string> scope;
    for(const auto& row:database.query("SELECT guid,interchangeproject_name FROM public.guidsforinterchangeprojectstab g JOIN public.interchangeprojectstab p ON p.interchangeproject_id=g.item_id ORDER BY lower(interchangeproject_name),guid")) {
        scope.push_back(*row[0]);projects.push_back(boost::json::object{{"guid",*row[0]},{"name",*row[1]}});
    }
    for(const auto& row:database.query("SELECT DISTINCT project_guid::text FROM audit.event")) {
        if(std::find(scope.begin(),scope.end(),*row[0])==scope.end())scope.push_back(*row[0]);
    }
    auto action=text("action");
    boost::json::value result;
    if(action=="settings") {
        result=store.settings();auto& settings=result.as_object();
        settings["projects"]=std::move(projects);settings["canManageSettings"]=true;
        auto capture=database.query("SELECT enabled::text,scope FROM audit.capture_configuration WHERE singleton");
        settings["captureEnabled"]=!capture.empty() && *capture[0][0]=="true";
        settings["captureScope"]=capture.empty()?"":*capture[0][1];
    } else if(action=="saveSettings") {
        auto enabled=request.if_contains("retentionEnabled");
        if(enabled && enabled->as_bool())throw std::runtime_error("Automatic cleanup cannot be enabled until the server cleanup integration is installed.");
        store.saveSettings(session.id(),session.name(),number("detailDays",30),number("headerDays",365),false);
        result=store.settings();
    } else if(action=="list") {
        auto project=text("projectGuid");
        if(!project.empty()) {
            if(std::find(scope.begin(),scope.end(),project)==scope.end())throw std::runtime_error("Audit project is not accessible.");
            scope={project};
        }
        etlforge::audit::Filter filter;
        filter.days=number("days",30);filter.objectType=number("objectType",0);
        filter.offset=number("offset",0);filter.limit=number("limit",50);filter.search=text("search");
        result=store.list(scope,filter);
    } else if(action=="detail") {
        auto id=request.if_contains("eventId");if(!id || id->as_int64()<=0)throw std::runtime_error("Invalid audit entry.");
        result=store.detail(scope,id->as_int64());
    } else throw std::runtime_error("Unknown audit action.");
    transaction.Commit();return boost::json::serialize(result);
}
