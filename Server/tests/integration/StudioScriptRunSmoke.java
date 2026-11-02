import java.net.URL;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.regex.*;
import org.etlforge.integration.server.implementation.webservice.interchange.jobcontrol.client.*;

/** Submits only the owned script fixture, using Studio's actual job-control client. */
public final class StudioScriptRunSmoke {
 public static void main(String[] args) throws Exception {
  String manifest=new String(Files.readAllBytes(Paths.get("C:/ETL/Reports/native-script-job-manifest.json")),StandardCharsets.UTF_8);
  Matcher match=Pattern.compile("\"operationGuid\":\"([a-f0-9-]{36})\"").matcher(manifest);
  if(!match.find())throw new AssertionError("Missing owned script operation GUID");
  String password=new String(Files.readAllBytes(Paths.get("C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt")),StandardCharsets.UTF_8).trim();
  StringBuilder hash=new StringBuilder();for(byte b:MessageDigest.getInstance("MD5").digest(password.getBytes(StandardCharsets.UTF_8)))hash.append(String.format("%02x",b&255));
  JobControl service=new JobControlServiceLocator().getkonga_jobcontrol(new URL("http://127.0.0.1:47100/axis/konga_jobcontrol"));
  ((org.apache.axis.client.Stub)service).setTimeout(30000);
  WsRunOperationResult result=service.runOperationInstanceMD5Password("Admin",hash.toString(),match.group(1));
  if(result==null||result.getOperationInstanceId()<1)throw new AssertionError("Operation submission did not return a valid instance");
  Files.write(Paths.get("C:/ETL/Reports/native-script-run-instance.txt"),Long.toString(result.getOperationInstanceId()).getBytes(StandardCharsets.UTF_8));
  System.out.println("PASS: Studio job-control SOAP request returned operation instance "+result.getOperationInstanceId());
 }
}
