import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
public final class SftpKeysFixtureCleanup {
 public static void main(String[] args)throws Exception{
  LocalVariablesRuntimeTest.init();SftpKeysLiveTest.hash=LocalVariablesRuntimeTest.hash;
  String first=new String(Files.readAllBytes(Paths.get("C:/ETL/Samples/SftpKeys/test_ed25519_plain.pub")),StandardCharsets.UTF_8).trim(),second=new String(Files.readAllBytes(Paths.get("C:/ETL/Samples/SftpKeys/test_ed25519_pass.pub")),StandardCharsets.UTF_8).trim(),initial=new String(Files.readAllBytes(Paths.get("C:/ETL/Samples/SftpKeys/test_ed25519.pub")),StandardCharsets.UTF_8).trim();
  int removed=0;for(JsonNode key:SftpKeysLiveTest.request(SftpKeysLiveTest.action("ssh.list")).path("keys")){
   String publicKey=key.path("publicKey").asText(),name=key.path("name").asText();if((publicKey.equals(first)||publicKey.equals(second)||publicKey.equals(initial))&&(name.startsWith("SFTP encrypted test ")||name.startsWith("SFTP integration test live-")||name.startsWith("SFTP verified test key"))){ObjectNode r=SftpKeysLiveTest.action("ssh.delete");r.put("id",key.path("id").asText());SftpKeysLiveTest.request(r);removed++;}
  }
  System.out.println("Removed "+removed+" owned integration fixture keys; user keys preserved.");
 }
}
