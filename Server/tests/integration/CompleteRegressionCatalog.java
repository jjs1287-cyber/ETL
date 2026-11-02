import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;
import java.nio.charset.StandardCharsets;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.etlforge.integration.data.entity.*;
import org.etlforge.integration.data.entity.id.*;
import org.etlforge.integration.data.entity.webservicecall.WsdlFile;
import org.etlforge.integration.data.entity.httpendpoint.*;
import org.etlforge.integration.data.entity.operation.*;
import org.etlforge.integration.http.HttpVerb;
import org.etlforge.integration.data.Password;
import org.etlforge.util.persist.*;
public final class CompleteRegressionCatalog extends ExportRegressionCatalog {
 static final Path STAGE=Paths.get("C:/ETL/Reports/regression-suite/Test Operations");
 static void save(IntegrationEntity e)throws Exception{e.setDeployed(true);e.setDeployDirty(false);XmlPersistor x=XmlPersistor.newRoot("Entity");x.putString("type",e.getEntityType().name());e.writeTo(x);Path p=STAGE.resolve("Data/"+e.getEntityType()+"/"+e.getID()+".xml");Files.createDirectories(p.getParent());x.store(p.toFile());}
 public static void main(String[]args)throws Exception{
  Class.forName("org.postgresql.Driver");db=DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/etlforge_dev_tran","etlforge_dev",NativeJavaScriptMappingTest.databasePassword());db.setReadOnly(true);
  for(Map<String,String> t:rows("select tablename from pg_tables where schemaname='public' and tablename like 'guidsfor%tab'")){String tab=t.get("tablename");Map<String,String> map=new HashMap<>();for(Map<String,String> r:rows("select * from "+tab))map.put(r.get("item_id"),r.get("guid"));guids.put(tab.substring(8,tab.length()-3),map);}
  load("webservice",EntityType.WebServiceCall);load("httpendpoint",EntityType.HttpEndpoint);load("salesforceconnector",EntityType.SalesforceConnector);
  for(Map<String,String> r:rows("select * from webservicestab")){
   WebServiceCall method=(WebServiceCall)get("webservice",r.get("webservice_id"));for(Map<String,String> w:rows("select * from wsdltab where wsdl_id="+r.get("wsdl_id"))){String folder=w.get("wsdlpath").replace('\\','/');WsdlFile file=new WsdlFile(folder.endsWith(w.get("wsdlname"))?folder:folder+"/"+w.get("wsdlname"));for(String k:w.keySet())if(w.get(k)!=null)file.put(k,w.get(k));method.setWsdlFile(file);}
   if(r.get("httppassword")!=null&&!r.get("httppassword").isEmpty())method.setHttpPassword(com.google.common.base.Optional.of(Password.encrypt(r.get("httppassword"))));
  }
  for(Map<String,String> r:rows("select * from httpendpointstab")){HttpEndpoint endpoint=(HttpEndpoint)get("httpendpoint",r.get("httpendpoint_id"));TriggeredOperations operations=new TriggeredOperations();for(Map<String,String> op:rows("select * from httpendpointoperationstab where httpendpoint_id="+r.get("httpendpoint_id"))){String id=guid("operations",op.get("operation_id"));if(id!=null)operations.addOperation(new TriggeredOperation(new OperationId(id),op.get("http_verb")==null||op.get("http_verb").isEmpty()?null:HttpVerb.valueOf(op.get("http_verb")),"1".equals(op.get("synchronous"))?OperationRunMode.SYNCHRONOUS:OperationRunMode.ASYNCHRONOUS,"1".equals(op.get("final_target_as_response"))));}endpoint.setTriggeredOperations(operations);}
  XmlPersistor index=XmlPersistor.load(STAGE.resolve("New Project.xml").toFile());ObjectMapper json=new ObjectMapper();ObjectNode groups=(ObjectNode)json.readTree(Files.readAllBytes(STAGE.getParent().resolve("groups.json")));int count=0;
  for(IntegrationEntity e:entities.values()){
   Path destination=STAGE.resolve("Data/"+e.getEntityType()+"/"+e.getID()+".xml");if(Files.exists(destination))continue;save(e);Persistor section=null;for(Persistor child:index.getChildren("EntityType"))if(e.getEntityType().name().equals(child.getString("name")))section=child;if(section==null){section=index.createChild("EntityType");section.putString("name",e.getEntityType().name());}Persistor item=section.createChild("Entity");item.putString("entityId",e.getID().toString());item.putString("name",e.getName());groups.put(e.getID().toString(),"Historical fixtures");count++;
  }
  index.store(STAGE.resolve("New Project.xml").toFile());json.writeValue(STAGE.getParent().resolve("groups.json").toFile(),groups);
  List<Map<String,String>> links=new ArrayList<>();for(Map<String,String> r:rows("select operation_id,activity_id,operationactivity_id from operationpipelineactivitiestab where activitytype_id=6 and operation_id<=234 order by operation_id,operationactivity_id")){Map<String,String> link=new LinkedHashMap<>();link.put("operation",guid("operations",r.get("operation_id")));link.put("method",guid("webservices",r.get("activity_id")));link.put("position",rows("select count(*) AS n from operationpipelineactivitiestab where operation_id="+r.get("operation_id")+" and operationactivity_id<"+r.get("operationactivity_id")).get(0).get("n"));links.add(link);}json.writeValue(STAGE.getParent().resolve("historical-webservice-links.json").toFile(),links);
  System.out.println("Completed "+count+" historical web-service, HTTP endpoint and Salesforce metadata objects");db.close();
 }
}


