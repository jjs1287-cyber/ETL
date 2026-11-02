#pragma once
#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>
#include <konga/Utils/Settings.h>
#include <konga/Utils/StringUtils.h>
#include <objbase.h>

namespace etlforge { namespace cluster {
inline std::string scheduleRevision() {
 konga::SmartDbPtr<konga::DbConnection> db(konga::GetBackEndDbConnection("TranDb"));
 db->SetQueryTimeout(10);
 konga::SmartDbPtr<konga::DynamicDbRecordSet> rows(konga::CreateDynamicDbRecordSet(db,"SELECT revision::text FROM etlforge_cluster.schedule_revision WHERE singleton"));
 rows->Open();if(!rows->Next())throw konga::CrException("Shared schedule revision is not installed");return rows->GetFieldValue(0);
}
inline const std::string& schedulerIncarnation() {
 static const std::string incarnation=[](){GUID id{};if(FAILED(CoCreateGuid(&id)))throw konga::CrException("Cannot create Scheduler incarnation");wchar_t value[40]{};StringFromGUID2(id,value,40);std::string result;for(int i=1;i<37;++i)result+=static_cast<char>(value[i]);return result;}();
 return incarnation;
}
inline void releaseSchedulerHeartbeat() {
 const std::string node=konga::GetOptionAsString("Cluster","NodeId","");if(node.empty())return;
 konga::SmartDbPtr<konga::DbConnection> db(konga::GetBackEndDbConnection("TranDb"));db->SetQueryTimeout(10);
 db->ExecuteSql("DELETE FROM etlforge_cluster.service_heartbeat WHERE node_id='"+konga::SqlEscape(node)+"'::uuid AND service='Scheduler' AND incarnation='"+schedulerIncarnation()+"'::uuid");
}
inline void schedulerHeartbeat() {
 const std::string node=konga::GetOptionAsString("Cluster","NodeId","");
 if(node.empty())return;
 const std::string& incarnation=schedulerIncarnation();
 wchar_t name[256]{};DWORD count=256;if(!GetComputerNameW(name,&count))throw konga::CrException("Cannot read node hostname");
 int bytes=WideCharToMultiByte(CP_UTF8,WC_ERR_INVALID_CHARS,name,count,0,0,0,0);if(bytes<=0)throw konga::CrException("Cannot encode node hostname");
 std::string host(bytes,'\0');WideCharToMultiByte(CP_UTF8,WC_ERR_INVALID_CHARS,name,count,&host[0],bytes,0,0);
 konga::SmartDbPtr<konga::DbConnection> db(konga::GetBackEndDbConnection("TranDb"));
 db->SetQueryTimeout(10);
 db->ExecuteSql("SELECT etlforge_cluster.scheduler_heartbeat('"+konga::SqlEscape(node)+"'::uuid,'"+konga::SqlEscape(host)+"','"+incarnation+"'::uuid,"+konga::toString(GetCurrentProcessId())+")");
}
}}
