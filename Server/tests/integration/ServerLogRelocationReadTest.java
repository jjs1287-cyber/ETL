import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.net.URL;
import com.etlforge.integration.server.implementation.webservice.serveradmin.client.*;
import org.etlforge.util.zip.ZipUtils;

public final class ServerLogRelocationReadTest {
 public static void main(String[] args)throws Exception{
  String password=new String(Files.readAllBytes(Paths.get("C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt")),StandardCharsets.UTF_8).trim();
  StringBuilder hash=new StringBuilder();for(byte b:MessageDigest.getInstance("MD5").digest(password.getBytes(StandardCharsets.UTF_8)))hash.append(String.format("%02x",b&255));password=null;
  ServerAdministration service=new ServerAdministrationServiceLocator().getkonga_serveradmin(new URL("http://localhost:47100/axis/konga_serveradmin"));((org.apache.axis.client.Stub)service).setTimeout(15000);
  WsServerFileLocators[] lists={service.listServerLogFiles("Admin",hash.toString()),service.listPluginLogFiles("Admin",hash.toString()),service.listOperationLogFiles("Admin",hash.toString(),null,null),service.listDebugLogFiles("Admin",hash.toString(),null,null)};
  int checks=0;String[] names={"server","plugin","operation","debug"};
  for(int i=0;i<lists.length;i++){
   if(lists[i]==null)throw new AssertionError("Missing "+names[i]+" list");checks++;System.out.println("PASS list "+names[i]);
   for(WsServerFileLocator locator:lists[i].getLocators()==null?new WsServerFileLocator[0]:lists[i].getLocators())if(locator.getSizeInBytes()<=1024*1024){
    Path path=Paths.get(locator.getPath());byte[] before=Files.readAllBytes(path);byte[] actual=ZipUtils.base64DecodeAndInflateStringAsBytes(service.downloadFile("Admin",hash.toString(),path.toString()));byte[] after=Files.readAllBytes(path);
    if(Arrays.equals(before,after)&&!Arrays.equals(before,actual))throw new AssertionError("Changed bytes in "+names[i]+" download");checks++;System.out.println("PASS download "+names[i]);break;
   }
  }
  boolean denied=false;try{service.downloadFile("Admin",hash.toString(),"C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt");}catch(java.rmi.RemoteException expected){denied=true;}if(!denied)throw new AssertionError("Private path allowed");checks++;
  System.out.println("PASS "+checks+" installed server log relocation read checks; no files or configuration changed");
 }
}
