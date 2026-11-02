#pragma once
#include "CloudSettings.h"
#include "../audit/AuditDatabase.h"
namespace etlforge::cloud {
inline boost::json::value request(audit::Database& db,const boost::json::object& input,const std::string& vaultKey) {
 auto rows=db.query("SELECT encode(convert_to(settings::text,'UTF8'),'hex'),revision::text FROM cloudlogging.configuration WHERE singleton FOR UPDATE");
 if(rows.empty())throw std::runtime_error("Cloud logging database migration is not installed");
 auto settings=boost::json::parse(hex(*rows[0][0],true)).as_object();auto action=value(input,"action");
 if(action=="cloud.save") {
  auto proposed=input.at("settings").as_object();
  for(auto field:{"passwordProtected","bearerProtected"})proposed.erase(field);
  for(auto pair:{std::make_pair("password","passwordProtected"),std::make_pair("bearerToken","bearerProtected")}) {
   auto plain=value(proposed,pair.first);
   if(plain.find_first_of("\r\n")!=std::string::npos)throw std::runtime_error("Credential contains an invalid newline");
   proposed[pair.second]=plain.empty()?value(settings,pair.second):protect(plain,vaultKey);proposed.erase(pair.first);
  }
  auto revision=input.if_contains("revision");if(!revision||!revision->is_int64()||std::to_string(revision->as_int64())!=*rows[0][1])throw std::runtime_error("Cloud settings changed; reload before saving");
  validate(proposed);
  bool enabling=proposed.at("enabled").as_bool() && (!settings.if_contains("enabled")||!settings.at("enabled").as_bool());
  db.query("UPDATE cloudlogging.configuration SET settings=convert_from(decode(?,'hex'),'UTF8')::jsonb,revision=revision+1,updated_at=now(),activated_at=CASE WHEN ?='true' AND activated_at IS NULL THEN localtimestamp ELSE activated_at END WHERE singleton",{hex(boost::json::serialize(proposed)),enabling?"true":"false"});
  settings=std::move(proposed);
 }else if(action!="cloud.settings")throw std::runtime_error("Unknown cloud logging action");
 auto current=db.query("SELECT revision::text FROM cloudlogging.configuration WHERE singleton");
 auto counts=db.query("SELECT cloud_upload_status::text,count(*)::text FROM public.operationinstancetab GROUP BY cloud_upload_status");
 boost::json::object result{{"settings",publicSettings(settings)},{"revision",std::stoll(*current[0][0])}};
 boost::json::object summary;for(auto& r:counts)summary[*r[0]]=std::stoll(*r[1]);result["counts"]=std::move(summary);return result;
}
}
