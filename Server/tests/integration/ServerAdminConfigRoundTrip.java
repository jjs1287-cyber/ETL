import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import com.etlforge.integration.server.implementation.webservice.serveradmin.client.*;
import org.etlforge.util.zip.ZipUtils;
public final class ServerAdminConfigRoundTrip {
 public static void main(String[] args)throws Exception {
  Path path=ServerAdminParity.HOME.resolve("etlforge.conf"),backup=ServerAdminParity.HOME.resolve("etlforge.conf.previous");byte[] original=Files.readAllBytes(path),oldBackup=Files.exists(backup)?Files.readAllBytes(backup):null;
  String password=new String(Files.readAllBytes(Paths.get("C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt")),StandardCharsets.UTF_8).trim(),hash=org.etlforge.util.crypto.DigestUtils.getMd5DigestAsHexString(password);password=null;
  ServerAdministration s=ServerAdminParity.service("http://127.0.0.1:47100/axis/konga_serveradmin");long modified=Files.getLastModifiedTime(path).toMillis();
  try {
   s.uploadServerConfigurationFile("Admin",hash,ZipUtils.deflateAndBase64EncodeBytes(original));ServerAdminParity.check(Files.getLastModifiedTime(path).toMillis()==modified,"Identical upload rewrote file");
   byte[] marker="\r\n# Native admin round-trip verification: caf\u00e9 \u4e2d\u6587 \ud83d\ude80\r\n".getBytes(StandardCharsets.UTF_8),changed=Arrays.copyOf(original,original.length+marker.length);System.arraycopy(marker,0,changed,original.length,marker.length);
   s.uploadServerConfigurationFile("Admin",hash,ZipUtils.deflateAndBase64EncodeBytes(changed));ServerAdminParity.check(Arrays.equals(Files.readAllBytes(path),changed),"Changed upload bytes differ");ServerAdminParity.check(Arrays.equals(Files.readAllBytes(backup),original),"Backup did not preserve original bytes");ServerAdminParity.check(Arrays.equals(ServerAdminParity.unpack(s.downloadServerConfigurationFile("Admin",hash)),changed),"Changed config download differs");
  }finally{s.uploadServerConfigurationFile("Admin",hash,ZipUtils.deflateAndBase64EncodeBytes(original));if(!Arrays.equals(Files.readAllBytes(path),original))throw new AssertionError("Original configuration not restored");if(oldBackup==null)Files.deleteIfExists(backup);else Files.write(backup,oldBackup);}
  ServerAdminParity.check(Arrays.equals(Files.readAllBytes(path),original),"Final original bytes differ");System.out.println("PASS: "+ServerAdminParity.checks+" live configuration checks; no-op timestamp, UTF8 change, backup, download, original/previous-backup restoration. No service restart requested.");
 }
}
