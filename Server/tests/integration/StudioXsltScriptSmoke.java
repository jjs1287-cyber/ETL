import java.net.URL;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.*;
import org.etlforge.integration.server.implementation.webservice.interchange.jobcontrol.client.*;
public final class StudioXsltScriptSmoke {
 static InterchangeDataTable table(String[] columns,String[]... values){
  InterchangeDataRow[] rows=new InterchangeDataRow[values.length];for(int i=0;i<values.length;i++)rows[i]=new InterchangeDataRow(values[i]);return new InterchangeDataTable(columns,rows);
 }
 static String quote(String value){return "\""+value.replace("\\","\\\\").replace("\"","\\\"").replace("\r","\\r").replace("\n","\\n")+"\"";}
 public static void main(String[] args)throws Exception{
  String password=new String(Files.readAllBytes(Paths.get("C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt")),StandardCharsets.UTF_8).trim();
  StringBuilder hash=new StringBuilder();for(byte b:MessageDigest.getInstance("MD5").digest(password.getBytes(StandardCharsets.UTF_8)))hash.append(String.format("%02x",b&255));
  String project=UUID.randomUUID().toString(),script=UUID.randomUUID().toString(),operation=UUID.randomUUID().toString();
  String stylesheet=new String(Files.readAllBytes(Paths.get("C:/ETL/Samples/XSLT/ExpandEmbeddedXml/transform.xsl")),StandardCharsets.UTF_8);
  String input=new String(Files.readAllBytes(Paths.get("C:/ETL/Samples/XSLT/ExpandEmbeddedXml/input.xml")),StandardCharsets.UTF_8);
  String marker="ETLForge-XSLT-"+project;
  String content="<trans>output=RunXSLT("+quote(stylesheet)+","+quote(input)+");WriteToOperationLog("+quote(marker)+"+output[0]);</trans>";
  InterchangeDataHolder data=new InterchangeDataHolder();
  data.setInterchangeProjectsTab(table(new String[]{"InterchangeProject_ID","InterchangeProject_Name"},new String[]{project,"ETLForge XSLT Smoke "+project.substring(0,8)}));
  data.setScriptsTab(table(new String[]{"Script_ID","Script_Name","Script_Text"},new String[]{script,"XSLT embedded XML test",Base64.getEncoder().encodeToString(content.getBytes(StandardCharsets.UTF_8))}));
  data.setOperationsTab(table(new String[]{"Operation_ID","Operation_Name","Operation_Type_ID","Source_ID","Target_ID","Schedule_ID","Transformation_ID","Success_Operation_ID","Success_Message_ID","Failure_Operation_ID","Failure_Message_ID"},new String[]{operation,"XSLT script operation","8",null,null,null,null,null,null,null,null}));
  data.setOperationPipelines(table(new String[]{"Operation_ID","ActivityType_ID","Activity_ID"},new String[]{operation,"1",script}));
  DeployInterchangeData deploy=new DeployInterchangeDataServiceLocator().getkonga_deploy_interchange_data(new URL("http://127.0.0.1:47100/axis/konga_deploy_interchange_data"));
  ((org.apache.axis.client.Stub)deploy).setTimeout(30000);
  CDeployResults result=deploy.deployInterchangeData("Admin",hash.toString(),new InterchangeIdHolder(),data);
  if(result==null||result.getDeployStatus()>1)throw new AssertionError("XSLT script deploy failed");
  JobControl control=new JobControlServiceLocator().getkonga_jobcontrol(new URL("http://127.0.0.1:47100/axis/konga_jobcontrol"));
  ((org.apache.axis.client.Stub)control).setTimeout(30000);
  WsRunOperationResult run=control.runOperationInstanceMD5Password("Admin",hash.toString(),operation);
  if(run==null||run.getOperationInstanceId()<1)throw new AssertionError("XSLT script submission failed");
  Files.write(Paths.get("C:/ETL/Reports/xslt-script-integration-instance.txt"),(run.getOperationInstanceId()+"\n"+marker+"\n"+project).getBytes(StandardCharsets.UTF_8));
  System.out.println("PASS: deployed and submitted isolated RunXSLT script; instance="+run.getOperationInstanceId());
 }
}
