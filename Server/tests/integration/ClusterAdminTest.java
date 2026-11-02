import java.net.URL;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import javax.xml.namespace.QName;
import org.apache.axis.client.*;
import org.apache.axis.constants.*;
import org.apache.axis.encoding.XMLType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
public final class ClusterAdminTest {
 static final ObjectMapper json=new ObjectMapper();static String hash;static int checks;
 static void ok(boolean b,String label){if(!b)throw new AssertionError(label);checks++;System.out.println("PASS "+label);}
 static ObjectNode action(String value){ObjectNode r=json.createObjectNode();r.put("action",value);return r;}
 static JsonNode request(ObjectNode r,String user,String secret)throws Exception{Service service=new Service();service.getEngine().setOption("sendMultiRefs",Boolean.FALSE);Call call=(Call)service.createCall();call.setTargetEndpointAddress("http://127.0.0.1:47100/axis/konga_admin");call.setOperationName(new QName("urn:org.etlforge.integration.server.implementation.webservice.admin.Admin","auditRequest"));call.setOperationStyle(Style.WRAPPED);call.setOperationUse(Use.LITERAL);call.setUseSOAPAction(true);call.setSOAPActionURI("");call.setTimeout(30000);for(String f:new String[]{"in0","in1","in2"})call.addParameter(new QName("",f),XMLType.XSD_STRING,javax.xml.rpc.ParameterMode.IN);call.setReturnType(XMLType.XSD_STRING);return json.readTree((String)call.invoke(new Object[]{user,secret,r.toString()}));}
 static JsonNode request(ObjectNode r)throws Exception{return request(r,"Admin",hash);}
 static void init()throws Exception{String password=new String(Files.readAllBytes(Paths.get("C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt")),StandardCharsets.UTF_8).trim();StringBuilder h=new StringBuilder();for(byte b:MessageDigest.getInstance("MD5").digest(password.getBytes(StandardCharsets.UTF_8)))h.append(String.format("%02x",b&255));hash=h.toString();}
 static void denied(ObjectNode r,String label)throws Exception{boolean denied=false;try{request(r);}catch(java.rmi.RemoteException e){denied=true;}ok(denied,label);}
 public static void main(String[] args)throws Exception{
  init();JsonNode original=request(action("cluster.settings"));ok(!original.path("clusterId").asText().isEmpty(),"native API returns stable cluster ID");ok(!original.path("activeActiveReady").asBoolean(),"uncertified active-active mode is clearly reported");ok(original.path("nodes").size()>=1,"physical laptop registered by Scheduler");
  ObjectNode bootstrap=action("cluster.bootstrap");denied(bootstrap,"cluster bootstrap requires explicit trusted-network risk acceptance");bootstrap.put("acceptTrustedNetworkBootstrapRisk",true);JsonNode packageData=request(bootstrap);ok(packageData.path("format").asInt()==1,"cluster bootstrap format is versioned");ok(packageData.path("keyVaultMasterKey").asText().length()>=32,"cluster bootstrap includes the existing vault key");ok(packageData.path("sharedConfiguration").path("TranDb").isTextual(),"cluster bootstrap includes shared database topology");
  ObjectNode save=action("cluster.save");save.put("revision",original.path("revision").asLong());save.put("clusterName","Multi-server verification \u4e16\u754c");save.put("heartbeatTimeout",60);
  try{
   JsonNode updated=request(save);ok(updated.path("clusterName").asText().equals(save.path("clusterName").asText()),"UTF-8 group name round trips");denied(save,"stale configuration revision rejected");
   save.put("revision",updated.path("revision").asLong());save.put("heartbeatTimeout",1);denied(save,"invalid heartbeat timeout rejected");save.put("heartbeatTimeout",60);save.put("clusterName","");denied(save,"empty group name rejected");
   JsonNode node=updated.path("nodes").get(0);ObjectNode n=action("cluster.node.save");n.put("nodeId",node.path("nodeId").asText());n.put("displayName",node.path("displayName").asText());n.put("apiUrl","http://user:secret@example.com/path");denied(n,"credentials and path rejected in advertised API origin");
   n.put("apiUrl",node.path("apiUrl").asText());JsonNode after=request(n);ok(after.path("nodes").size()==updated.path("nodes").size(),"node address metadata saves without duplicating nodes");
   boolean denied=false;try{request(action("cluster.settings"),"invalid-cluster-user","00000000000000000000000000000000");}catch(java.rmi.RemoteException e){denied=true;}ok(denied,"unauthenticated cluster administration denied");
   Path account=Paths.get("C:/ETL/Server/private/sftp-test-user.json");if(Files.exists(account)){JsonNode user=json.readTree(Files.readAllBytes(account));denied=false;try{request(action("cluster.settings"),user.path("name").asText(),user.path("hash").asText());}catch(java.rmi.RemoteException e){denied=true;}ok(denied,"non-administrator cluster access denied");}
  }finally{JsonNode current=request(action("cluster.settings"));ObjectNode restore=action("cluster.save");restore.put("revision",current.path("revision").asLong());restore.put("clusterName",original.path("clusterName").asText());restore.put("heartbeatTimeout",original.path("heartbeatTimeout").asInt());request(restore);}
  System.out.println("Passed "+checks+" native multi-server API checks");
 }
}
