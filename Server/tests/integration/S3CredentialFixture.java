import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.io.*;
import java.util.UUID;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.etlforge.integration.data.Password;
public final class S3CredentialFixture {
 public static void main(String[] args)throws Exception{
  BufferedReader reader=new BufferedReader(new InputStreamReader(System.in,StandardCharsets.UTF_8));String access=reader.readLine(),secret=reader.readLine();
  if(access==null||secret==null)throw new IllegalArgumentException("Credentials must be supplied over standard input");
  ObjectNode config=new ObjectMapper().createObjectNode();config.put("bucket","testing-source-target");config.put("region","us-east-1");config.put("prefix","etlforge-tests/"+UUID.randomUUID()+"/");config.put("accessKeyProtected",Password.encrypt(access.trim()).encrypted());config.put("secretKeyProtected",Password.encrypt(secret.trim()).encrypted());
  Files.write(Paths.get(args[0]),new ObjectMapper().writeValueAsBytes(config));System.out.println("Protected S3 test fixture created; credentials not printed.");
 }
}
