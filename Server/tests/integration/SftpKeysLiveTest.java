import java.net.URL;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import javax.xml.namespace.QName;
import org.apache.axis.client.*;
import org.apache.axis.constants.*;
import org.apache.axis.encoding.XMLType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.*;
import org.etlforge.integration.server.implementation.webservice.interchange.jobcontrol.client.*;
public final class SftpKeysLiveTest {
 static final ObjectMapper JSON=new ObjectMapper();static String hash,key,encryptedKey,badPassphraseKey,user,hostFingerprint;static Path root;static int checks;static List<String> operations=new ArrayList<>();
 static void check(boolean ok,String label){if(!ok)throw new AssertionError(label);checks++;System.out.println("PASS "+label);}
 static JsonNode request(ObjectNode r,String login,String credential)throws Exception{Service service=new Service();service.getEngine().setOption("sendMultiRefs",Boolean.FALSE);Call call=(Call)service.createCall();call.setTargetEndpointAddress("http://127.0.0.1:47100/axis/konga_admin");call.setOperationName(new QName("urn:org.etlforge.integration.server.implementation.webservice.admin.Admin","auditRequest"));call.setOperationStyle(Style.WRAPPED);call.setOperationUse(Use.LITERAL);call.setUseSOAPAction(true);call.setSOAPActionURI("");call.setTimeout(30000);for(String f:new String[]{"in0","in1","in2"})call.addParameter(new QName("",f),XMLType.XSD_STRING,javax.xml.rpc.ParameterMode.IN);call.setReturnType(XMLType.XSD_STRING);return JSON.readTree((String)call.invoke(new Object[]{login,credential,r.toString()}));}
 static JsonNode request(ObjectNode r)throws Exception{return request(r,"Admin",hash);}
 static ObjectNode action(String a){ObjectNode r=JSON.createObjectNode();r.put("action",a);return r;}
 static String guid(){return UUID.randomUUID().toString();}
 static String state(String operation)throws Exception{JobControl jobs=new JobControlServiceLocator().getkonga_jobcontrol(new URL("http://127.0.0.1:47100/axis/konga_jobcontrol"));long instance=jobs.runOperationInstanceMD5Password("Admin",hash,operation).getOperationInstanceId();for(int i=0;i<400;i++){String status=NativeJavaScriptMappingTest.status(instance);if(!status.isEmpty()&&!status.equals("0")&&!status.equals("2")){System.out.println("instance="+instance+" status="+status);return status;}Thread.sleep(100);}throw new AssertionError("Operation timed out "+instance);}
 static void deploy()throws Exception{
  String project=guid();ArrayList<String[]> sources=new ArrayList<>(),targets=new ArrayList<>(),ops=new ArrayList<>(),pipes=new ArrayList<>(),props=new ArrayList<>();
  for(int n=0;n<7;n++){
   String s=guid(),t=guid(),op=guid();operations.add(op);boolean upload=n==1;String fingerprint=n==2?"SHA256:AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA":hostFingerprint;String keyId=n==3?"00000000-0000-0000-0000-000000000000":n==5?encryptedKey:n==6?badPassphraseKey:key;
   sources.add(new String[]{s,"SFTP test source "+n,upload?"16":"1",upload?root.resolve("input").toString():"127.0.0.1",upload?null:"/input/",n==5?"caf*.bin":"payload.bin",upload?null:user,null,upload?null:"2022",upload?null:"4",null,null,null});
   targets.add(new String[]{t,"SFTP test target "+n,upload?"1":"16",upload?"127.0.0.1":root.resolve("download-"+n).toString(),upload?"/output/":null,"result-"+n+".bin",upload?user:null,null,upload?"2022":null,upload?"4":null,null,null,null,"-1"});
   String type=upload?"3":"2",endpoint=upload?t:s;props.add(new String[]{type,endpoint,"sftp_key_id",keyId});if(n!=4)props.add(new String[]{type,endpoint,"sftp_host_sha256",fingerprint});
   ops.add(new String[]{op,"SFTP key verification "+n,"1",s,t,"1",null,null,null,null,null,null});pipes.add(new String[]{op,"3",s});pipes.add(new String[]{op,"4",t});Files.createDirectories(root.resolve("download-"+n));
  }
  InterchangeDataHolder d=new InterchangeDataHolder();d.setInterchangeProjectsTab(StudioFileCopySmoke.table(new String[]{"InterchangeProject_ID","InterchangeProject_Name"},new String[]{project,"SFTP key verification "+root.getFileName()}));
  d.setSourcesTab(StudioFileCopySmoke.table(new String[]{"Source_ID","Source_Name","Source_Type_ID","Locator","Path","File_Name","Login","Password","Port","Encrypt_Choice","Driver","DriverType","ConnectString"},sources.toArray(new String[0][])));
  d.setTargetsTab(StudioFileCopySmoke.table(new String[]{"Target_ID","Target_Name","Source_Type_ID","Locator","Path","File_Name","Login","Password","Port","Encrypt_Choice","Driver","DriverType","ConnectString","Target_Target_ID"},targets.toArray(new String[0][])));
  d.setOperationsTab(StudioFileCopySmoke.table(new String[]{"Operation_ID","Operation_Name","Operation_Type_ID","Source_ID","Target_ID","Ignore_History","Schedule_ID","Transformation_ID","Success_Operation_ID","Success_Message_ID","Failure_Operation_ID","Failure_Message_ID"},ops.toArray(new String[0][])));
  d.setOperationPipelines(StudioFileCopySmoke.table(new String[]{"Operation_ID","ActivityType_ID","Activity_ID"},pipes.toArray(new String[0][])));d.setEntityProperties(StudioFileCopySmoke.table(new String[]{"EntityType_ID","Entity_ID","KeyName","KeyValue"},props.toArray(new String[0][])));
  CDeployResults result=new DeployInterchangeDataServiceLocator().getkonga_deploy_interchange_data(new URL("http://127.0.0.1:47100/axis/konga_deploy_interchange_data")).deployInterchangeData("Admin",hash,new InterchangeIdHolder(),d);check(result!=null&&result.getDeployStatus()<=1,"deploy key references on SFTP source and target");
 }
 public static void main(String[] args)throws Exception{
  LocalVariablesRuntimeTest.init();hash=LocalVariablesRuntimeTest.hash;JsonNode fixture=JSON.readTree(Files.readAllBytes(Paths.get("C:/ETL/Reports/sftp-live-fixture.json")));root=Paths.get(fixture.path("root").asText());user=fixture.path("user").asText();
  String publicHost=new String(Files.readAllBytes(Paths.get("C:/ProgramData/SFTPGo/id_ecdsa.pub")),StandardCharsets.UTF_8).trim();byte[] hostBytes=Base64.getDecoder().decode(publicHost.split(" ")[1]);hostFingerprint="SHA256:"+Base64.getEncoder().withoutPadding().encodeToString(java.security.MessageDigest.getInstance("SHA-256").digest(hostBytes));
  ObjectNode save=action("ssh.save");save.put("name","SFTP integration test "+root.getFileName());save.put("privateKey",new String(Files.readAllBytes(Paths.get("C:/ETL/Samples/SftpKeys/test_ed25519_plain")),StandardCharsets.UTF_8));save.put("publicKey",new String(Files.readAllBytes(Paths.get("C:/ETL/Samples/SftpKeys/test_ed25519_plain.pub")),StandardCharsets.UTF_8));save.put("passphrase","");for(JsonNode k:request(action("ssh.list")).path("keys"))if(k.path("name").asText().equals(save.path("name").asText()))save.put("id",k.path("id").asText());JsonNode result=request(save);for(JsonNode k:result.path("keys"))if(k.path("name").asText().equals(save.path("name").asText()))key=k.path("id").asText();check(key!=null,"administrator imports key through native API");check(!result.toString().contains("PRIVATE KEY")&&!result.toString().contains("passphrase"),"API never returns private material");
  boolean denied=false;try{request(action("ssh.list"),"etlforge_invalid_user","00000000000000000000000000000000");}catch(java.rmi.RemoteException expected){denied=true;}check(denied,"unauthenticated key listing denied");
  JsonNode permissions=JSON.readTree(Files.readAllBytes(Paths.get("C:/ETL/Server/private/sftp-test-user.json")));String normal=permissions.path("name").asText(),normalHash=permissions.path("hash").asText();check(request(action("ssh.list"),normal,normalHash).path("keys").isArray(),"authenticated non-admin can select metadata");denied=false;try{request(save,normal,normalHash);}catch(java.rmi.RemoteException expected){denied=true;}check(denied,"authenticated non-admin import denied");ObjectNode deletion=action("ssh.delete");deletion.put("id",key);denied=false;try{request(deletion,normal,normalHash);}catch(java.rmi.RemoteException expected){denied=true;}check(denied,"authenticated non-admin delete denied");
  for(int n=0;n<2;n++){ObjectNode r=action("ssh.save");r.put("name","SFTP encrypted test "+n+" "+UUID.randomUUID());r.put("privateKey",new String(Files.readAllBytes(Paths.get("C:/ETL/Samples/SftpKeys/test_ed25519_pass")),StandardCharsets.UTF_8));r.put("publicKey",new String(Files.readAllBytes(Paths.get("C:/ETL/Samples/SftpKeys/test_ed25519_pass.pub")),StandardCharsets.UTF_8));r.put("passphrase",n==0?"fixture-passphrase":"incorrect-passphrase");JsonNode saved=request(r);for(JsonNode k:saved.path("keys"))if(k.path("name").asText().equals(r.path("name").asText())){if(n==0)encryptedKey=k.path("id").asText();else badPassphraseKey=k.path("id").asText();}}
  Files.copy(root.resolve("input/payload.bin"),root.resolve("input/caf\u00e9 \u4e16\u754c\ud83d\ude00.bin"),StandardCopyOption.REPLACE_EXISTING);
  deploy();check(state(operations.get(0)).equals("3"),"SFTP source key authentication");check(Arrays.equals(Files.readAllBytes(root.resolve("input/payload.bin")),Files.readAllBytes(root.resolve("download-0/result-0.bin"))),"SFTP download preserves binary and UTF-8 bytes");
  check(state(operations.get(1)).equals("3"),"SFTP target key authentication");check(Arrays.equals(Files.readAllBytes(root.resolve("input/payload.bin")),Files.readAllBytes(root.resolve("output/result-1.bin"))),"SFTP upload preserves binary and UTF-8 bytes");
  for(int n=2;n<5;n++){check(state(operations.get(n)).equals("6"),"wrong fingerprint / missing key / missing host fingerprint fails cleanly "+n);try(java.util.stream.Stream<Path> paths=Files.list(root.resolve("download-"+n))){check(paths.count()==0,"failure has no target output "+n);}}
  check(state(operations.get(5)).equals("3"),"encrypted OpenSSH key and Unicode filename");check(Arrays.equals(Files.readAllBytes(root.resolve("input/payload.bin")),Files.readAllBytes(root.resolve("download-5/result-5.bin"))),"Unicode SFTP filename byte comparison");check(state(operations.get(6)).equals("6"),"wrong private-key passphrase fails cleanly");
  check(Files.exists(root.resolve("input/payload.bin")),"SFTP source preserved");
  ObjectNode rename=action("ssh.save");rename.put("id",key);rename.put("name","SFTP verified test key "+key);request(rename);check(state(operations.get(0)).equals("3"),"key rename preserves endpoint reference");
  Path vault=Paths.get("C:/ETL/Server/instances/dev/private/ssh-keys");try(java.util.stream.Stream<Path> files=Files.list(vault)){check(files.noneMatch(p->p.toString().endsWith(".pem")),"no transient private key files left after operations");}
  System.out.println("Passed "+checks+" live SFTP checks; key="+key);
 }
}
