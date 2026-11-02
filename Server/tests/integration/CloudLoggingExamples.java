import java.net.URL;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.*;
import org.etlforge.integration.server.implementation.webservice.interchange.jobcontrol.client.*;
public final class CloudLoggingExamples {
 static final String PROJECT="42420000-0000-4000-8000-000000000001",RECEIVER="42420000-0000-4000-8000-000000000002",PRODUCER="42420000-0000-4000-8000-000000000003",TARGET="42420000-0000-4000-8000-000000000004",ENDPOINT="42420000-0000-4000-8000-000000000005",SCRIPT1="42420000-0000-4000-8000-000000000006",SCRIPT2="42420000-0000-4000-8000-000000000007";
 static InterchangeDataTable table(String[] columns,String[]...values){InterchangeDataRow[] rows=new InterchangeDataRow[values.length];for(int i=0;i<rows.length;i++)rows[i]=new InterchangeDataRow(values[i]);return new InterchangeDataTable(columns,rows);}
 public static void main(String[] args)throws Exception {
  String password=new String(Files.readAllBytes(Paths.get("C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt")),StandardCharsets.UTF_8).trim();StringBuilder digest=new StringBuilder();for(byte b:MessageDigest.getInstance("MD5").digest(password.getBytes(StandardCharsets.UTF_8)))digest.append(String.format("%02x",b&255));String hash=digest.toString();
  if(args.length>0 && args[0].equals("run")){JobControl jobs=new JobControlServiceLocator().getkonga_jobcontrol(new URL("http://127.0.0.1:47100/axis/konga_jobcontrol"));System.out.println("Queued cloud logging example instance="+jobs.runOperationInstanceMD5Password("Admin",hash,PRODUCER).getOperationInstanceId());return;}
  Path root=Paths.get("C:/ETL/Samples/CloudLogging/received");Files.createDirectories(root);
  InterchangeDataHolder data=new InterchangeDataHolder();data.setInterchangeProjectsTab(table(new String[]{"InterchangeProject_ID","InterchangeProject_Name"},new String[]{PROJECT,"Cloud Logging Examples"}));
  data.setTargetsTab(table(new String[]{"Target_ID","Target_Name","Source_Type_ID","Locator","File_Name","Login","Password","Driver","DriverType","ConnectString","Target_Target_ID"},new String[]{TARGET,"Cloud log JSON payloads","16",root.toString(),"cloud-log-[unique].json",null,null,null,null,null,null}));
  String receiver="<trans>WriteFile(\"t."+TARGET+"\",$CloudLogRequest);FlushFile(\"t."+TARGET+"\");</trans>",producer="<trans>WriteToOperationLog(\"Cloud logging example: caf\u00e9 \u20ac \u4e2d\u6587 \ud83d\ude00, \\\"quoted\\\" text\");</trans>";
  data.setScriptsTab(table(new String[]{"Script_ID","Script_Name","Script_Text"},new String[]{SCRIPT1,"Save cloud log JSON",Base64.getEncoder().encodeToString(receiver.getBytes(StandardCharsets.UTF_8))},new String[]{SCRIPT2,"Generate cloud log message",Base64.getEncoder().encodeToString(producer.getBytes(StandardCharsets.UTF_8))}));
  data.setOperationsTab(table(new String[]{"Operation_ID","Operation_Name","Operation_Type_ID","Source_ID","Target_ID","Ignore_History","Schedule_ID","Transformation_ID","Success_Operation_ID","Success_Message_ID","Failure_Operation_ID","Failure_Message_ID"},new String[]{RECEIVER,"Cloud log HTTP receiver","8",null,null,"0",null,null,null,null,null,null},new String[]{PRODUCER,"Cloud logging example operation","8",null,null,"0",null,null,null,null,null,null}));
  data.setOperationPipelines(table(new String[]{"Operation_ID","ActivityType_ID","Activity_ID"},new String[]{RECEIVER,"1",SCRIPT1},new String[]{PRODUCER,"1",SCRIPT2}));
  data.setHttpEndpointsTab(table(new String[]{"httpendpoint_id","httpendpoint_name","endpoint_url","response_type","request_variable","response_content_type"},new String[]{ENDPOINT,"Cloud log receiver","etlforge/cloud-logging","0","CloudLogRequest","application/json"}));
  data.setHttpEndpointOperationsTab(table(new String[]{"httpendpoint_id","operation_id","synchronous","final_target_as_response","http_verb"},new String[]{ENDPOINT,RECEIVER,"1","0","POST"}));
  DeployInterchangeData deploy=new DeployInterchangeDataServiceLocator().getkonga_deploy_interchange_data(new URL("http://127.0.0.1:47100/axis/konga_deploy_interchange_data"));CDeployResults result=deploy.deployInterchangeData("Admin",hash,new InterchangeIdHolder(),data);if(result==null||result.getDeployStatus()>1)throw new AssertionError("Cloud logging example deployment failed");
  System.out.println("PASS: Cloud Logging Examples project deployed with an HTTP receiver operation and a sample producer operation.");
 }
}
