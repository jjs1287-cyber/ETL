import java.net.URL;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.etlforge.integration.data.Password;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.*;
import org.etlforge.integration.server.implementation.webservice.interchange.jobcontrol.client.*;
public final class S3EndpointLiveTest {
 static final Path ROOT=Paths.get("C:/ETL/Samples/S3/live");static final ObjectMapper JSON=new ObjectMapper();
 static String hash,prefix,bucket,region,access,secret;static int checks;static ArrayList<String> operations=new ArrayList<>();static ArrayList<String[]> props=new ArrayList<>();
 static String guid(){return UUID.randomUUID().toString();}
 static void check(boolean ok,String label){if(!ok)throw new AssertionError(label);checks++;System.out.println("PASS "+label);System.out.flush();}
 static void prop(String entity,String id,String key,String value){props.add(new String[]{entity,id,key,value});}
 static void credentials(String entity,String id,boolean bad){prop(entity,id,"s3_region",region);prop(entity,id,"s3_authentication","access_key");prop(entity,id,"s3_access_key_protected",access);prop(entity,id,"s3_secret_key_protected",bad?Password.encrypt("incorrect-test-secret").encrypted():secret);}
 static String run(int index)throws Exception{
  JobControl jobs=new JobControlServiceLocator().getkonga_jobcontrol(new URL("http://127.0.0.1:47100/axis/konga_jobcontrol"));long instance=jobs.runOperationInstanceMD5Password("Admin",hash,operations.get(index)).getOperationInstanceId();
  for(int n=0;n<1200;n++){String state=NativeJavaScriptMappingTest.status(instance);if(!state.isEmpty()&&!state.equals("0")&&!state.equals("2")){System.out.println("Operation "+index+" instance "+instance+" status "+state);return state;}Thread.sleep(100);}throw new AssertionError("S3 operation timed out");
 }
 static void deploy()throws Exception{
  String project=guid();ArrayList<String[]> sources=new ArrayList<>(),targets=new ArrayList<>(),ops=new ArrayList<>(),pipes=new ArrayList<>();
  for(int n=0;n<10;n++){
   String s=guid(),t=guid(),op=guid();operations.add(op);boolean sourceS3=n!=1&&n!=5;boolean targetS3=n==1||n==2||n==5;
   String sourcePrefix=prefix+(n==6?"output/":n==8?"delete-after/":n==9?"no-matching-objects/":"input/");
   String pattern=n==2?"*":n==4?"*.txt":n==6?"combined.zip":"payload.bin";
   String targetName=n==1||n==5?"uploaded.bin":n==2?"[file].[ext]":n==6?"[file].[ext]":n==4?"result.txt":"result.bin";
   Files.createDirectories(ROOT.resolve("out"+n));
   sources.add(new String[]{s,"S3 source verification "+n,sourceS3?"18":"16",sourceS3?bucket:ROOT.toString(),sourceS3?sourcePrefix:null,sourceS3?pattern:"input.bin",null,null,null,null,null,null,n==8?"/\\delete/\\":null});
   targets.add(new String[]{t,"S3 target verification "+n,targetS3?"18":"16",targetS3?bucket:ROOT.resolve("out"+n).toString(),targetS3?prefix+"output/":null,targetName,null,null,null,null,null,null,"-1"});
   if(sourceS3){credentials("2",s,n==3);prop("2",s,"s3_recursive","true");}if(targetS3){credentials("3",t,false);prop("3",t,"s3_overwrite",n==5?"false":"true");}
   if(n==2){prop("3",t,"compression_enabled","true");prop("3",t,"compression_format","zip");prop("3",t,"compression_mode","combined");prop("3",t,"compression_archive_name","combined");}
   if(n==6)prop("2",s,"compression_enabled","true");
   ops.add(new String[]{op,"Amazon S3 verification "+n,"1",s,t,"1",null,null,null,null,null,null});pipes.add(new String[]{op,"3",s});pipes.add(new String[]{op,"4",t});
  }
  InterchangeDataHolder d=new InterchangeDataHolder();d.setInterchangeProjectsTab(StudioFileCopySmoke.table(new String[]{"InterchangeProject_ID","InterchangeProject_Name"},new String[]{project,"Amazon S3 verification "+UUID.randomUUID()}));
  d.setSourcesTab(StudioFileCopySmoke.table(new String[]{"Source_ID","Source_Name","Source_Type_ID","Locator","Path","File_Name","Login","Password","Driver","DriverType","ConnectString","Port","RenameFileTo"},sources.toArray(new String[0][])));
  d.setTargetsTab(StudioFileCopySmoke.table(new String[]{"Target_ID","Target_Name","Source_Type_ID","Locator","Path","File_Name","Login","Password","Driver","DriverType","ConnectString","Port","Target_Target_ID"},targets.toArray(new String[0][])));
  d.setOperationsTab(StudioFileCopySmoke.table(new String[]{"Operation_ID","Operation_Name","Operation_Type_ID","Source_ID","Target_ID","Ignore_History","Schedule_ID","Transformation_ID","Success_Operation_ID","Success_Message_ID","Failure_Operation_ID","Failure_Message_ID"},ops.toArray(new String[0][])));
  d.setOperationPipelines(StudioFileCopySmoke.table(new String[]{"Operation_ID","ActivityType_ID","Activity_ID"},pipes.toArray(new String[0][])));d.setEntityProperties(StudioFileCopySmoke.table(new String[]{"EntityType_ID","Entity_ID","KeyName","KeyValue"},props.toArray(new String[0][])));
  CDeployResults result=new DeployInterchangeDataServiceLocator().getkonga_deploy_interchange_data(new URL("http://127.0.0.1:47100/axis/konga_deploy_interchange_data")).deployInterchangeData("Admin",hash,new InterchangeIdHolder(),d);check(result!=null&&result.getDeployStatus()<=1,"deploy S3 endpoints and encrypted project credentials");
 }
 public static void main(String[] args)throws Exception{
  LocalVariablesRuntimeTest.init();hash=LocalVariablesRuntimeTest.hash;JsonNode config=JSON.readTree(Files.readAllBytes(Paths.get("C:/ETL/Server/private/s3-test-credentials.json")));bucket=config.path("bucket").asText();region=config.path("region").asText();prefix=config.path("prefix").asText();access=config.path("accessKeyProtected").asText();secret=config.path("secretKeyProtected").asText();
  ObjectNode r=JSON.createObjectNode();r.put("action","s3.test");r.put("bucket",bucket);r.put("region",region);r.put("prefix",prefix);r.put("accessKeyProtected",access);r.put("secretKeyProtected",secret);SftpKeysLiveTest.hash=hash;
  JsonNode response=SftpKeysLiveTest.request(r);check(response.path("files").isArray(),"native Test Connection authenticated through Studio HTTP port");check(!response.toString().contains(secret)&&!response.toString().contains(access),"connection response excludes encrypted credentials");
  boolean denied=false;try{SftpKeysLiveTest.request(r,"invalid-user","00000000000000000000000000000000");}catch(Exception e){denied=true;}check(denied,"unauthenticated S3 test rejected");
  deploy();check(run(0).equals("3"),"S3 source operation succeeds");check(Arrays.equals(Files.readAllBytes(ROOT.resolve("input.bin")),Files.readAllBytes(ROOT.resolve("out0/result.bin"))),"S3 source operation exact binary bytes");
  check(run(1).equals("3"),"S3 target operation succeeds");check(run(2).equals("3"),"multiple S3 source objects compressed to one S3 target ZIP");
  check(run(3).equals("6"),"invalid S3 credentials fail operation cleanly");try(java.util.stream.Stream<Path> files=Files.list(ROOT.resolve("out3"))){check(files.count()==0,"failed source operation writes no target files");}
  check(run(4).equals("3"),"recursive Unicode S3 source pattern");check(Arrays.equals(Files.readAllBytes(ROOT.resolve("unicode.txt")),Files.readAllBytes(ROOT.resolve("out4/result.txt"))),"Unicode source operation exact UTF-8 bytes");
  check(run(5).equals("6"),"overwrite-disabled S3 target rejects existing object");check(run(6).equals("3"),"compressed S3 source expands through existing pipeline");
  java.util.List<byte[]> contents=new ArrayList<>();try(java.util.stream.Stream<Path> files=Files.list(ROOT.resolve("out6"))){for(Path p:(Iterable<Path>)files::iterator)contents.add(Files.readAllBytes(p));}check(contents.size()==2,"S3 archive expands both members");byte[] binary=Files.readAllBytes(ROOT.resolve("input.bin")),unicode=Files.readAllBytes(ROOT.resolve("unicode.txt"));check(contents.stream().anyMatch(x->Arrays.equals(x,binary))&&contents.stream().anyMatch(x->Arrays.equals(x,unicode)),"S3 ZIP operation preserves binary and Unicode contents");
  check(run(8).equals("3"),"delete source after successful S3 operation");String empty=run(9);check(empty.equals("3")||empty.equals("4")||empty.equals("5"),"empty source prefix completes without failure");
  Files.write(ROOT.resolve("operation-guids.txt"),operations,StandardCharsets.UTF_8);System.out.println(checks+" live S3 operation checks passed");
 }
}
