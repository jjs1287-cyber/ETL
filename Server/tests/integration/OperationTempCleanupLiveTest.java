import java.net.URL;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.*;
import org.etlforge.integration.server.implementation.webservice.interchange.jobcontrol.client.*;

public final class OperationTempCleanupLiveTest {
 static InterchangeDataTable table(String[] columns,String[]...values){InterchangeDataRow[] rows=new InterchangeDataRow[values.length];for(int i=0;i<values.length;i++)rows[i]=new InterchangeDataRow(values[i]);return new InterchangeDataTable(columns,rows);}
 public static void main(String[] args)throws Exception {
  String password=new String(Files.readAllBytes(Paths.get("C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt")),StandardCharsets.UTF_8).trim();
  StringBuilder md5=new StringBuilder();for(byte b:MessageDigest.getInstance("MD5").digest(password.getBytes(StandardCharsets.UTF_8)))md5.append(String.format("%02x",b&255));String hash=md5.toString();password=null;
  String project=UUID.randomUUID().toString(),source=UUID.randomUUID().toString();String[] scripts={UUID.randomUUID().toString(),UUID.randomUUID().toString()},operations={UUID.randomUUID().toString(),UUID.randomUUID().toString()};
  Path root=Paths.get("C:/ETL/Server/build/admin-tests/cleanup-live-"+project);Files.createDirectory(root);
  byte[] original="cleanup source caf\u00e9 \u4e2d\u6587 \ud83d\ude80".getBytes(StandardCharsets.UTF_8);Files.write(root.resolve("input.txt"),original);
  InterchangeDataHolder data=new InterchangeDataHolder();
  data.setInterchangeProjectsTab(table(new String[]{"InterchangeProject_ID","InterchangeProject_Name"},new String[]{project,"ETLForge cleanup verification "+project.substring(0,8)}));
  data.setSourcesTab(table(new String[]{"Source_ID","Source_Name","Source_Type_ID","Locator","File_Name","Login","Password","Driver","DriverType","ConnectString"},new String[]{source,"Cleanup input","16",root.toString(),"input.txt",null,null,null,null,null}));
  String[][] scriptRows=new String[2][],opRows=new String[2][],pipeline=new String[2][];
  for(int i=0;i<2;i++) {
   String code="<trans>value=ReadFile(\"s."+source+"\");"+(i==0?"WriteToOperationLog(\"cleanup verification caf\u00e9 \u4e2d\u6587\");":"RaiseError(\"cleanup intentional failure\");")+"</trans>";
   scriptRows[i]=new String[]{scripts[i],i==0?"Cleanup success script":"Cleanup failure script",Base64.getEncoder().encodeToString(code.getBytes(StandardCharsets.UTF_8))};
   opRows[i]=new String[]{operations[i],i==0?"Cleanup success":"Cleanup expected failure","8",null,null,null,null,null,null,null,null};
   pipeline[i]=new String[]{operations[i],"1",scripts[i]};
  }
  data.setScriptsTab(table(new String[]{"Script_ID","Script_Name","Script_Text"},scriptRows));
  data.setOperationsTab(table(new String[]{"Operation_ID","Operation_Name","Operation_Type_ID","Source_ID","Target_ID","Schedule_ID","Transformation_ID","Success_Operation_ID","Success_Message_ID","Failure_Operation_ID","Failure_Message_ID"},opRows));
  data.setOperationPipelines(table(new String[]{"Operation_ID","ActivityType_ID","Activity_ID"},pipeline));
  DeployInterchangeData deploy=new DeployInterchangeDataServiceLocator().getkonga_deploy_interchange_data(new URL("http://127.0.0.1:47100/axis/konga_deploy_interchange_data"));
  CDeployResults result=deploy.deployInterchangeData("Admin",hash,new InterchangeIdHolder(),data);if(result==null||result.getDeployStatus()>1)throw new AssertionError("Cleanup fixture deploy failed");
  JobControl jobs=new JobControlServiceLocator().getkonga_jobcontrol(new URL("http://127.0.0.1:47100/axis/konga_jobcontrol"));
  List<String> manifest=new ArrayList<>();manifest.add("projectGuid="+project);manifest.add("successGuid="+operations[0]);manifest.add("failureGuid="+operations[1]);manifest.add("root="+root);
  for(int i=0;i<12;i++) {
   int type=i==11?1:0;WsRunOperationResult job=jobs.runOperationInstanceMD5Password("Admin",hash,operations[type]);
   if(job==null||job.getOperationInstanceId()<1)throw new AssertionError("Cleanup operation not queued");
   manifest.add((type==0?"successInstance=":"failureInstance=")+job.getOperationInstanceId());
  }
  Files.write(Paths.get("C:/ETL/Reports/operation-cleanup-live-manifest.txt"),manifest,StandardCharsets.UTF_8);
  if(!Arrays.equals(original,Files.readAllBytes(root.resolve("input.txt")) ))throw new AssertionError("Original source changed");
  System.out.println("PASS: queued 11 successful operations and one intentional failure using the real Studio client and a read-only UTF-8 local source.");
 }
}
