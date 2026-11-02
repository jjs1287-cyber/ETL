#pragma once
#include "../audit/AuditDatabase.h"
#include <stdexcept>

namespace etlforge::cluster {
inline std::string field(const boost::json::object& input,const char* key) {
 auto item=input.if_contains(key);
 if(!item||!item->is_string())throw std::runtime_error("Missing multi-server text field");
 std::string value(item->as_string());
 if(value.size()>512||value.find_first_of("\r\n")!=std::string::npos||value.find('\0')!=std::string::npos)throw std::runtime_error("Invalid multi-server text field");
 for(unsigned char c:value)if(c<32||c==127)throw std::runtime_error("Control characters are not allowed in multi-server text fields");
 return value;
}
inline boost::json::value request(audit::Database& db,const boost::json::object& input,const std::string& vaultKey,const boost::json::object& sharedConfiguration) {
 auto action=field(input,"action");
 auto rows=db.query("SELECT cluster_id::text,cluster_name,revision::text,heartbeat_timeout_seconds::text FROM etlforge_cluster.configuration WHERE singleton FOR UPDATE");
 if(rows.size()!=1)throw std::runtime_error("Multi-server database migration is not installed");
 if(action=="cluster.bootstrap") {
  auto accepted=input.if_contains("acceptTrustedNetworkBootstrapRisk");
  if(!accepted||!accepted->is_bool()||!accepted->as_bool())throw std::runtime_error("Cluster bootstrap risk acceptance is required");
  if(vaultKey.size()<32)throw std::runtime_error("The existing server has no valid cluster vault master key");
  return boost::json::object{
   {"format",1},{"clusterId",*rows[0][0]},{"clusterName",*rows[0][1]},
   {"heartbeatTimeout",std::stoi(*rows[0][3])},{"keyVaultMasterKey",vaultKey},
   {"sharedConfiguration",sharedConfiguration}
  };
 } else if(action=="cluster.save") {
  auto revision=input.if_contains("revision"),timeout=input.if_contains("heartbeatTimeout");
  if(!revision||!revision->is_int64()||std::to_string(revision->as_int64())!=*rows[0][2])throw std::runtime_error("Multi-server settings changed; reload before saving");
  if(!timeout||!timeout->is_int64()||timeout->as_int64()<30||timeout->as_int64()>600)throw std::runtime_error("Heartbeat timeout must be 30 to 600 seconds");
  auto name=field(input,"clusterName");if(name.empty()||name.size()>120)throw std::runtime_error("Cluster name must contain 1 to 120 UTF-8 bytes");
  db.query("UPDATE etlforge_cluster.configuration SET cluster_name=?,heartbeat_timeout_seconds=CAST(? AS integer),revision=revision+1,updated_at=now() WHERE singleton",{name,std::to_string(timeout->as_int64())});
  rows=db.query("SELECT cluster_id::text,cluster_name,revision::text,heartbeat_timeout_seconds::text FROM etlforge_cluster.configuration WHERE singleton");
 } else if(action=="cluster.node.save") {
  auto url=field(input,"apiUrl"),name=field(input,"displayName"),id=field(input,"nodeId");
  if(name.empty()||name.size()>120)throw std::runtime_error("Node name must contain 1 to 120 UTF-8 bytes");
  if(!url.empty()) {
   auto start=url.rfind("https://",0)==0?8:url.rfind("http://",0)==0?7:0;
   if(!start||url.size()<=static_cast<size_t>(start)||url.find_first_of(" /\\?#@",start)!=std::string::npos)throw std::runtime_error("API address must be an HTTP(S) origin, for example http://192.168.1.20:47100");
  }
  auto changed=db.query("UPDATE etlforge_cluster.node SET display_name=?,api_url=? WHERE node_id=CAST(? AS uuid) RETURNING node_id::text",{name,url,id});
  if(changed.empty())throw std::runtime_error("Node is no longer registered");
 } else if(action!="cluster.settings")throw std::runtime_error("Unknown multi-server action");
 boost::json::array nodes;
 for(const auto& r:db.query("SELECT n.node_id::text,n.hostname,n.display_name,n.api_url,n.build,COALESCE(h.heartbeat_at::text,''),CASE WHEN h.heartbeat_at > now()-make_interval(secs=>c.heartbeat_timeout_seconds) THEN 'Online' WHEN h.node_id IS NULL THEN 'Not reported' ELSE 'Stale' END,COALESCE(h.started_at::text,'') FROM etlforge_cluster.node n CROSS JOIN etlforge_cluster.configuration c LEFT JOIN etlforge_cluster.service_heartbeat h ON h.node_id=n.node_id AND h.service='Scheduler' ORDER BY lower(n.display_name),n.node_id LIMIT 1000"))
  nodes.push_back(boost::json::object{{"nodeId",*r[0]},{"hostname",*r[1]},{"displayName",*r[2]},{"apiUrl",*r[3]},{"build",*r[4]},{"heartbeat",*r[5]},{"schedulerStatus",*r[6]},{"startedAt",*r[7]}});
 return boost::json::object{{"clusterId",*rows[0][0]},{"clusterName",*rows[0][1]},{"revision",std::stoll(*rows[0][2])},{"heartbeatTimeout",std::stoi(*rows[0][3])},{"activeActiveReady",false},{"mode","Preparation — active-active execution is not yet certified"},{"nodes",std::move(nodes)}};
}
}
