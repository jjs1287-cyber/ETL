import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import org.etlforge.integration.server.implementation.webservice.interchange.authenticate.client.Authenticate;
import org.etlforge.integration.server.implementation.webservice.interchange.authenticate.client.AuthenticateResult;
import org.etlforge.integration.server.implementation.webservice.interchange.authenticate.client.AuthenticateServiceLocator;
import org.etlforge.integration.server.implementation.webservice.WsKeyValue;
import org.etlforge.integration.server.implementation.webservice.WsKeyValuePairs;
public final class StudioNativeLoginSmoke {
 public static void main(String[] args) throws Exception {
  String password=new String(Files.readAllBytes(Paths.get("C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt")),StandardCharsets.UTF_8).trim();
  byte[] digest=MessageDigest.getInstance("MD5").digest(password.getBytes(StandardCharsets.UTF_8));StringBuilder hash=new StringBuilder();for(byte b:digest)hash.append(String.format("%02x",b&255));
  Authenticate service=new AuthenticateServiceLocator().getkonga_authenticate(new URL(System.getProperty("etlforge.native.api", "http://127.0.0.1:47113/axis/")+"konga_authenticate"));
  AuthenticateResult result=service.loginWithVersionAndKeyValues("Admin",hash.toString(),"42.0.0.0",new WsKeyValuePairs(new WsKeyValue[0]));
  if(result==null||result.getServerGuid()==null||result.getServerGuid().isEmpty()||result.getUserGuid()==null||result.getUserGuid().isEmpty())throw new AssertionError("Missing login GUIDs");
  if(!"42.0.0.0".equals(result.getVersionString()))throw new AssertionError("Unexpected native ETLForge product version");
  if(result.getKeyValues()==null)throw new AssertionError("Missing native login properties");
  org.etlforge.integration.server.implementation.webservice.interchange.checkout.client.CheckoutInterchangeData checkout = new org.etlforge.integration.server.implementation.webservice.interchange.checkout.client.CheckoutInterchangeDataServiceLocator().getkonga_checkout_interchange_data(new URL(System.getProperty("etlforge.native.api", "http://127.0.0.1:47113/axis/")+"konga_checkout_interchange_data"));
  org.etlforge.integration.server.implementation.webservice.interchange.checkout.client.InterchangeDataHolder projects=checkout.getOperationTree("Admin",hash.toString());
  if(projects==null)throw new AssertionError("Project list response is null");
  org.etlforge.integration.server.implementation.webservice.admin.client.Admin admin = new org.etlforge.integration.server.implementation.webservice.admin.client.AdminServiceLocator().getkonga_admin(new URL(System.getProperty("etlforge.native.api", "http://127.0.0.1:47113/axis/")+"konga_admin"));
  if(!admin.isEngineRunning("Admin",hash.toString())||!admin.isSchedulerRunning("Admin",hash.toString()))throw new AssertionError("Native service administration reports an inactive processing service");
  if(Boolean.getBoolean("etlforge.test.jms")&&!admin.isJmsEngineRunning("Admin",hash.toString()))throw new AssertionError("Optional JMS service is not running");
  System.out.println("PASS: rebuilt Studio client reads running process-engine and scheduler status through native Admin SOAP API");
  System.out.println("PASS: rebuilt Studio client retrieves the native server project list");
  System.out.println("PASS: actual rebuilt Studio Java SOAP client authenticates against native x64 service and deserializes GUIDs, version and key/value properties");
 }
}
