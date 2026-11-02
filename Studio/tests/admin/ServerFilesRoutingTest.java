import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.lang.reflect.*;
import java.util.concurrent.atomic.AtomicReference;
import org.etlforge.integration.client.server.*;
import org.etlforge.integration.client.server.login.*;
import org.etlforge.integration.client.server.webservice.*;
import org.etlforge.integration.common.server.ServerGuid;
import com.etlforge.integration.client.admin.server.*;
import com.etlforge.integration.server.implementation.webservice.admin.server.impl.ServerAdministrationWsImpl;

public final class ServerFilesRoutingTest {
 static AtomicReference<Object> received=new AtomicReference<>();static AtomicReference<Throwable> error=new AtomicReference<>();static boolean cancelled;static int checks;
 static void check(boolean condition,String message){if(!condition)throw new AssertionError(message);checks++;}
 @SuppressWarnings("unchecked") static <T>T callback(Class<T> type){received.set(null);error.set(null);cancelled=false;return (T)Proxy.newProxyInstance(type.getClassLoader(),new Class<?>[]{type},(proxy,method,args)->{if(method.getName().equals("caught"))error.set((Throwable)args[0]);else if(method.getName().equals("cancelled"))cancelled=true;else received.set(args==null?Boolean.TRUE:args[0]);return null;});}
 static void success(){check(error.get()==null&&!cancelled&&received.get()!=null,"Studio adapter failed: "+(error.get()==null?"missing/cancelled result":error.get().getClass().getSimpleName()));}
 public static void main(String[] args)throws Exception {
  ServerPorts ports=new ServerPorts(){public int getDefaultPort(){return 47100;}public int getDefaultHttpsPort(){return 47101;}};
  ServerPorts deadJavaPorts=new ServerPorts(){public int getDefaultPort(){return 47199;}public int getDefaultHttpsPort(){return 47198;}};
  ServerManager manager=new ServerManager(null,ports,null);
  String password=new String(Files.readAllBytes(Paths.get("C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt")),StandardCharsets.UTF_8).trim();
  LoginCredentials credentials=new LoginCredentials(manager.getAddressFactory(),"127.0.0.1:47100","Admin",password,false,false);manager.getLoginService().getConfiguration().setConnected(credentials.getServerAddress(),new ServerGuid("42440000-0000-4000-8000-000000000099"),credentials);
  CppWebServiceConfiguration cpp=new CppWebServiceConfiguration(manager,ports,result->true);JavaWebServiceConfiguration javaConfig=new JavaWebServiceConfiguration(manager,deadJavaPorts,result->true);
  ServerAdministrationWsImpl api=new ServerAdministrationWsImpl(cpp,javaConfig);
  check(System.getProperty("etlforge.serverAdmin.backend")==null,"Default routing must be tested");
  api.viewServerConfigurationFile(callback(ViewServerConfigurationFileCallback.class));success();check(((ServerConfigurationFile)received.get()).getContents().equals(new String(Files.readAllBytes(Paths.get("C:/ETL/Server/instances/dev/etlforge.conf")),StandardCharsets.UTF_8)),"UI config content differs");
  api.uploadServerConfigurationFile(Paths.get("C:/ETL/Server/instances/dev/etlforge.conf").toFile(),callback(UploadServerConfigurationFileCallback.class));success();
  api.listServerLogFiles(callback(GetServerFileListCallback.class));success();api.listPluginLogFiles(callback(GetServerFileListCallback.class));success();api.listOperationLogFiles(null,null,callback(GetServerFileListCallback.class));success();api.listDebugLogFiles(null,null,callback(GetServerFileListCallback.class));success();
  Path file=Paths.get("C:/ETL/Server/instances/dev/log/native-admin-fixture-1-\u00e9\u4e2d\u6587.log");api.downloadServerFile(file.toString(),callback(DownloadServerFileCallback.class));success();check(java.util.Arrays.equals(Files.readAllBytes(file),Files.readAllBytes(((java.io.File)received.get()).toPath())),"UI downloaded bytes differ");
  Thread.currentThread().interrupt();try{api.listServerLogFiles(callback(GetServerFileListCallback.class));check(cancelled,"Interrupted request not cancelled");}finally{Thread.interrupted();}
  System.setProperty("etlforge.serverAdmin.backend","java");try{api.listServerLogFiles(callback(GetServerFileListCallback.class));check(error.get()!=null,"Explicit Java rollback did not route to dead Java port");}finally{System.clearProperty("etlforge.serverAdmin.backend");}
  System.out.println("PASS: "+checks+" Studio routing/callback checks. All seven default methods succeeded with Java configured at an unused port.");System.exit(0);
 }
}
