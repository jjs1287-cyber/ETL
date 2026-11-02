import java.net.URL;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.*;
import java.util.*;
import java.util.regex.*;
import org.etlforge.integration.server.implementation.webservice.interchange.jobcontrol.client.*;
public final class ClusterRuntimeSmoke {
 public static void main(String[] args)throws Exception{
  ClusterDatabaseTest.readPassword();String manifest=new String(Files.readAllBytes(Paths.get("C:/ETL/Reports/native-script-job-manifest.json")),StandardCharsets.UTF_8);Matcher m=Pattern.compile("\"operationGuid\":\"([a-f0-9-]{36})\"").matcher(manifest);if(!m.find())throw new AssertionError("Fixture missing");
  String password=new String(Files.readAllBytes(Paths.get("C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt")),StandardCharsets.UTF_8).trim();StringBuilder hash=new StringBuilder();for(byte b:MessageDigest.getInstance("MD5").digest(password.getBytes(StandardCharsets.UTF_8)))hash.append(String.format("%02x",b&255));password=null;
  JobControl api=new JobControlServiceLocator().getkonga_jobcontrol(new URL("http://127.0.0.1:47100/axis/konga_jobcontrol"));((org.apache.axis.client.Stub)api).setTimeout(30000);
  int requested=args.length==0?20:Integer.parseInt(args[0]);if(requested<1||requested>2000)throw new IllegalArgumentException("Count must be 1..2000");
  List<Long> ids=new ArrayList<>();long start=System.nanoTime();for(int i=0;i<requested;i++)ids.add(api.runOperationInstanceMD5Password("Admin",hash.toString(),m.group(1)).getOperationInstanceId());
  String list=ids.toString().replace('[','(').replace(']',')');
  try(Connection c=ClusterDatabaseTest.db("tran")){
   for(int i=0;i<1800;i++){if(ClusterDatabaseTest.number(c,"SELECT count(*) FROM operationlogtab WHERE operation_instance_id IN "+list+" AND status IN (3,4,5)")==requested)break;if(i==1799)throw new AssertionError("Operations did not all succeed");Thread.sleep(100);}
   if(ClusterDatabaseTest.number(c,"SELECT count(*) FROM operationlogtab WHERE operation_instance_id IN "+list)!=requested)throw new AssertionError("Duplicate or missing instance logs");
   if(ClusterDatabaseTest.number(c,"SELECT count(DISTINCT operation_instance_id) FROM operationmessagetab WHERE operation_instance_id IN "+list)!=requested)throw new AssertionError("Missing messages");
   List<String> outputs=new ArrayList<>();try(Statement s=c.createStatement();ResultSet r=s.executeQuery("SELECT message FROM operationmessagetab WHERE operation_instance_id IN "+list+" ORDER BY operation_instance_id,operation_message_id")){while(r.next())outputs.add(r.getString(1));}
   String digest=Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-256").digest(String.join("\n",outputs).getBytes(StandardCharsets.UTF_8)));
   System.out.println("PASS "+requested+" real Studio SOAP submissions completed successfully with logs and messages");System.out.println("first_instance="+ids.get(0)+" last_instance="+ids.get(ids.size()-1)+" elapsed_ms="+(System.nanoTime()-start)/1000000+" messages="+outputs.size()+" output_sha256="+digest);
  }
 }
}
