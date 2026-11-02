import java.net.URL;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import org.etlforge.integration.server.implementation.webservice.admin.client.*;
public final class ServerProxySmoke {
 static int checks;static void require(boolean ok,String message){if(!ok)throw new AssertionError(message);checks++;}
 public static void main(String[] args)throws Exception {
  String password=new String(Files.readAllBytes(Paths.get("C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt")),StandardCharsets.UTF_8).trim();StringBuilder hash=new StringBuilder();for(byte b:MessageDigest.getInstance("MD5").digest(password.getBytes(StandardCharsets.UTF_8)))hash.append(String.format("%02x",b&255));
  Admin service=new AdminServiceLocator().getkonga_admin(new URL("http://127.0.0.1:47100/axis/konga_admin"));((org.apache.axis.client.Stub)service).setTimeout(30000);
  WsProxySettings original=service.getProxySettings("Admin",hash.toString());require(original!=null,"Missing proxy settings");
  require(!original.isEnabled(),"This test requires proxy usage to be disabled so live traffic is unaffected");
  try {
   WsProxySettings test=new WsProxySettings(false,"127.0.0.1",18080,"DOMAIN\\t\u00e9st_\u4e2d\u6587","temporary-"+UUID.randomUUID()+"-\u00e9\u4e2d\u6587",true,new String[]{"localhost","127.0.0.1"});
   System.out.println("Testing disabled proxy save...");try{service.setProxySettings("Admin",hash.toString(),test);}catch(Exception error){String request=((org.apache.axis.client.Stub)service)._getCall().getMessageContext().getRequestMessage().getSOAPPartAsString();try{javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new org.xml.sax.InputSource(new java.io.StringReader(request)));System.out.println("Outgoing SOAP XML is valid.");}catch(Exception invalid){System.out.println("Outgoing SOAP XML is invalid: "+invalid.getClass().getSimpleName());}throw error;}System.out.println("Save returned; loading settings...");WsProxySettings loaded=service.getProxySettings("Admin",hash.toString());
   require(!loaded.isEnabled(),"Proxy usage became enabled");require(loaded.getPort()==18080,"Proxy port did not persist");require(test.getUserName().equals(loaded.getUserName()),"Domain-qualified Unicode login changed");require(test.getPassword().equals(loaded.getPassword()),"Encrypted proxy password did not round-trip");require(loaded.isTrustAllCertificates(),"Certificate policy did not persist");require(Arrays.equals(test.getExceptions(),loaded.getExceptions()),"Proxy exceptions changed");
   test.setExceptions(null);service.setProxySettings("Admin",hash.toString(),test);require(Arrays.equals(new String[]{"localhost","127.0.0.1"},service.getProxySettings("Admin",hash.toString()).getExceptions()),"UI-style save cleared existing exceptions");
   test.setPort(70000);boolean rejected=false;try{service.setProxySettings("Admin",hash.toString(),test);}catch(org.apache.axis.AxisFault fault){rejected=true;}require(rejected,"Invalid port accepted");require(service.getProxySettings("Admin",hash.toString()).getPort()==18080,"Invalid save partially changed settings");
   test.setPort(18080);test.setHost("http://user:secret@proxy/");rejected=false;try{service.setProxySettings("Admin",hash.toString(),test);}catch(org.apache.axis.AxisFault fault){rejected=true;}require(rejected,"Proxy URL with embedded credentials accepted");
   rejected=false;try{service.getProxySettings("Admin","not-a-valid-password-hash");}catch(org.apache.axis.AxisFault fault){rejected=true;}require(rejected,"Unauthenticated proxy read accepted");
   test.setHost("");test.setEnabled(true);rejected=false;try{service.setProxySettings("Admin",hash.toString(),test);}catch(org.apache.axis.AxisFault fault){rejected=true;}require(rejected,"Enabled proxy with no host accepted");
  }finally {if(original.getExceptions()==null)original.setExceptions(new String[]{""});service.setProxySettings("Admin",hash.toString(),original);}
  require(!service.getProxySettings("Admin",hash.toString()).isEnabled(),"Original disabled state not restored");
  System.out.println("PASS: "+checks+" server proxy SOAP checks; disabled configuration round-trip, Unicode/domain login, encrypted password, exceptions preservation, validation and authentication. Original configuration restored; proxy usage remained disabled.");
 }
}
