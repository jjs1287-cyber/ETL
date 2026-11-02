#include "AuditDeployment.h"
#include <windows.h>
#include <wincrypt.h>
#include <objbase.h>
#include <fstream>
#include <iterator>
#include <algorithm>
#include <cstdlib>

namespace etlforge::audit {
namespace {
std::string uuid() {
    GUID id;if(CoCreateGuid(&id)!=S_OK)throw std::runtime_error("Cannot create audit deployment identity");
    wchar_t value[40];StringFromGUID2(id,value,40);std::string result;
    for(int i=1;i<37;++i)result.push_back(static_cast<char>(value[i]));return result;
}
std::vector<unsigned char> loadKey() {
    const char* home=std::getenv("ETLFORGE_HOME");if(!home)throw std::runtime_error("Audit server home is missing");
    std::ifstream input(std::string(home)+"/services/audit-hmac-v1.dpapi",std::ios::binary);
    if(!input)throw std::runtime_error("Protected audit comparison key is missing");
    std::vector<unsigned char> encrypted((std::istreambuf_iterator<char>(input)),{});
    if(encrypted.empty() || encrypted.size()>4096)throw std::runtime_error("Invalid protected audit comparison key");
    DATA_BLOB source{static_cast<DWORD>(encrypted.size()),encrypted.data()},plain{};
    if(!CryptUnprotectData(&source,nullptr,nullptr,nullptr,nullptr,CRYPTPROTECT_UI_FORBIDDEN,&plain))throw std::runtime_error("Cannot unlock protected audit comparison key");
    std::vector<unsigned char> key(plain.pbData,plain.pbData+plain.cbData);SecureZeroMemory(plain.pbData,plain.cbData);LocalFree(plain.pbData);
    if(key.size()!=32)throw std::runtime_error("Invalid audit comparison key length");return key;
}
EntityPlan plan(int type,const char* label,const std::string& stem,const std::string& plural) {
    EntityPlan p;p.type=type;p.typeName=label;p.nameColumn=stem+"_name";
    p.guidTable="guidsfor"+plural+"tab";p.membershipTable="interchangeproject"+plural+"tab";
    p.root.table=stem+"stab";p.root.primaryKey=stem+"_id";
    TablePlan properties;properties.table=stem+"propertiestab";properties.primaryKey=stem+"properties_id";properties.ownerColumn=stem+"_id";
    p.children.push_back(properties);return p;
}
}
DeploymentCapture::DeploymentCapture(Database& database,const std::string& projectGuid)
    :database_(database),store_(database),projectGuid_(projectGuid) {
    auto enabled=database_.query("SELECT enabled::text FROM audit.capture_configuration WHERE singleton");
    enabled_=!enabled.empty() && *enabled[0][0]=="true";
    if(!enabled_)return;
    store_.lockProject(projectGuid_);key_=loadKey();before_=read();
}
Objects DeploymentCapture::read() {
    auto operation=plan(6,"Operation","operation","operations");
    operation.root.references={{"source_id","guidsforsourcestab"},{"target_id","guidsfortargetstab"},{"schedule_id","guidsforschedulestab"},{"transformation_id","audit.transformation_identity"},{"success_operation_id","guidsforoperationstab"},{"failure_operation_id","guidsforoperationstab"},{"success_message_id","guidsformessagestab"},{"failure_message_id","guidsformessagestab"}};
    TablePlan chains;chains.table="transformationchainlinkstab";chains.primaryKey="transformationchainlink_id";chains.ownerColumn="operation_id";
    chains.references={{"transformation_id","audit.transformation_identity"},{"nexttransformation_id","audit.transformation_identity"}};operation.children.push_back(chains);
    auto supplement=[&](const EntityPlan& p,const boost::json::object& row)->boost::json::value {
        std::string id=boost::json::serialize(row.at(p.root.primaryKey));
        boost::json::object result;boost::json::array folders,plugins;
        for(const auto& item:database_.query("SELECT g.guid FROM folderitemtab f JOIN guidsforfolderstab g ON g.item_id=f.parentfolder_id WHERE f.entitytype_id=CAST(? AS integer) AND f.item_id=CAST(? AS integer) ORDER BY g.guid",{std::to_string(p.type),id}))folders.push_back(boost::json::value(*item[0]));
        result["folders"]=std::move(folders);
        for(const auto& item:database_.query("SELECT jsonb_build_object('plugin',p.pluginname,'pipelineType',x.pipelineentitytypeid,'position',x.relativepositionid)::text FROM pipelineplugininstancetab i JOIN pluginstab p ON p.pluginid=i.pluginid LEFT JOIN pipelinepluginpositiontab x ON x.pipelineplugininstanceid=i.pipelineplugininstanceid WHERE i.entitytypeid=CAST(? AS integer) AND i.entityid=CAST(? AS integer) ORDER BY p.pluginname,x.pipelineentitytypeid,x.relativepositionid",{std::to_string(p.type),id}))plugins.push_back(boost::json::parse(*item[0]));
        result["plugins"]=std::move(plugins);
        if(p.type==6) {
            boost::json::array pipeline;
            for(const auto& item:database_.query("SELECT activitytype_id::text,activity_id::text FROM operationpipelineactivitiestab WHERE operation_id=CAST(? AS integer) ORDER BY operationactivity_id",{id})) {
                const int type=std::stoi(*item[0]);const char* table=nullptr;
                switch(type){case 1:table="guidsforscriptstab";break;case 3:table="guidsforsourcestab";break;case 4:table="guidsfortargetstab";break;case 5:table="audit.transformation_identity";break;case 6:table="guidsforwebservicestab";break;case 7:table="guidsforsalesforcebulkloadstab";break;case 8:table="guidsforxslttransformstab";break;}
                std::string identity;
                auto refs=type==2?database_.query("SELECT pluginname FROM pluginstab WHERE pluginid=CAST(? AS integer)",{*item[1]}):table?database_.query(std::string("SELECT guid FROM ")+table+" WHERE item_id=CAST(? AS integer)",{*item[1]}):std::vector<Row>{};
                if(refs.size()!=1)throw std::runtime_error("Audit operation contains an unsupported or unresolved pipeline activity");
                pipeline.push_back(boost::json::object{{"type",type},{"object",*refs[0][0]}});
            }
            result["pipeline"]=std::move(pipeline);
        }
        return result;
    };
    Rules rules;rules.version="2";
    return ObjectReader(database_,{operation,plan(23,"Script","script","scripts"),plan(34,"XSLT","xslttransform","xslttransforms"),plan(35,"Project Variable","projectvariable","projectvariables")},rules,key_,"v1",{},supplement).readProject(projectGuid_);
}
void DeploymentCapture::complete(long long userId,const std::string& userName,const std::string& projectName) {
    if(!enabled_)return;
    store_.record(Deployment{uuid(),projectGuid_,projectName,userName,userId},before_,read());
}
}
