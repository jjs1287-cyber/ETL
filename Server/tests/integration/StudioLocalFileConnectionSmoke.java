import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.*;
import org.etlforge.integration.server.implementation.webservice.interchange.connection.client.*;

/** Test Connection uses an unsaved key/value location rather than the deployed DB factory. */
public final class StudioLocalFileConnectionSmoke {
 static ConnectionInfo info(String type,String directory,String filter){
  return new ConnectionInfo(new String[]{"source_type_id","locator","file_name"},new String[]{type,directory,filter});
 }
 public static void main(String[] args) throws Exception {
  String password=new String(Files.readAllBytes(Paths.get("C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt")),StandardCharsets.UTF_8).trim();
  StringBuilder hash=new StringBuilder();for(byte b:MessageDigest.getInstance("MD5").digest(password.getBytes(StandardCharsets.UTF_8)))hash.append(String.format("%02x",b&255));
  Path root=Paths.get("C:/ETL/Server/instances/dev/DataInterchange/TemporaryStorage/integration-smoke/connection-"+UUID.randomUUID());
  Files.createDirectories(root);
  byte[] xml="<root><value>ETLForge 日本</value></root>".getBytes(StandardCharsets.UTF_8);
  Files.write(root.resolve("fixture.xml"),xml);Files.write(root.resolve("unmatched.txt"),new byte[]{1,2,3});
  TestConnection service=new TestConnectionServiceLocator().getkonga_test_connection(new URL("http://127.0.0.1:47100/axis/konga_test_connection"));
  ((org.apache.axis.client.Stub)service).setTimeout(30000);
  FileList result=service.testFileTypeConnection("Admin",hash.toString(),info("16",root.toString(),"*.xml"),"",true);
  if(result==null||result.getFiles()==null||!Arrays.asList(result.getFiles()).contains("fixture.xml")||Arrays.asList(result.getFiles()).contains("unmatched.txt"))throw new AssertionError("Local XML file filter/listing failed");
  if(!Arrays.equals(xml,Files.readAllBytes(root.resolve("fixture.xml"))))throw new AssertionError("Connection test changed the source XML");
  service.testFileTypeConnection("Admin",hash.toString(),info("16",root.resolve("target").toString(),"output.xml"),"",false);
  if(!Files.isDirectory(root.resolve("target"))||Files.exists(root.resolve("target/output.xml")))throw new AssertionError("Local target connection probe unexpectedly wrote output");
  String relative="integration-smoke/"+root.getFileName();
  result=service.testFileTypeConnection("Admin",hash.toString(),info("15",relative,"*.xml"),"",true);
  if(result==null||result.getFiles()==null||!Arrays.asList(result.getFiles()).contains("fixture.xml"))throw new AssertionError("Temporary storage connection regression");
  System.out.println("PASS: actual Studio Test Connection SOAP client lists matching local XML files, preserves source bytes, tests local targets without output and retains temporary-storage support");
 }
}
