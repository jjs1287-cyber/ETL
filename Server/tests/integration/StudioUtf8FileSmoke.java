import java.net.URL;
import java.nio.file.*;
import java.nio.charset.*;
import java.security.MessageDigest;
import java.util.*;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.*;
import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.*;

public final class StudioUtf8FileSmoke {
 static String hash,project=UUID.randomUUID().toString();static int checks;
 static TreeMapperInfoProvider tests;
 static InterchangeDataTable table(String[] columns,String[]...values){InterchangeDataRow[] rows=new InterchangeDataRow[values.length];for(int i=0;i<rows.length;i++)rows[i]=new InterchangeDataRow(values[i]);return new InterchangeDataTable(columns,rows);}
 static String quote(String value){return "\""+value.replace("\\","\\\\").replace("\"","\\\"").replace("\r","\\r").replace("\n","\\n")+"\"";}
 static String expression(String body)throws Exception {
  DebuggingParameters params=new DebuggingParameters();params.setCommand("Run");params.setDebuggingSessionId(UUID.randomUUID().toString());params.setBreakpoints(new Breakpoint[0]);params.setGlobalVariables(new WsDataElement[0]);
  WsTestResult result=tests.testExpression("Admin",hash,project,Base64.getEncoder().encodeToString(("<trans>"+body+"</trans>").getBytes(StandardCharsets.UTF_8)),params);
  if(result.getErrors()!=null&&!result.getErrors().isEmpty())throw new AssertionError(result.getErrors());
  return new String(Base64.getMimeDecoder().decode(result.getResult()),StandardCharsets.UTF_8);
 }
 static void require(boolean value,String message){if(!value)throw new AssertionError(message);checks++;}
 public static void main(String[] args)throws Exception {
  String password=new String(Files.readAllBytes(Paths.get("C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt")),StandardCharsets.UTF_8).trim();StringBuilder digest=new StringBuilder();for(byte b:MessageDigest.getInstance("MD5").digest(password.getBytes(StandardCharsets.UTF_8)))digest.append(String.format("%02x",b&255));hash=digest.toString();password=null;
  DeployInterchangeData deploy=new DeployInterchangeDataServiceLocator().getkonga_deploy_interchange_data(new URL("http://127.0.0.1:47100/axis/konga_deploy_interchange_data"));
  tests=new TreeMapperInfoProviderServiceLocator().getkonga_treemapper_infoprovider(new URL("http://127.0.0.1:47100/axis/konga_treemapper_infoprovider"));((org.apache.axis.client.Stub)tests).setTimeout(60000);
  Path root=Paths.get("C:/ETL/Server/instances/dev/DataInterchange/TemporaryStorage/integration-smoke/utf8-"+project);Files.createDirectories(root);Path output=root.resolve("output");Files.createDirectories(output);
  String unicode="caf\u00e9 \u20ac \u4e2d\u6587 \ud83d\ude00";
  String xml="<?xml version='1.0' encoding='UTF-16'?><r>"+unicode+"</r>";
  String latinXml="<?xml version='1.0' encoding='windows-1252'?><r>caf\u00e9 \u20ac</r>";
  byte[][] bytes={unicode.getBytes(StandardCharsets.UTF_8),xml.getBytes(StandardCharsets.UTF_16),latinXml.getBytes(Charset.forName("windows-1252")),new byte[]{0,(byte)255,(byte)192,(byte)175,13,10},"caf\u00e9 \u20ac".getBytes(Charset.forName("windows-1252"))};
  String[] ids=new String[bytes.length];String[][] rows=new String[bytes.length][];
  for(int i=0;i<bytes.length;i++){ids[i]=UUID.randomUUID().toString();Files.write(root.resolve("input"+i+".dat"),bytes[i]);rows[i]=new String[]{ids[i],"UTF8 verification "+i,"16",root.toString(),"input"+i+".dat",null,null,null,null,null};}
  String target=UUID.randomUUID().toString();InterchangeDataHolder data=new InterchangeDataHolder();
  InterchangeDataTable projectTable=table(new String[]{"InterchangeProject_ID","InterchangeProject_Name"},new String[]{project,"UTF8 file verification "+project.substring(0,8)});data.setInterchangeProjectsTab(projectTable);
  data.setSourcesTab(table(new String[]{"Source_ID","Source_Name","Source_Type_ID","Locator","File_Name","Login","Password","Driver","DriverType","ConnectString"},rows));
  data.setTargetsTab(table(new String[]{"Target_ID","Target_Name","Source_Type_ID","Locator","File_Name","Login","Password","Driver","DriverType","ConnectString","Target_Target_ID"},new String[]{target,"UTF8 verification target","16",output.toString(),"result.txt",null,null,null,null,null,null}));
  CDeployResults deployed=deploy.deployInterchangeData("Admin",hash,new InterchangeIdHolder(),data);require(deployed!=null&&deployed.getDeployStatus()<=1,"Fixture deployment failed");
  try {
   require(expression("ReadFile("+quote("s."+ids[0])+")").equals(unicode),"UTF8 ReadFile changed multilingual text");
   require(expression("ReadFile("+quote("s."+ids[1])+")").equals(xml.replace("UTF-16","UTF-8")),"UTF16 XML did not normalize to UTF8");
   require(expression("ReadFile("+quote("s."+ids[2])+")").equals(latinXml.replace("windows-1252","UTF-8")),"XML encoding declaration was not honored");
   require(Arrays.equals(Base64.getMimeDecoder().decode(expression("Base64EncodeFile("+quote("s."+ids[3])+")")),bytes[3]),"Binary file bytes changed");
   require(expression("$etlforge.source.text.character_encoding=\"windows-1252\";ReadFile("+quote("s."+ids[4])+")").equals("caf\u00e9 \u20ac"),"Explicit source encoding was not honored");
   String body="WriteFile("+quote("t."+target)+","+quote(unicode)+");FlushFile("+quote("t."+target)+");\"ok\"";require(expression(body).equals("ok"),"WriteFile/FlushFile failed");
   require(Arrays.equals(Files.readAllBytes(output.resolve("result.txt")),unicode.getBytes(StandardCharsets.UTF_8)),"Target file bytes are not UTF8");
   String xmlOutput="<?xml version='1.0' encoding='UTF-16'?><r>"+unicode+"</r>";
   require(expression("WriteFile("+quote("t."+target)+","+quote(xmlOutput)+",\"xml.xml\");FlushFile("+quote("t."+target)+",\"xml.xml\");\"ok\"").equals("ok"),"XML text write failed");
   require(Arrays.equals(Files.readAllBytes(output.resolve("xml.xml")),xmlOutput.replace("UTF-16","UTF-8").getBytes(StandardCharsets.UTF_8)),"XML text target declaration does not match UTF8 bytes");
   String style="<xsl:stylesheet version='3.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform'><xsl:output encoding='UTF-16'/><xsl:template match='/'><r><xsl:value-of select='/r'/></r></xsl:template></xsl:stylesheet>";
   String transformed=expression("x=RunXSLT("+quote(style)+","+quote("<r>"+unicode+"</r>")+");x[0]");require(transformed.contains("encoding=\"UTF-8\"")&&transformed.contains(unicode),"RunXSLT serializer did not force UTF8");
   for(int i=0;i<bytes.length;i++)require(Arrays.equals(Files.readAllBytes(root.resolve("input"+i+".dat")),bytes[i]),"Original source was modified");
   System.out.println("PASS "+checks+" live UTF8 checks: ReadFile, Unicode BOM, XML declaration, explicit source charset, binary preservation, UTF8 target, XSLT serialization.");
  } finally {
   InterchangeDataHolder empty=new InterchangeDataHolder();empty.setInterchangeProjectsTab(projectTable);InterchangeIdHolder deleted=new InterchangeIdHolder();deleted.setSourcesTab_ids(ids);deleted.setTargetsTab_ids(new String[]{target});deploy.deployInterchangeData("Admin",hash,deleted,empty);
  }
 }
}
