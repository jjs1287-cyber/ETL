import java.net.URL;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.io.*;
import java.net.HttpURLConnection;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.*;

/** Deploys the user's hello-world script shape through the real v5 Studio SOAP stubs. */
public final class StudioScriptDeploySmoke {
 static InterchangeDataTable table(String[] columns, String[]... values) {
  InterchangeDataRow[] rows = new InterchangeDataRow[values.length];
  for (int i=0;i<values.length;i++) rows[i]=new InterchangeDataRow(values[i]);
  return new InterchangeDataTable(columns,rows);
 }
 public static void main(String[] args) throws Exception {
  String password=new String(Files.readAllBytes(Paths.get("C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt")),StandardCharsets.UTF_8).trim();
  StringBuilder hash=new StringBuilder();
  for(byte b:MessageDigest.getInstance("MD5").digest(password.getBytes(StandardCharsets.UTF_8))) hash.append(String.format("%02x",b&255));
  String project=UUID.randomUUID().toString(), script=UUID.randomUUID().toString(), operation=UUID.randomUUID().toString();
  String marker="ETLForge script deploy smoke "+project;
  String content="<trans>\nWriteToOperationLog('"+marker+"');\n</trans>";
  InterchangeDataHolder data=new InterchangeDataHolder();
  data.setInterchangeProjectsTab(table(new String[]{"InterchangeProject_ID","InterchangeProject_Name"},new String[]{project,"ETLForge Script Smoke "+project.substring(0,8)}));
  data.setScriptsTab(table(new String[]{"Script_ID","Script_Name","Script_Text"},new String[]{script,"Hello world",Base64.getEncoder().encodeToString(content.getBytes(StandardCharsets.UTF_8))}));
  data.setOperationsTab(table(new String[]{"Operation_ID","Operation_Name","Operation_Type_ID","Source_ID","Target_ID","Schedule_ID","Transformation_ID","Success_Operation_ID","Success_Message_ID","Failure_Operation_ID","Failure_Message_ID"},new String[]{operation,"Hello world operation","8",null,null,null,null,null,null,null,null}));
  data.setOperationPipelines(table(new String[]{"Operation_ID","ActivityType_ID","Activity_ID"},new String[]{operation,"1",script}));
  DeployInterchangeData deploy=new DeployInterchangeDataServiceLocator().getkonga_deploy_interchange_data(new URL("http://127.0.0.1:47100/axis/konga_deploy_interchange_data"));
  ((org.apache.axis.client.Stub)deploy).setTimeout(30000);
  CDeployResults result=deploy.deployInterchangeData("Admin",hash.toString(),new InterchangeIdHolder(),data);
  if(result==null || result.getDeployStatus()>1) throw new AssertionError("Script deployment failed");
  Files.write(Paths.get("C:/ETL/Reports/native-script-job-manifest.json"),("{\"projectGuid\":\""+project+"\",\"scriptGuid\":\""+script+"\",\"operationGuid\":\""+operation+"\",\"marker\":\""+marker+"\"}").getBytes(StandardCharsets.UTF_8));
  System.out.println("PASS: real Studio SOAP client deployed a project, hello-world script, script operation and pipeline to the native x64 service");
  // Replay the same deployment with v5-only fields omitted, as a v4 client does.
  // Keep the request containing authentication data in memory only.
  String request=((org.apache.axis.client.Stub)deploy)._getCall().getMessageContext().getRequestMessage().getSOAPPartAsString();
  DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();factory.setNamespaceAware(true);
  factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true);
  Document document=factory.newDocumentBuilder().parse(new ByteArrayInputStream(request.getBytes(StandardCharsets.UTF_8)));
  Set<String> extras=new HashSet<>(Arrays.asList("DocumentFieldValidationTab","SalesforceConnectorsTab","SapEndpointsTab","SapFunctionsTab","MSCrmEndpointsTab","MSCrmFunctionsTab","MSAXEndpointsTab","MSAXFunctionsTab","MSGPEndpointsTab","MSGPFunctionsTab","EbsEndpointsTab","EbsFunctionsTab","AutoDeskEndpointsTab","AutoDeskFunctionsTab","NetSuiteEndpointsTab","NetSuiteFunctionsTab"));
  NodeList nodes=document.getElementsByTagNameNS("*","*");
  for(int i=nodes.getLength()-1;i>=0;i--){Node node=nodes.item(i);String name=node.getLocalName();if(extras.contains(name)||name.endsWith("_ids")&&extras.contains(name.substring(0,name.length()-4)))node.getParentNode().removeChild(node);}
  ByteArrayOutputStream wire=new ByteArrayOutputStream();
  TransformerFactory.newInstance().newTransformer().transform(new DOMSource(document),new StreamResult(wire));
  HttpURLConnection connection=(HttpURLConnection)new URL("http://127.0.0.1:47100/axis/konga_deploy_interchange_data").openConnection();
  connection.setConnectTimeout(10000);connection.setReadTimeout(30000);connection.setRequestMethod("POST");connection.setDoOutput(true);
  connection.setRequestProperty("Content-Type","text/xml; charset=utf-8");connection.setRequestProperty("SOAPAction","deployInterchangeData");
  try(OutputStream stream=connection.getOutputStream()){stream.write(wire.toByteArray());}
  if(connection.getResponseCode()!=200)throw new AssertionError("Legacy field-omission replay failed with HTTP "+connection.getResponseCode());
  Document reply=factory.newDocumentBuilder().parse(connection.getInputStream());connection.disconnect();
  if(reply.getElementsByTagNameNS("http://schemas.xmlsoap.org/soap/envelope/","Fault").getLength()!=0)throw new AssertionError("Legacy field-omission replay returned a SOAP fault");
  System.out.println("PASS: existing deployment can be updated with v5-only fields omitted (v4 wire compatibility)");
  // A populated unsupported connector must fail explicitly, without dropping data.
  InterchangeDataHolder unsupported=new InterchangeDataHolder();
  unsupported.setSalesforceConnectorsTab(table(new String[]{"ID"},new String[]{UUID.randomUUID().toString()}));
  try {
   deploy.deployInterchangeData("Admin",hash.toString(),new InterchangeIdHolder(),unsupported);
   throw new AssertionError("Unsupported connector data was silently accepted");
  } catch (java.rmi.RemoteException expected) {
   if(!expected.getMessage().contains("does not yet support deployment field SalesforceConnectorsTab")) throw expected;
  }
  System.out.println("PASS: populated unsupported v5 connector data is rejected with an explicit SOAP fault");
  InterchangeIdHolder unsupportedDeletes=new InterchangeIdHolder();
  unsupportedDeletes.setSapEndpointsTab_ids(new String[]{UUID.randomUUID().toString()});
  try {
   deploy.deployInterchangeData("Admin",hash.toString(),unsupportedDeletes,new InterchangeDataHolder());
   throw new AssertionError("Unsupported connector deletion was silently accepted");
  } catch(java.rmi.RemoteException expected) {
   if(!expected.getMessage().contains("does not yet support deployment field SapEndpointsTab_ids"))throw expected;
  }
  System.out.println("PASS: unsupported connector deletions are rejected before deployment changes");
 }
}
