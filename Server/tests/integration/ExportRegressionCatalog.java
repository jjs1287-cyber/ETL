import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.data.entity.*;
import org.etlforge.integration.data.entity.id.*;
import org.etlforge.integration.data.entity.operation.*;
import org.etlforge.integration.data.entity.operation.pipeline.*;
import org.etlforge.integration.data.location.*;
import org.etlforge.integration.data.structure.*;
import org.etlforge.integration.data.Password;
import org.etlforge.util.persist.XmlPersistor;
import org.etlforge.integration.server.implementation.webservice.impl.common.property_keys.*;
import com.etlforge.integration.data.download.entity.*;
import com.etlforge.integration.data.download.structure.TextStructureFactory;
public class ExportRegressionCatalog {
 static final Path OUT=Paths.get("C:/ETL/Reports/regression-suite/catalog");
 static Connection db;static Map<String,Map<String,String>> guids=new HashMap<>();
 static Map<String,IntegrationEntity> entities=new LinkedHashMap<>();static List<String> catalog=new ArrayList<>();
 static List<Map<String,String>> rows(String sql)throws Exception {List<Map<String,String>> all=new ArrayList<>();try(Statement s=db.createStatement();ResultSet r=s.executeQuery(sql)){while(r.next()){Map<String,String> m=new LinkedHashMap<>();for(int n=1;n<=r.getMetaData().getColumnCount();n++)m.put(r.getMetaData().getColumnLabel(n).toLowerCase(),r.getString(n));all.add(m);}}return all;}
 static String guid(String type,String id){return id==null?null:guids.getOrDefault(type,Collections.emptyMap()).get(id);}
 static StringKeyValuePairs properties(EntityType type,Map<String,String> raw)throws Exception {
  StringKeyValuePairs p=new StringKeyValuePairs();DeployKeyMappings keys=null;try{keys=ClientServerDeployKeyMappings.getServerClientKeyMappings(type);}catch(NullPointerException missing){}
  for(Map.Entry<String,String> e:raw.entrySet()){if(e.getValue()==null)continue;ServerPropertyKey key=keys==null?null:keys.get(e.getKey());p.put(key==null?e.getKey():key.getClientKeyName(),e.getValue());}return p;
 }
 static Map<String,String> raw(String noun,Map<String,String> row)throws Exception {
  Map<String,String> m=new LinkedHashMap<>(row);String id=m.get(noun+"_id");
  if(table(noun+"propertiestab"))for(Map<String,String> p:rows("select * from "+noun+"propertiestab where "+noun+"_id="+id))m.put(p.get("keyname"),p.get("keyvalue"));
  Map<String,String> refs=new HashMap<>();refs.put("source_id","sources");refs.put("source_sourceid","sources");refs.put("target_id","targets");refs.put("target_targetid","targets");refs.put("transformation_id","transformations");refs.put("source_text_doc","documents");refs.put("target_text_doc","documents");refs.put("schedule_id","schedules");refs.put("success_operation_id","operations");refs.put("failure_operation_id","operations");refs.put("success_message_id","messages");refs.put("failure_message_id","messages");
  for(Map.Entry<String,String> ref:refs.entrySet())if(m.containsKey(ref.getKey()))m.put(ref.getKey(),guid(ref.getValue(),m.get(ref.getKey())));
  m.put(noun+"_id",guid(noun+"s",id));return m;
 }
 static boolean table(String name)throws Exception {return !rows("select 1 from information_schema.tables where table_schema='public' and table_name='"+name+"'").isEmpty();}
 static void add(IntegrationEntity e,String noun,String id)throws Exception {if(e instanceof ProjectVariable)e.setName("T"+id+"_"+e.getName());else e.setName("T"+id+" - "+e.getName());e.setDeployed(false);e.setDeployDirty(true);entities.put(noun+":"+id,e);}
 static IntegrationEntity get(String noun,String id){return entities.get(noun+":"+id);}
 static String name(Map<String,String> r,String noun){return r.get(noun+"_name");}
 static void load(String noun,EntityType type)throws Exception {
  for(Map<String,String> r:rows("select * from "+(noun.equals("schedule")?"scheduletab":noun+"stab")+" order by "+noun+"_id")){
   String id=r.get(noun+"_id");Map<String,String> m=raw(noun,r);StringKeyValuePairs p=properties(type,m);IntegrationEntity e;
   if(type==EntityType.Source||type==EntityType.Target){p.put(noun+"_id",guid(noun+"s",id));p.put(noun+"_name",name(r,noun));e=type==EntityType.Source?new SourceFactory().createItem(p):new TargetFactory().createItem(p);DataLocation loc=type==EntityType.Source?((Source)e).getLocation():((Target)e).getLocation();
    for(String k:m.keySet())if(k.startsWith("compression_")||k.startsWith("pgp_")||k.startsWith("s3_")||k.startsWith("sftp_"))loc.setProperty(k,m.get(k));
    if(m.get("password")!=null&&!m.get("password").isEmpty())loc.setProperty("password",Password.encrypt(m.get("password")).encrypted());
   }else if(type==EntityType.Script){Script s=new Script(new ScriptId(guid("scripts",id)),name(r,noun));s.setScript(r.get("script_text"));e=s;
   }else if(type==EntityType.XsltTransform){XsltTransform x=new XsltTransform(new XsltTransformId(guid("xslttransforms",id)),name(r,noun));x.setXslt(r.get("xslt"));e=x;
   }else {if(type==EntityType.WebServiceCall&&p.get("httppassword")!=null&&!p.get("httppassword").isEmpty())p.put("httppassword",Password.encrypt(p.get("httppassword")).encrypted());e=new DefaultEntityFactory(type).createItem(p);}
   add(e,noun,id);
   if(e instanceof TextDocument){List<StringKeyValuePairs> defs=new ArrayList<>();for(Map<String,String>d:rows("select * from document_definitionstab where document_id="+id+" order by sequence")){d.put("document_id",guid("documents",id));StringKeyValuePairs kv=new StringKeyValuePairs();for(String k:d.keySet())if(d.get(k)!=null)kv.put(k,d.get(k));defs.add(kv);}((TextDocument)e).setStructure(new TextStructureFactory((TextDocument)e).createTextStructure(defs,Collections.emptyList()));}
  }
 }
 static DataStructure structure(Map<String,String> r,boolean input)throws Exception {String side=input?"source":"target";int type=Integer.parseInt(r.get(side+"_type_id"));
  if(type==1||type==5){IntegrationEntity doc=get("document",r.get(side+"_text_doc"));return doc instanceof TextDocument?((TextDocument)doc).getDataStructure():new NoStructure();}
  if(type==4){XmlStructure x=new XmlStructure();x.setFilePath(r.get(side+"_xml"));x.setRootNodeName(r.get(input?"sourcedtd_root":"targetdtd_root"));return x;}
  if(type==17)return new JsonStructure(r.get(side+"_xml"),r.getOrDefault(input?"sourcedtd_root":"targetdtd_root","json"));
  return new NoStructure();
 }
 public static void main(String[]args)throws Exception {
  Files.createDirectories(OUT);Class.forName("org.postgresql.Driver");db=DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/etlforge_dev_tran","etlforge_dev",NativeJavaScriptMappingTest.databasePassword());db.setReadOnly(true);
  for(Map<String,String> t:rows("select tablename from pg_tables where schemaname='public' and tablename like 'guidsfor%tab'")){String tab=t.get("tablename");Map<String,String> map=new HashMap<>();for(Map<String,String> r:rows("select * from "+tab))map.put(r.get("item_id"),r.get("guid"));guids.put(tab.substring(8,tab.length()-3),map);}
  load("source",EntityType.Source);load("target",EntityType.Target);load("script",EntityType.Script);load("document",EntityType.Document);load("xslttransform",EntityType.XsltTransform);load("projectvariable",EntityType.ProjectVariable);load("schedule",EntityType.Schedule);
  for(Map<String,String> r:rows("select * from transformationstab order by transformation_id")){String id=r.get("transformation_id");if(guid("transformations",id)==null)continue;Transformation tr=new Transformation(new TransformationId(guid("transformations",id)),r.get("transformation_name"));tr.setPropertyValues(properties(EntityType.Transformation,raw("transformation",r)));tr.setInputStructure(structure(r,true));tr.setOutputStructure(structure(r,false));for(Map<String,String>d:rows("select * from transformation_definitionstab where transformation_id="+id+" order by trandiffid"))tr.addMapping(d.get("target_field_id")+"\t"+d.get("transformation"));add(tr,"transformation",id);}
  OperationType.load(ArchiveOperationType.TYPE,ScriptOperationType.TYPE,TransformationOperationType.TYPE,HostedWebServiceOperationType.TYPE,WebServiceOperationType.TYPE,XsltOperationType.TYPE);
  for(Map<String,String> r:rows("select * from operationstab order by operation_id")){String id=r.get("operation_id");Operation op=new Operation(new OperationId(guid("operations",id)),r.get("operation_name"));op.setPropertyValues(properties(EntityType.Operation,raw("operation",r)));op.setOperationType(OperationType.fromId(Integer.parseInt(r.get("operation_type_id"))));List<PipelineActivity> activities=new ArrayList<>();
   for(Map<String,String>a:rows("select * from operationpipelineactivitiestab where operation_id="+id+" order by operationactivity_id")){int t=Integer.parseInt(a.get("activitytype_id"));String noun=t==1?"script":t==3?"source":t==4?"target":t==5?"transformation":t==8?"xslttransform":null;if(noun==null)continue;EntityActivity activity=(EntityActivity)PipelineActivityType.fromId(t).createEmptyActivity();IntegrationEntity content=get(noun,a.get("activity_id"));if(content==null)throw new Exception("Missing pipeline content for operation "+id);activity.setContent(content);activities.add(activity);}
   op.setPipeline(new OperationPipeline(op.getOperationType(),activities));op.setProperty("schedule_id",null);op.setProperty("scheduleEnabled","false");add(op,"operation",id);
   String project="";for(Map<String,String> p:rows("select p.interchangeproject_id,p.interchangeproject_name from interchangeprojectoperationstab i join interchangeprojectstab p using(interchangeproject_id) where i.item_id="+id+" order by p.interchangeproject_id desc limit 1"))project=p.get("interchangeproject_id")+"|"+p.get("interchangeproject_name");
   catalog.add(id+"|"+op.getID()+"|"+r.get("operation_name")+"|"+project);
  }
  XmlPersistor index=XmlPersistor.newRoot("Project");index.putString("name","Regression Catalog");index.putString("projectId",UUID.randomUUID().toString());Map<EntityType,org.etlforge.util.persist.Persistor> types=new HashMap<>();
  for(IntegrationEntity e:entities.values()){Path dir=OUT.resolve("Data/"+e.getEntityType().name());Files.createDirectories(dir);XmlPersistor x=XmlPersistor.newRoot("Entity");x.putString("type",e.getEntityType().name());e.writeTo(x);x.store(dir.resolve(e.getID()+".xml").toFile());org.etlforge.util.persist.Persistor section=types.get(e.getEntityType());if(section==null){section=index.createChild("EntityType");section.putString("name",e.getEntityType().name());types.put(e.getEntityType(),section);}org.etlforge.util.persist.Persistor item=section.createChild("Entity");item.putString("entityId",e.getID().toString());item.putString("name",e.getName());}
  index.store(OUT.resolve("Regression Catalog.xml").toFile());Properties props=new Properties();props.setProperty("structure-file","Regression Catalog.xml");props.setProperty("project-id",index.getString("projectId"));props.setProperty("saved-version","42.0.0.0");props.setProperty("saved-edition","enterprise");props.setProperty("requires_version","5.4.0.1");try(OutputStream stream=Files.newOutputStream(OUT.resolve("Regression Catalog.jip"))){props.store(stream,"Regression test catalog");}
  Files.write(OUT.getParent().resolve("operations.txt"),catalog,StandardCharsets.UTF_8);System.out.println("Exported "+catalog.size()+" operations and "+entities.size()+" entities");db.close();
 }
}






