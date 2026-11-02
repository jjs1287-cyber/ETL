import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import com.fasterxml.jackson.databind.*;
import org.etlforge.integration.data.Password;
public final class RegressionProjectSecretCheck {
 public static void main(String[]args)throws Exception {
  Path root=Paths.get("C:/ETL/Reports/regression-suite/Test Operations");List<String> secrets=new ArrayList<>();secrets.add(new String(Files.readAllBytes(Paths.get("C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt")),StandardCharsets.UTF_8).trim());secrets.add(NativeJavaScriptMappingTest.databasePassword());JsonNode s3=new ObjectMapper().readTree(Files.readAllBytes(Paths.get("C:/ETL/Server/private/s3-test-credentials.json")));
  for(String key:new String[]{"accessKeyProtected","secretKeyProtected"})secrets.add(Password.decrypt(s3.path(key).asText()).clearText());
  int count=0;try(java.util.stream.Stream<Path> paths=Files.walk(root)){for(Path p:(Iterable<Path>)paths.filter(Files::isRegularFile)::iterator){String name=p.toString().toLowerCase();if(!name.endsWith(".xml")&&!name.endsWith(".json")&&!name.endsWith(".jip")&&!name.endsWith(".md"))continue;String text=new String(Files.readAllBytes(p),StandardCharsets.UTF_8);for(String secret:secrets)if(secret.length()>7&&text.contains(secret))throw new AssertionError("Unprotected test credential in "+root.relativize(p));if(text.contains("-----BEGIN PGP PRIVATE KEY BLOCK-----")||text.contains("-----BEGIN OPENSSH PRIVATE KEY-----"))throw new AssertionError("Private key embedded in project");count++;}}
  System.out.println("PASS scanned "+count+" project metadata files: no plaintext AWS/database/admin test credentials or embedded private keys");
 }
}
