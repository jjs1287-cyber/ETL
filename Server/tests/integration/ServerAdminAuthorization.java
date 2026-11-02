import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import com.etlforge.integration.server.implementation.webservice.serveradmin.client.*;
public final class ServerAdminAuthorization {
 interface Request {void run() throws Exception;}
 static int checks;
 static void denied(Request request)throws Exception {try{request.run();throw new AssertionError("Non-admin request accepted");}catch(java.rmi.RemoteException expected){String text=expected.toString().toLowerCase();if(text.contains("invalid user name")||text.contains("invalid user or password"))throw new AssertionError("Test user did not authenticate");checks++;}}
 public static void main(String[] args)throws Exception {
  String user="native_admin_permission_test",hash=new String(Files.readAllBytes(Paths.get("C:/ETL/Server/private/native-server-admin-test.hash")),StandardCharsets.US_ASCII).trim();
  ServerAdministration s=ServerAdminParity.service(args[0]);
  denied(()->s.downloadServerConfigurationFile(user,hash));denied(()->s.uploadServerConfigurationFile(user,hash,"invalid"));denied(()->s.downloadFile(user,hash,"C:/ETL/Server/instances/dev/etlforge.conf"));denied(()->s.listServerLogFiles(user,hash));denied(()->s.listPluginLogFiles(user,hash));denied(()->s.listOperationLogFiles(user,hash,null,null));denied(()->s.listDebugLogFiles(user,hash,null,null));
  System.out.println("PASS: "+checks+" authenticated non-admin denials, endpoint="+args[0]);
 }
}
