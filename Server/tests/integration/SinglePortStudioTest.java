import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.util.concurrent.*;
import javax.swing.SwingUtilities;
import org.etlforge.application.Application;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.main.IntegrationMain;
import org.etlforge.integration.client.runtime.ClientPlatform;
import org.etlforge.integration.client.server.*;
import org.etlforge.integration.client.server.login.*;
import org.etlforge.integration.client.server.webservice.*;
import org.etlforge.integration.client.database.serverapi.JdbcDriverListCallback;
import org.etlforge.integration.database.driver.JdbcDriverDescriptor;
import org.etlforge.integration.server.implementation.webservice.interchange.db.client.JdbcInfoProviderServiceLocator;
import org.etlforge.integration.server.implementation.webservice.interchange.db.client.WsJdbcDriverInfo;

public final class SinglePortStudioTest {
 static int checks;static String password,hash;static final Set<Object> jobs=ConcurrentHashMap.newKeySet();
 static void ok(boolean value,String label){if(!value)throw new AssertionError(label);checks++;System.out.println("PASS "+label);}
 static WsJdbcDriverInfo drivers(int port,String user,String secret)throws Exception{
  org.etlforge.integration.server.implementation.webservice.interchange.db.client.JdbcInfoProvider api=new JdbcInfoProviderServiceLocator().getkonga_jdbc_infoprovider(new URL("http://127.0.0.1:"+port+"/axis/services/konga_jdbc_infoprovider"));((org.apache.axis.client.Stub)api).setTimeout(20000);return api.getDrivers(user,secret);
 }
 static final class Forward implements AutoCloseable {
  final ServerSocket listener;final Set<Socket> sockets=ConcurrentHashMap.newKeySet();final List<String> paths=new CopyOnWriteArrayList<>();final ExecutorService pool=Executors.newCachedThreadPool(r->{Thread t=new Thread(r,"ETLForge test forward");t.setDaemon(true);return t;});
  Forward()throws Exception{listener=new ServerSocket(0,50,InetAddress.getByName("127.0.0.1"));pool.submit(()->{while(!listener.isClosed()){try{Socket client=listener.accept();sockets.add(client);pool.submit(()->serve(client));}catch(IOException e){if(!listener.isClosed())throw new RuntimeException(e);}}});}
  void serve(Socket client){Socket backend=null;try{backend=new Socket("127.0.0.1",47100);backend.setSoTimeout(30000);client.setSoTimeout(30000);sockets.add(backend);final Socket target=backend;pool.submit(()->copy(target,client,false));copy(client,target,true);}catch(IOException ignored){}finally{if(backend!=null){sockets.remove(backend);try{backend.close();}catch(IOException ignored){}}sockets.remove(client);try{client.close();}catch(IOException ignored){}}}
  void copy(Socket from,Socket to,boolean track){try{InputStream in=from.getInputStream();OutputStream out=to.getOutputStream();byte[] data=new byte[8192];boolean first=true;int n;while((n=in.read(data))!=-1){if(track&&first){String line=new String(data,0,n,StandardCharsets.ISO_8859_1).split("\r\n",2)[0];paths.add(line);first=false;}out.write(data,0,n);out.flush();}to.shutdownOutput();}catch(IOException ignored){}}
  public void close()throws Exception{listener.close();for(Socket s:sockets)try{s.close();}catch(IOException ignored){}pool.shutdownNow();pool.awaitTermination(3,TimeUnit.SECONDS);}
 }
 static void idle(Forward forward)throws Exception{
  int quiet=0;for(int i=0;i<300;i++){SwingUtilities.invokeAndWait(()->{});if(jobs.isEmpty()&&(forward==null||forward.sockets.isEmpty()))quiet++;else quiet=0;if(quiet>=15)return;Thread.sleep(100);}throw new AssertionError("Background Studio requests did not become idle");
 }
 static void studioDrivers()throws Exception{
  CountDownLatch done=new CountDownLatch(1);Throwable[] failure={null};JdbcDriverDescriptor[][] result={null};
  Application.getBean("Server.JdbcInfoProvider",org.etlforge.integration.client.database.serverapi.JdbcInfoProvider.class).getDrivers(new JdbcDriverListCallback(){
   public void driversRetrieved(JdbcDriverDescriptor[] values,ServerInfo server){result[0]=values;done.countDown();}
   public void cancelled(){failure[0]=new AssertionError("JDBC request cancelled");done.countDown();}
   public void caught(Throwable error){failure[0]=error;done.countDown();}
  });if(!done.await(30,TimeUnit.SECONDS))throw new AssertionError("Studio JDBC timeout");if(failure[0]!=null)throw new Exception(failure[0]);ok(result[0]!=null&&result[0].length>0,"real Studio JDBC provider retrieves drivers through Apache");
 }
 public static void main(String[] args){try{run();System.exit(0);}catch(Throwable e){e.printStackTrace(System.out);System.exit(1);}}
 static void run()throws Exception{
  password=new String(Files.readAllBytes(Paths.get("C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt")),StandardCharsets.UTF_8).trim();StringBuilder h=new StringBuilder();for(byte b:MessageDigest.getInstance("MD5").digest(password.getBytes(StandardCharsets.UTF_8)))h.append(String.format("%02x",b&255));hash=h.toString();
  WsJdbcDriverInfo direct=drivers(47112,"Admin",hash),proxy=drivers(47100,"Admin",hash);ok(direct.equals(proxy),"typed JDBC driver response equals direct Tomcat baseline");
  boolean invalidDirect=false,invalidProxy=false;try{drivers(47112,"no-such-user","0");}catch(java.rmi.RemoteException expected){invalidDirect=true;}try{drivers(47100,"no-such-user","0");}catch(java.rmi.RemoteException expected){invalidProxy=true;}ok(invalidDirect&&invalidProxy,"Java authentication remains enforced on both paths");
  Path report=Paths.get("C:/ETL/Reports/studio-single-port");Files.createDirectories(report);Path root=Files.createTempDirectory(report,"studio-");IntegrationMain.main(new String[]{"-d",root.resolve("workspace").toString()});ClientPlatform.waitForInitialization(60,TimeUnit.SECONDS);Thread.sleep(2000);SwingUtilities.invokeAndWait(()->{});System.setOut(new PrintStream(report.resolve("studio-results.txt").toFile(),"UTF-8"));int errors=ErrorLog.getNumberOfErrors();
  org.etlforge.application.ui.ApplicationUi.getJobManager().addGlobalJobStatusListener(new org.etlforge.application.ui.job.JobStatusListener(){
   public void jobStarted(org.etlforge.application.ui.job.JobStatusChangedEvent e){jobs.add(e.getJob());}
   public void jobEnded(org.etlforge.application.ui.job.JobStatusChangedEvent e){jobs.remove(e.getJob());}
   public void statusChanged(org.etlforge.application.ui.job.JobStatusChangedEvent e){}
  });
  LoginService login=Application.getBean("platform.LoginService",LoginService.class);JavaWebServiceConfiguration javaConfig=Application.getBean("ws.config.Java",JavaWebServiceConfiguration.class);CppWebServiceConfiguration cpp=Application.getBean("ws.config.Cpp",CppWebServiceConfiguration.class);
  ok(javaConfig.getServerPorts()==cpp.getServerPorts(),"Java and native APIs share one configured port provider");
  login.login(new LoginCredentials(ServerAddressFactory.usingPorts(47100,46909),"127.0.0.1:47100","Admin",password,false,false));ok(javaConfig.getServerName().equals("127.0.0.1:47100"),"Java API retains login host and port");studioDrivers();idle(null);login.logout();idle(null);
  try(Forward tunnel=new Forward()){
   String address="127.0.0.1:"+tunnel.listener.getLocalPort();login.login(new LoginCredentials(ServerAddressFactory.usingPorts(47100,46909),address,"Admin",password,false,false));
   ok(cpp.getServerName().equals(address)&&javaConfig.getServerName().equals(address),"custom forwarded port is retained for both API types");studioDrivers();
   ok(tunnel.paths.stream().anyMatch(p->p.contains("/axis/konga_authenticate")),"native login traverses the single TCP forward");ok(tunnel.paths.stream().anyMatch(p->p.contains("/axis/services/konga_jdbc_infoprovider")),"Java request traverses the same TCP forward");idle(tunnel);login.logout();idle(tunnel);
  }
  for(String address:new String[]{"localhost:43123","server.example:443","127.0.0.1:47100"})ok(javaConfig.replaceJavaPort(address).equals(address),"legacy address adapter preserves "+address);
  login.getConfiguration().setUseHttps(true);StringBuilder endpoint=new StringBuilder();javaConfig.configureWebservice(new org.apache.axis.client.Service(),"konga_jdbc_infoprovider","server.example:443",endpoint);ok(endpoint.toString().equals("https://server.example:443/axis/services/konga_jdbc_infoprovider"),"HTTPS URL construction preserves gateway address");login.getConfiguration().setUseHttps(false);
  Thread.sleep(700);SwingUtilities.invokeAndWait(()->{});ok(ErrorLog.getNumberOfErrors()==errors,"no new Studio login/driver-cache errors");password=null;System.out.println("Passed "+checks+" installed Studio single-port checks; TCP forward used, actual SSH/TLS handshake not tested");for(java.awt.Window window:java.awt.Window.getWindows())window.dispose();
 }
}
