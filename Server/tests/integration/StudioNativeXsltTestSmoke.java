import java.net.URL;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.*;
public class StudioNativeXsltTestSmoke {
 static String quote(String s){return "\""+s.replace("\\","\\\\").replace("\"","\\\"").replace("\r","\\r").replace("\n","\\n")+"\"";}
 public static void main(String[] args)throws Exception{
  String password=new String(Files.readAllBytes(Paths.get("C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt")),StandardCharsets.UTF_8).trim();
  StringBuilder hash=new StringBuilder();for(byte b:MessageDigest.getInstance("MD5").digest(password.getBytes(StandardCharsets.UTF_8)))hash.append(String.format("%02x",b&255));
  TreeMapperInfoProvider api=new TreeMapperInfoProviderServiceLocator().getkonga_treemapper_infoprovider(new URL("http://127.0.0.1:47100/axis/konga_treemapper_infoprovider"));
  ((org.apache.axis.client.Stub)api).setTimeout(30000);
  DebuggingParameters params=new DebuggingParameters();params.setCommand("Run");params.setDebuggingSessionId(UUID.randomUUID().toString());params.setBreakpoints(new Breakpoint[0]);params.setGlobalVariables(new WsDataElement[0]);
  String style=new String(Files.readAllBytes(Paths.get("C:/ETL/Samples/XSLT/ExpandEmbeddedXml/transform.xsl")),StandardCharsets.UTF_8);
  String input=new String(Files.readAllBytes(Paths.get("C:/ETL/Samples/XSLT/ExpandEmbeddedXml/input.xml")),StandardCharsets.UTF_8);
  String expression="<trans>output=RunXSLT("+quote(style)+","+quote(input)+");output[0]</trans>";
  WsTestResult result=api.testExpression("Admin",hash.toString(),"",Base64.getEncoder().encodeToString(expression.getBytes(StandardCharsets.UTF_8)),params);
  if(result.getErrors()!=null&&!result.getErrors().isEmpty())throw new AssertionError(result.getErrors());
  String output=new String(Base64.getMimeDecoder().decode(result.getResult()),StandardCharsets.UTF_8);
  if(!output.contains("<CustomerPartyMaster>")||output.contains("&lt;?xml"))throw new AssertionError("Unexpected test result: "+output.substring(0,Math.min(200,output.length())));
  System.out.println("PASS: Studio Test endpoint executes RunXSLT using the native backend and returns expanded XML");
 }
}
