import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.*;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.*;
import org.etlforge.integration.server.implementation.webservice.interchange.jobcontrol.client.*;

/** Probe an existing mapping using a copied input and an isolated output. */
public final class StudioXmlTransformProbe {
 public static void main(String[] args) throws Exception {
  if(args.length!=2)throw new IllegalArgumentException("Expected transformation GUID and input XML path");
  String transformation=UUID.fromString(args[0]).toString();
  Path original=Paths.get(args[1]);byte[] input=Files.readAllBytes(original);
  String project=UUID.randomUUID().toString(),source=UUID.randomUUID().toString(),target=UUID.randomUUID().toString(),operation=UUID.randomUUID().toString();
  Path root=Paths.get("C:/ETL/Server/instances/dev/DataInterchange/TemporaryStorage/integration-smoke/xml-"+project),output=root.resolve("output/result.xml");
  Files.createDirectories(root.resolve("input"));Files.createDirectories(output.getParent());Files.write(root.resolve("input/input.xml"),input);
  String password=new String(Files.readAllBytes(Paths.get("C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt")),StandardCharsets.UTF_8).trim();StringBuilder hash=new StringBuilder();for(byte b:MessageDigest.getInstance("MD5").digest(password.getBytes(StandardCharsets.UTF_8)))hash.append(String.format("%02x",b&255));
  InterchangeDataHolder data=new InterchangeDataHolder();
  data.setInterchangeProjectsTab(StudioFileCopySmoke.table(new String[]{"InterchangeProject_ID","InterchangeProject_Name"},new String[]{project,"ETLForge XML Probe "+project.substring(0,8)}));
  data.setSourcesTab(StudioFileCopySmoke.table(new String[]{"Source_ID","Source_Name","Source_Type_ID","Locator","File_Name","Login","Password","Driver","DriverType","ConnectString"},new String[]{source,"Isolated XML input","16",root.resolve("input").toString(),"input.xml",null,null,null,null,null}));
  data.setTargetsTab(StudioFileCopySmoke.table(new String[]{"Target_ID","Target_Name","Source_Type_ID","Locator","File_Name","Login","Password","Driver","DriverType","ConnectString","Target_Target_ID"},new String[]{target,"Isolated XML output","16",output.getParent().toString(),"result.xml",null,null,null,null,null,null}));
  data.setOperationsTab(StudioFileCopySmoke.table(new String[]{"Operation_ID","Operation_Name","Operation_Type_ID","Source_ID","Target_ID","Ignore_History","Schedule_ID","Transformation_ID","Success_Operation_ID","Success_Message_ID","Failure_Operation_ID","Failure_Message_ID","Trace"},new String[]{operation,"XML mapping probe","3",source,target,"1",null,transformation,null,null,null,null,"1"}));
  data.setOperationPipelines(StudioFileCopySmoke.table(new String[]{"Operation_ID","ActivityType_ID","Activity_ID"},new String[]{operation,"3",source},new String[]{operation,"5",transformation},new String[]{operation,"4",target}));
  DeployInterchangeData deploy=new DeployInterchangeDataServiceLocator().getkonga_deploy_interchange_data(new URL("http://127.0.0.1:47100/axis/konga_deploy_interchange_data"));
  CDeployResults result=deploy.deployInterchangeData("Admin",hash.toString(),new InterchangeIdHolder(),data);
  if(result==null||result.getDeployStatus()>1)throw new AssertionError("Probe deployment failed");
  JobControl jobs=new JobControlServiceLocator().getkonga_jobcontrol(new URL("http://127.0.0.1:47100/axis/konga_jobcontrol"));
  WsRunOperationResult run=jobs.runOperationInstanceMD5Password("Admin",hash.toString(),operation);
  if(run==null||run.getOperationInstanceId()<1)throw new AssertionError("Probe submission failed");
  if(!Arrays.equals(input,Files.readAllBytes(original)))throw new AssertionError("Original input changed");
  Files.write(Paths.get("C:/ETL/Reports/xml-transform-probe.json"),("{\"instance\":"+run.getOperationInstanceId()+",\"output\":\""+output.toString().replace('\\','/')+"\"}").getBytes(StandardCharsets.UTF_8));
  System.out.println("Queued isolated XML mapping probe instance "+run.getOperationInstanceId());
 }
}
