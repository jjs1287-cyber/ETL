import java.nio.file.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.etlforge.integration.data.Password;
public final class S3TestConnectionLiveTest {
 static int checks;
 static void check(boolean condition,String label){if(!condition)throw new AssertionError(label);checks++;System.out.println("PASS "+label);}
 public static void main(String[] args)throws Exception{
  LocalVariablesRuntimeTest.init();SftpKeysLiveTest.hash=LocalVariablesRuntimeTest.hash;ObjectMapper json=new ObjectMapper();ObjectNode r=(ObjectNode)json.readTree(Files.readAllBytes(Paths.get("C:/ETL/Server/private/s3-test-credentials.json")));r.put("action","s3.test");r.put("authentication","access_key");r.put("direction","source");
  JsonNode source=SftpKeysLiveTest.request(r);check(source.path("files").isArray(),"source Test Connection lists accessible prefix");
  r.put("direction","target");JsonNode target=SftpKeysLiveTest.request(r);check(target.path("message").asText().contains("upload"),"target Test Connection verifies upload and delete permissions");
  String access=Password.decrypt(r.path("accessKeyProtected").asText()).clearText(),secret=Password.decrypt(r.path("secretKeyProtected").asText()).clearText();check(!target.toString().contains(access)&&!target.toString().contains(secret),"S3 connection response never exposes plaintext credentials");
  r.put("direction","source");ObjectNode invalid=r.deepCopy();invalid.put("bucket","https://127.0.0.1/");boolean denied=false;try{SftpKeysLiveTest.request(invalid);}catch(Exception expected){denied=true;}check(denied,"bucket URL injection rejected");
  invalid=r.deepCopy();invalid.put("region","us-east-1.evil.test");denied=false;try{SftpKeysLiveTest.request(invalid);}catch(Exception expected){denied=true;}check(denied,"region cannot redirect signed requests");
  invalid=r.deepCopy();invalid.put("authentication","temporary");invalid.put("sessionTokenProtected","");denied=false;try{SftpKeysLiveTest.request(invalid);}catch(Exception expected){denied=true;}check(denied,"temporary credentials require a session token");
  Path normal=Paths.get("C:/ETL/Server/private/s3-test-user.json");if(Files.exists(normal)){JsonNode user=json.readTree(Files.readAllBytes(normal));check(SftpKeysLiveTest.request(r,user.path("name").asText(),user.path("hash").asText()).path("files").isArray(),"authenticated non-admin can test supplied S3 credentials");}
  System.out.println(checks+" S3 Test Connection checks passed");
 }
}
