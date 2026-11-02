import java.net.URL;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.*;
import org.etlforge.integration.server.implementation.webservice.interchange.jobcontrol.client.JobControl;
import org.etlforge.integration.server.implementation.webservice.interchange.jobcontrol.client.JobControlServiceLocator;
import org.etlforge.integration.server.implementation.webservice.interchange.jobcontrol.client.WsRunOperationResult;
public final class StudioFileCopySmoke {
 static InterchangeDataTable table(String[] columns,String[]...values){InterchangeDataRow[] rows=new InterchangeDataRow[values.length];for(int i=0;i<values.length;i++)rows[i]=new InterchangeDataRow(values[i]);return new InterchangeDataTable(columns,rows);}
 public static void main(String[] args)throws Exception{
  String password=new String(Files.readAllBytes(Paths.get("C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt")),StandardCharsets.UTF_8).trim();StringBuilder hash=new StringBuilder();for(byte b:MessageDigest.getInstance("MD5").digest(password.getBytes(StandardCharsets.UTF_8)))hash.append(String.format("%02x",b&255));
  String project=UUID.randomUUID().toString(),source=UUID.randomUUID().toString(),target=UUID.randomUUID().toString(),operation=UUID.randomUUID().toString();
  String relative="integration-smoke/"+project;
  boolean local=args.length>0&&args[0].equals("local");
  Path root=local?Paths.get("C:/temp/etlforge-smoke-"+project):Paths.get("C:/ETL/Server/instances/dev/DataInterchange/TemporaryStorage").resolve(relative);
  Path input=root.resolve("input/input.csv"),output=local?Paths.get("C:/temp2/etlforge-smoke-"+project+"/result.csv"):root.resolve("output/result.csv");Files.createDirectories(input.getParent());Files.createDirectories(output.getParent());
  byte[] payload=("id,name\n1,ETLForge 日本\n2,"+project+"\n").getBytes(StandardCharsets.UTF_8);Files.write(input,payload);Files.write(root.resolve(".etlforge-integration-fixture"),"Owned ETLForge service integration fixture".getBytes(StandardCharsets.UTF_8));
  String manifest="{\"projectGuid\":\""+project+"\",\"operationGuid\":\""+operation+"\",\"fixtureRoot\":\""+root.toString().replace('\\','/')+"\"}";Files.write(Paths.get("C:/ETL/Reports/native-file-job-manifest.json"),manifest.getBytes(StandardCharsets.UTF_8));
  InterchangeDataHolder data=new InterchangeDataHolder();
  data.setInterchangeProjectsTab(table(new String[]{"InterchangeProject_ID","InterchangeProject_Name"},new String[]{project,"ETLForge File Copy Smoke "+project.substring(0,8)}));
  String sourcePath=local?input.getParent().toString():relative+"/input",targetPath=local?output.getParent().toString():relative+"/output",type=local?"16":"15";
  data.setSourcesTab(table(new String[]{"Source_ID","Source_Name","Source_Type_ID","Locator","File_Name","Login","Password","Driver","DriverType","ConnectString"},new String[]{source,"Smoke input",type,sourcePath,"input.csv",null,null,null,null,null}));
  data.setTargetsTab(table(new String[]{"Target_ID","Target_Name","Source_Type_ID","Locator","File_Name","Login","Password","Driver","DriverType","ConnectString","Target_Target_ID"},new String[]{target,"Smoke output",type,targetPath,"result.csv",null,null,null,null,null,null}));
  data.setOperationsTab(table(new String[]{"Operation_ID","Operation_Name","Operation_Type_ID","Source_ID","Target_ID","Ignore_History","Schedule_ID","Transformation_ID","Success_Operation_ID","Success_Message_ID","Failure_Operation_ID","Failure_Message_ID"},new String[]{operation,"Copy UTF-8 CSV","1",source,target,"1",null,null,null,null,null,null}));
  data.setOperationPipelines(table(new String[]{"Operation_ID","ActivityType_ID","Activity_ID"},new String[]{operation,"3",source},new String[]{operation,"4",target}));
  DeployInterchangeData deploy=new DeployInterchangeDataServiceLocator().getkonga_deploy_interchange_data(new URL("http://127.0.0.1:47100/axis/konga_deploy_interchange_data"));((org.apache.axis.client.Stub)deploy).setTimeout(30000);
  CDeployResults result=deploy.deployInterchangeData("Admin",hash.toString(),new InterchangeIdHolder(),data);if(result==null||result.getDeployStatus()>1)throw new AssertionError("Project deployment failed");
  System.out.println("PASS: Studio client deployed a new project, local-storage source/target and archive operation through the native API");
  JobControl jobs=new JobControlServiceLocator().getkonga_jobcontrol(new URL("http://127.0.0.1:47100/axis/konga_jobcontrol"));((org.apache.axis.client.Stub)jobs).setTimeout(30000);
  WsRunOperationResult job=jobs.runOperationInstanceMD5Password("Admin",hash.toString(),operation);if(job==null||job.getOperationInstanceId()<0)throw new AssertionError("Operation was not queued");System.out.println("Queued operation instance "+job.getOperationInstanceId());
  long deadline=System.nanoTime()+90000000000L;while(!Files.exists(output)&&System.nanoTime()<deadline)Thread.sleep(250);
  if(!Files.exists(output))throw new AssertionError("Queued operation did not produce its output file within 90 seconds");if(!Arrays.equals(payload,Files.readAllBytes(output)))throw new AssertionError("ETL output differs from input");
  if(!Files.exists(input)||!Arrays.equals(payload,Files.readAllBytes(input)))throw new AssertionError("Archive unexpectedly changed the source file");
  System.out.println("PASS: real queued x64 file-copy operation produced byte-identical UTF-8 CSV output");
 }
}
