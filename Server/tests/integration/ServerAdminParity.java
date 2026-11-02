import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.io.*;
import com.etlforge.integration.server.implementation.webservice.serveradmin.client.*;
import org.etlforge.util.zip.ZipUtils;

public final class ServerAdminParity {
 static final Path HOME=Paths.get("C:/ETL/Server/instances/dev"), REPORTS=Paths.get("C:/ETL/Reports");
 static final String PREFIX="native-admin-fixture-";
 static String hash; static int checks; static List<String> snapshot=new ArrayList<>(),times=new ArrayList<>();
 interface Action {Object run() throws Exception;}
 static void check(boolean condition,String text){if(!condition)throw new AssertionError(text);checks++;}
 static String sha(byte[] bytes)throws Exception {StringBuilder result=new StringBuilder();for(byte b:MessageDigest.getInstance("SHA-256").digest(bytes))result.append(String.format("%02x",b&255));return result.toString();}
 static void rejected(Action action,String text)throws Exception {boolean rejected=false;try{action.run();}catch(java.rmi.RemoteException expected){rejected=true;}check(rejected,text);}
 static byte[] unpack(String s)throws Exception{return ZipUtils.base64DecodeAndInflateStringAsBytes(s);}
 static ServerAdministration service(String endpoint)throws Exception{ServerAdministration s=new ServerAdministrationServiceLocator().getkonga_serveradmin(new URL(endpoint));((org.apache.axis.client.Stub)s).setTimeout(30000);return s;}
 static final String[] DIRS={"log","log/plugin","DataInterchange/Temp/LOG","DataInterchange/Temp/Debug"};
 static void setup()throws Exception {
  byte[][] content={new byte[0],"caf\u00e9 \u4e2d\u6587 \ud83d\ude80\r\nsecond line\n".getBytes(StandardCharsets.UTF_8),new byte[262144],new byte[4194304]};
  Random random=new Random(42);random.nextBytes(content[2]);Arrays.fill(content[3],(byte)'x');
  for(String dir:DIRS){Path root=HOME.resolve(dir);Files.createDirectories(root);for(int i=0;i<content.length;i++){Path p=root.resolve(PREFIX+i+"-\u00e9\u4e2d\u6587"+(i==2?".bin":i==3?".LOG":".log"));Files.write(p,content[i]);Files.setLastModifiedTime(p,FileTime.fromMillis(1767225600000L+i*1000));}Files.createDirectories(root.resolve(PREFIX+"directory.log"));}
 }
 static void list(String name,WsServerFileLocators result,boolean logsOnly)throws Exception {
  check(result!=null,"Missing "+name+" response");int fixtures=0;
  for(WsServerFileLocator file:result.getLocators()==null?new WsServerFileLocator[0]:result.getLocators()) {
   if(!Paths.get(file.getPath()).getFileName().toString().startsWith(PREFIX))continue;
   Path p=Paths.get(file.getPath());check(Files.isRegularFile(p),"Listed directory");check(file.getSizeInBytes()==Files.size(p),"Wrong size");
   check(!logsOnly||p.toString().toLowerCase(Locale.ROOT).endsWith(".log"),"Wrong extension");
   snapshot.add(name+"|"+p.toAbsolutePath().normalize()+"|"+file.getLastModified()+"|"+file.getSizeInBytes());fixtures++;
  }
  check(fixtures==(logsOnly?3:4),"Wrong fixture count "+name+"="+fixtures);
 }
 static void bench(String label,int count,Action action)throws Exception {
  for(int i=0;i<2;i++)action.run();double[] samples=new double[count];
  for(int i=0;i<count;i++){long started=System.nanoTime();action.run();samples[i]=(System.nanoTime()-started)/1e6;}
  Arrays.sort(samples);times.add(label+","+count+","+samples[count/2]+","+samples[(int)Math.ceil(count*.95)-1]);
 }
 public static void main(String[] args)throws Exception {
  String mode=args[0]; if(mode.equals("setup")){setup();System.out.println("Owned fixtures created.");return;}
  if(mode.equals("cleanup")){for(String dir:DIRS)try(DirectoryStream<Path> files=Files.newDirectoryStream(HOME.resolve(dir),PREFIX+"*")){for(Path p:files)Files.delete(p);}return;}
  String password=new String(Files.readAllBytes(Paths.get("C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt")),StandardCharsets.UTF_8).trim();StringBuilder digest=new StringBuilder();for(byte b:MessageDigest.getInstance("MD5").digest(password.getBytes(StandardCharsets.UTF_8)))digest.append(String.format("%02x",b&255));hash=digest.toString();password=null;
  ServerAdministration s=service(args[1]);
  byte[] config=Files.readAllBytes(HOME.resolve("etlforge.conf"));check(Arrays.equals(config,unpack(s.downloadServerConfigurationFile("Admin",hash))),"Configuration bytes differ");snapshot.add("config|"+sha(config));
  list("server",s.listServerLogFiles("Admin",hash),true);list("plugin",s.listPluginLogFiles("Admin",hash),true);list("operation",s.listOperationLogFiles("Admin",hash,null,null),false);list("debug",s.listDebugLogFiles("Admin",hash,null,null),false);
  for(String dir:DIRS)try(DirectoryStream<Path> entries=Files.newDirectoryStream(HOME.resolve(dir),PREFIX+"*")) {for(Path p:entries)if(Files.isRegularFile(p)){byte[] got=unpack(s.downloadFile("Admin",hash,p.toString()));check(Arrays.equals(got,Files.readAllBytes(p)),"Download bytes differ "+p);snapshot.add("download|"+p+"|"+sha(got));}}
  WsServerFileLocator[] filtered=s.listOperationLogFiles("Admin",hash,"2026-01-01 00:00:01","2026-01-01 00:00:02").getLocators();int matches=0;for(WsServerFileLocator f:filtered==null?new WsServerFileLocator[0]:filtered)if(f.getPath().contains(PREFIX)){check(f.getLastModified().equals("2026-01-01 00:00:01")||f.getLastModified().equals("2026-01-01 00:00:02"),"Date endpoint wrong");matches++;}check(matches==2,"Inclusive date endpoints failed");
  check(s.listDebugLogFiles("Admin",hash,"2000-01-01 00:00:00","2000-01-02 00:00:00").getLocators().length==0,"Empty date range failed");
  rejected(()->s.listDebugLogFiles("Admin",hash,"2026-01-02 00:00:00","2026-01-01 00:00:00"),"Reversed dates accepted");
  rejected(()->s.listOperationLogFiles("Admin",hash,"bad-date",null),"Malformed date accepted");
  rejected(()->s.downloadFile("Admin",hash,HOME.resolve("log/missing-native-admin-fixture.log").toString()),"Missing file accepted");
  rejected(()->s.downloadServerConfigurationFile("Admin","invalid"),"Bad auth accepted");
  rejected(()->{s.uploadServerConfigurationFile("Admin","invalid",ZipUtils.deflateAndBase64EncodeBytes(config));return null;},"Bad upload auth accepted");
  String upload=ZipUtils.deflateAndBase64EncodeBytes(config);
  if(mode.equals("java")){try{long start=System.nanoTime();s.uploadServerConfigurationFile("Admin",hash,upload);times.add("configuration-upload-identical,1,"+(System.nanoTime()-start)/1e6+",NaN");Thread.sleep(3000);byte[] saved=Files.readAllBytes(HOME.resolve("etlforge.conf"));System.out.println("Legacy identical upload preserved bytes: "+Arrays.equals(config,saved)+"; original="+config.length+", saved="+saved.length+". Restoring archived bytes.");}finally{Files.write(HOME.resolve("etlforge.conf"),config);}}
  else bench("configuration-upload-identical",15,()->{s.uploadServerConfigurationFile("Admin",hash,upload);return null;});
  check(Arrays.equals(config,Files.readAllBytes(HOME.resolve("etlforge.conf"))),"Config upload changed original");
  Path sample=HOME.resolve("log/"+PREFIX+"3-\u00e9\u4e2d\u6587.LOG");
  bench("configuration-download",15,()->s.downloadServerConfigurationFile("Admin",hash));bench("server-list",15,()->s.listServerLogFiles("Admin",hash));bench("plugin-list",15,()->s.listPluginLogFiles("Admin",hash));bench("operation-list",15,()->s.listOperationLogFiles("Admin",hash,null,null));bench("debug-list",15,()->s.listDebugLogFiles("Admin",hash,null,null));bench("file-download-4MiB-compressible",15,()->s.downloadFile("Admin",hash,sample.toString()));
  if(!mode.equals("java")){
   rejected(()->s.downloadFile("Admin",hash,"C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt"),"Private path accepted");
   rejected(()->s.downloadFile("Admin",hash,HOME.toString()+"/etlforge.conf:stream"),"ADS accepted");
   rejected(()->s.downloadFile("Admin",hash,"\\\\127.0.0.1\\c$\\Windows\\win.ini"),"UNC accepted");
   rejected(()->s.downloadFile("Admin",hash,HOME.resolve("log/../etlforge.conf").toString()),"Traversal accepted");
   rejected(()->{s.uploadServerConfigurationFile("Admin",hash,"not-base64");return null;},"Malformed upload accepted");
   rejected(()->{s.uploadServerConfigurationFile("Admin",hash,ZipUtils.deflateAndBase64EncodeBytes(new byte[]{(byte)0xc3,0x28}));return null;},"Invalid UTF8 accepted");
   rejected(()->{s.uploadServerConfigurationFile("Admin",hash,ZipUtils.deflateAndBase64EncodeString("[broken\nvalue\n"));return null;},"Invalid INI accepted");
   check(Arrays.equals(config,Files.readAllBytes(HOME.resolve("etlforge.conf"))),"Rejected upload changed config");
  }
  Collections.sort(snapshot);Files.write(REPORTS.resolve("native-server-admin-"+mode+"-results.txt"),snapshot,StandardCharsets.UTF_8);times.add(0,"method,iterations,median_ms,p95_ms");Files.write(REPORTS.resolve("native-server-admin-"+mode+"-timing.csv"),times,StandardCharsets.UTF_8);
  if(!mode.equals("java"))check(snapshot.equals(Files.readAllLines(REPORTS.resolve("native-server-admin-java-results.txt"),StandardCharsets.UTF_8)),"Baseline mismatch");
  System.out.println("PASS: "+checks+" checks, mode="+mode+"; result snapshot and request timings saved.");
 }
}
