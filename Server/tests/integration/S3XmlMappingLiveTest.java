import java.net.URL;
import java.nio.file.*;
import java.util.*;
import java.nio.charset.StandardCharsets;
import com.fasterxml.jackson.databind.*;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.*;
import org.etlforge.integration.server.implementation.webservice.interchange.jobcontrol.client.*;
public final class S3XmlMappingLiveTest {
 static InterchangeDataTable table(String[] names,String[]...rows){return StudioFileCopySmoke.table(names,rows);}
 static String id(){return UUID.randomUUID().toString();}
 static String expression(String text){return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));}
 public static void main(String[]args)throws Exception{
  LocalVariablesRuntimeTest.init();String hash=LocalVariablesRuntimeTest.hash;JsonNode config=new ObjectMapper().readTree(Files.readAllBytes(Paths.get("C:/ETL/Server/private/s3-test-credentials.json")));
  String project=id(),source=id(),target=id(),transformation=id(),operation=id(),schema="S3Utf8Verification.xsd";
  NativeJavaScriptMappingTest.hash=hash;NativeJavaScriptMappingTest.upload(schema,"<xs:schema xmlns:xs='http://www.w3.org/2001/XMLSchema'><xs:element name='rows'><xs:complexType><xs:sequence><xs:element name='row' minOccurs='0' maxOccurs='unbounded'><xs:complexType><xs:sequence><xs:element name='id' type='xs:string'/><xs:element name='name' type='xs:string'/></xs:sequence></xs:complexType></xs:element></xs:sequence></xs:complexType></xs:element></xs:schema>");
  InterchangeDataHolder d=new InterchangeDataHolder();d.setInterchangeProjectsTab(table(new String[]{"InterchangeProject_ID","InterchangeProject_Name"},new String[]{project,"S3 XML UTF-8 verification "+project.substring(0,8)}));
  d.setSourcesTab(table(new String[]{"Source_ID","Source_Name","Source_Type_ID","Locator","Path","File_Name","Login","Password","Driver","DriverType","ConnectString"},new String[]{source,"S3 UTF-16 XML source","18",config.path("bucket").asText(),config.path("prefix").asText()+"xml/","input.xml",null,null,null,null,null}));
  d.setTargetsTab(table(new String[]{"Target_ID","Target_Name","Source_Type_ID","Locator","Path","File_Name","Login","Password","Driver","DriverType","ConnectString","Target_Target_ID"},new String[]{target,"S3 UTF-8 mapped target","18",config.path("bucket").asText(),config.path("prefix").asText()+"xml/","output.xml",null,null,null,null,null,"-1"}));
  d.setTransformationsTab_New(table(new String[]{"transformation_id","transformation_name","source_type_id","target_type_id","source_text_doc","target_text_doc","source_sourceid","target_targetid","source_xml","target_xml","natureofsource","natureoftarget","sourcedtd_root","targetdtd_root","sourcejmsmessageid","targetjmsmessageid"},new String[]{transformation,"S3 XML identity","4","4",null,null,null,null,schema,schema,"1","1","rows","rows",null,null}));
  d.setTransformation_DefinitionsTab(table(new String[]{"transformation_id","target_field_id","transformation"},new String[]{transformation,"rows$row.id$",expression("<trans>rows$row.id$</trans>")},new String[]{transformation,"rows$row.name$",expression("<trans>rows$row.name$</trans>")}));
  d.setOperationsTab(table(new String[]{"Operation_ID","Operation_Name","Operation_Type_ID","Source_ID","Target_ID","Ignore_History","Schedule_ID","Transformation_ID","Success_Operation_ID","Success_Message_ID","Failure_Operation_ID","Failure_Message_ID"},new String[]{operation,"S3 UTF-16 to UTF-8 XML transformation","3",source,target,"1",null,transformation,null,null,null,null}));
  d.setOperationPipelines(table(new String[]{"Operation_ID","ActivityType_ID","Activity_ID"},new String[]{operation,"3",source},new String[]{operation,"5",transformation},new String[]{operation,"4",target}));
  ArrayList<String[]> props=new ArrayList<>();for(String[] endpoint:new String[][]{{"2",source},{"3",target}}){props.add(new String[]{endpoint[0],endpoint[1],"s3_region",config.path("region").asText()});props.add(new String[]{endpoint[0],endpoint[1],"s3_access_key_protected",config.path("accessKeyProtected").asText()});props.add(new String[]{endpoint[0],endpoint[1],"s3_secret_key_protected",config.path("secretKeyProtected").asText()});}d.setEntityProperties(table(new String[]{"EntityType_ID","Entity_ID","KeyName","KeyValue"},props.toArray(new String[0][])));
  CDeployResults result=new DeployInterchangeDataServiceLocator().getkonga_deploy_interchange_data(new URL("http://127.0.0.1:47100/axis/konga_deploy_interchange_data")).deployInterchangeData("Admin",hash,new InterchangeIdHolder(),d);if(result.getDeployStatus()>1)throw new AssertionError("XML S3 deployment failed");System.out.println("PASS deploy XML transformation with S3 source and target");
  JobControl jobs=new JobControlServiceLocator().getkonga_jobcontrol(new URL("http://127.0.0.1:47100/axis/konga_jobcontrol"));long instance=jobs.runOperationInstanceMD5Password("Admin",hash,operation).getOperationInstanceId();for(int i=0;i<1200;i++){String state=NativeJavaScriptMappingTest.status(instance);if(state.equals("3")){System.out.println("PASS S3 XML transformation operation instance "+instance);return;}if(!state.isEmpty()&&!state.equals("0")&&!state.equals("2"))throw new AssertionError("S3 XML mapping failed instance "+instance+" status "+state);Thread.sleep(100);}throw new AssertionError("XML operation timed out");
 }
}
