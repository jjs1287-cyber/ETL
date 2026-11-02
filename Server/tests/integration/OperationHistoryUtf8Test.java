import java.net.URL;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import org.etlforge.integration.server.implementation.webservice.activity.client.*;
public final class OperationHistoryUtf8Test {
 public static void main(String[] args)throws Exception {
  String password=new String(Files.readAllBytes(Paths.get("C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt")),StandardCharsets.UTF_8).trim();StringBuilder hash=new StringBuilder();for(byte b:MessageDigest.getInstance("MD5").digest(password.getBytes(StandardCharsets.UTF_8)))hash.append(String.format("%02x",b&255));
  ActivityInfoProvider service=new ActivityInfoProviderServiceLocator().getkonga_activity_infoprovider(new URL("http://127.0.0.1:47100/axis/konga_activity_infoprovider"));((org.apache.axis.client.Stub)service).setTimeout(60000);
  WsOperationInstanceFilter filter=new WsOperationInstanceFilter(new long[]{81},new String[0],new int[0],new int[]{3,4,5,6},"","","","",100,-1);
  WsOperationLogInstance[] rows=service.getOperationLog("Admin",hash.toString(),filter);if(rows==null||rows.length==0)throw new AssertionError("Operation history returned no records");
  boolean unicode=false;for(WsOperationLogInstance row:rows){WsOperationMessage[] messages=row.getOperationMessages();System.out.println("Instance="+row.getOperationInstanceId()+" auxiliary messages="+(messages==null?0:messages.length));if(messages!=null)for(WsOperationMessage message:messages)if(message.getMessage()!=null&&message.getMessage().contains("caf\u00e9 \u20ac \u4e2d\u6587 \ud83d\ude00"))unicode=true;}
  if(!unicode)throw new AssertionError("Stored Unicode operation message was not preserved");
  filter.setOperationInstances(new long[0]);rows=service.getOperationLog("Admin",hash.toString(),filter);if(rows==null||rows.length==0)throw new AssertionError("All-history retrieval failed");
  filter.setOperationInstances(new long[]{79});rows=service.getOperationLog("Admin",hash.toString(),filter);boolean large=false;for(WsOperationLogInstance row:rows)if(row.getOperationMessages()!=null)for(WsOperationMessage message:row.getOperationMessages())if("Cloud logging size verification".equals(message.getFieldName())){String text=message.getMessage();if(text.codePointCount(0,text.length())!=300000)throw new AssertionError("Long Unicode message was truncated");for(int offset=0;offset<text.length();){int code=text.codePointAt(offset);if(code!=0x1f600)throw new AssertionError("Long Unicode message changed");offset+=Character.charCount(code);}large=true;}
  if(!large)throw new AssertionError("Long Unicode history message was not returned");
  filter.setOperationInstances(new long[]{9223372036854775807L});rows=service.getOperationLog("Admin",hash.toString(),filter);if(rows!=null&&rows.length!=0)throw new AssertionError("Missing-instance filter returned unrelated records");
  System.out.println("PASS: operation-history SOAP response parses; stored multilingual/emoji messages preserved; instance and missing-instance filters work; 300,000 emoji message preserved without truncation.");
 }
}
