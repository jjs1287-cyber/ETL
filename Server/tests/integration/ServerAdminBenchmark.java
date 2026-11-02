import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import com.etlforge.integration.server.implementation.webservice.serveradmin.client.*;
public final class ServerAdminBenchmark {
 interface Action {Object run(ServerAdministration service)throws Exception;}
 static final int COUNT=31;
 static String hash;
 static List<String> csv=new ArrayList<>();
 static void measure(String method,ServerAdministration java,ServerAdministration nativeApi,Action action)throws Exception{
  for(int i=0;i<4;i++){action.run(java);action.run(nativeApi);}double[][] samples=new double[2][COUNT];ServerAdministration[] services={java,nativeApi};
  for(int i=0;i<COUNT;i++)for(int turn=0;turn<2;turn++){int backend=(i+turn)%2;long start=System.nanoTime();action.run(services[backend]);samples[backend][i]=(System.nanoTime()-start)/1e6;}
  for(int backend=0;backend<2;backend++){Arrays.sort(samples[backend]);csv.add(method+","+(backend==0?"Java":"Native")+","+COUNT+","+samples[backend][COUNT/2]+","+samples[backend][(int)Math.ceil(COUNT*.95)-1]);}
 }
 public static void main(String[] args)throws Exception {
  String password=new String(Files.readAllBytes(Paths.get("C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt")),StandardCharsets.UTF_8).trim();StringBuilder digest=new StringBuilder();for(byte b:MessageDigest.getInstance("MD5").digest(password.getBytes(StandardCharsets.UTF_8)))digest.append(String.format("%02x",b&255));hash=digest.toString();password=null;
  ServerAdministration java=ServerAdminParity.service("http://127.0.0.1:47112/axis/services/konga_serveradmin"),nativeApi=ServerAdminParity.service("http://127.0.0.1:47100/axis/konga_serveradmin");
  measure("configuration-download",java,nativeApi,s->s.downloadServerConfigurationFile("Admin",hash));measure("server-list",java,nativeApi,s->s.listServerLogFiles("Admin",hash));measure("plugin-list",java,nativeApi,s->s.listPluginLogFiles("Admin",hash));measure("operation-list",java,nativeApi,s->s.listOperationLogFiles("Admin",hash,null,null));measure("debug-list",java,nativeApi,s->s.listDebugLogFiles("Admin",hash,null,null));
  measure("file-download-4MiB-compressible",java,nativeApi,s->s.downloadFile("Admin",hash,"C:/ETL/Server/instances/dev/log/native-admin-fixture-3-\u00e9\u4e2d\u6587.LOG"));
  measure("file-download-256KiB-binary",java,nativeApi,s->s.downloadFile("Admin",hash,"C:/ETL/Server/instances/dev/log/native-admin-fixture-2-\u00e9\u4e2d\u6587.bin"));
  csv.add(0,"method,backend,iterations,median_ms,p95_ms");Files.write(Paths.get("C:/ETL/Reports/native-server-admin-paired-timing.csv"),csv,StandardCharsets.UTF_8);System.out.println("PASS: 434 timed paired requests plus warmups; alternating backend order; no configuration writes in paired benchmark.");
 }
}
