import java.net.URL;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.*;
import org.etlforge.integration.server.implementation.webservice.interchange.webservice.client.*;
import org.etlforge.util.zip.ZipUtils;
import org.etlforge.integration.server.implementation.webservice.interchange.xml.client.*;
import org.etlforge.integration.server.implementation.webservice.interchange.jobcontrol.client.*;
public class StudioWebServicesSmoke {
 static final String PROJECT="42000000-0000-4000-8000-000000000001", WS="42000000-0000-4000-8000-000000000002";
 static String hash(byte[] bytes)throws Exception{StringBuilder b=new StringBuilder();for(byte v:MessageDigest.getInstance("MD5").digest(bytes))b.append(String.format("%02x",v&255));return b.toString();}
 public static void main(String[] args)throws Exception{
  String password=new String(Files.readAllBytes(Paths.get("C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt")),StandardCharsets.UTF_8).trim(),md5=hash(password.getBytes(StandardCharsets.UTF_8));
  Path wsdl=Paths.get("C:/ETL/Samples/WebServices/ETLForgeEcho.wsdl");
  WebServiceInfoProvider info=new WebServiceInfoProviderServiceLocator().getkonga_webservice_infoprovider(new URL("http://127.0.0.1:47100/axis/konga_webservice_infoprovider"));
  ((org.apache.axis.client.Stub)info).setTimeout(60000);
  CWsdlFile file=new CWsdlFile();file.setWsdlLocator(wsdl.getFileName().toString());file.setMD5(hash(Files.readAllBytes(wsdl)));file.setZippedBase64Contents(ZipUtils.deflateAndBase64EncodeFileContents(wsdl.toFile()));
  CWsdlFileUploadResult uploaded=info.uploadWsdl("Admin",md5,file,null);if(uploaded==null)throw new AssertionError("No WSDL upload result");System.out.println("WSDL upload status "+uploaded.getWsdlFile().getStatus());
  WebServiceOperationData od=new WebServiceOperationData("ETLForgeEcho.wsdl","EchoBinding","Echo",null,null,"document","literal"); WebServiceJtr jtr=info.getInputAndOutputJTR("Admin",md5,od); Files.write(Paths.get("C:/ETL/Reports/echo-input.jtr"),jtr.getInputJtr().getBytes(StandardCharsets.UTF_8)); Files.write(Paths.get("C:/ETL/Reports/echo-output.jtr"),jtr.getOutputJtr().getBytes(StandardCharsets.UTF_8)); InterchangeDataHolder data=new InterchangeDataHolder();
  data.setInterchangeProjectsTab(StudioFileCopySmoke.table(new String[]{"InterchangeProject_ID","InterchangeProject_Name"},new String[]{PROJECT,"ETLForge Web Service Examples"}));
  data.setWebServicesTab(StudioFileCopySmoke.table(new String[]{"webservice_id","wsdl_id","webservice_name","webservice_url","service","port","binding","operation","soapversion","soapaction","soapstyle","soapbodyuse","inputxsdfilename","outputxsdfilename","httpusername","httppassword","targetnamespace","othernamespaces"},new String[]{WS,"ETLForgeEcho.wsdl","Local Echo SOAP","http://127.0.0.1:47100/etlforge/soap?id=42000000-0000-4000-8000-000000000007","EchoService","EchoPort","EchoBinding","Echo","1","urn:etlforge:echo/Echo","document","literal",null,null,"Admin",password,"urn:etlforge:echo",null}));
  String host="42000000-0000-4000-8000-000000000003",request="42000000-0000-4000-8000-000000000004",response="42000000-0000-4000-8000-000000000005",target="42000000-0000-4000-8000-000000000006",hostOp="42000000-0000-4000-8000-000000000007",callOp="42000000-0000-4000-8000-000000000008";
  Path schema=Paths.get("C:/ETL/Samples/WebServices/EchoResult.xsd");
  XmlInfoProvider xi=new XmlInfoProviderServiceLocator().getkonga_xml_infoprovider(new URL("http://127.0.0.1:47100/axis/konga_xml_infoprovider"));
  CXmlStructureFile xf=new CXmlStructureFile();xf.setPath("EchoResult.xsd");xf.setMD5(hash(Files.readAllBytes(schema)));xf.setModifiedTime(Files.getLastModifiedTime(schema).toMillis());xf.setSchemaType(0);xf.setZippedBase64Contents(ZipUtils.deflateAndBase64EncodeFileContents(schema.toFile()));xi.uploadStructureFile("Admin",md5,xf,null);
  Files.createDirectories(Paths.get("C:/ETL/Samples/WebServices/output"));
  data.setTargetsTab(StudioFileCopySmoke.table(new String[]{"Target_ID","Target_Name","Source_Type_ID","Locator","File_Name","Login","Password","Driver","DriverType","ConnectString","Target_Target_ID"},new String[]{target,"SOAP response file","16","C:/ETL/Samples/WebServices/output","echo-[unique].xml",null,null,null,null,null,null}));
  data.setTransformationsTab_New(StudioFileCopySmoke.table(new String[]{"transformation_id","transformation_name","source_type_id","target_type_id","source_text_doc","target_text_doc","source_sourceid","target_targetid","source_xml","target_xml","natureofsource","natureoftarget","sourcedtd_root","targetdtd_root"},new String[]{host,"Hosted Echo request to response","12","14",null,null,null,null,null,null,"1","1","root","root"},new String[]{request,"Build Echo SOAP request","13","12",null,null,null,null,null,null,"0","1",null,"root"},new String[]{response,"Save Echo SOAP response","14","4",null,null,null,null,null,"EchoResult.xsd","1","1","root","EchoResult"}));
  InterchangeDataTable tt=data.getTransformationsTab_New(); String[] cols=java.util.Arrays.copyOf(tt.getColumnNames(),tt.getColumnNames().length+2);cols[cols.length-2]="sourcejmsmessageid";cols[cols.length-1]="targetjmsmessageid";tt.setColumnNames(cols);for(InterchangeDataRow row:tt.getRows())row.setFields(java.util.Arrays.copyOf(row.getFields(),cols.length));
  String[][] defs={{host,"root$transaction$body$EchoResponse$Text$","root$transaction$body$Echo$Text$"},{request,"root$transaction.body$Echo$Text$","\"Hello from ETLForge 42 — SOAP round trip\""},{response,"EchoResult$Text$","root$transaction#1.response$body$EchoResponse$Text$"}};
  for(String[] row:defs)row[2]=java.util.Base64.getEncoder().encodeToString(("<trans>\n"+row[2]+"\n</trans>").getBytes(StandardCharsets.UTF_8));
  data.setTransformation_DefinitionsTab(StudioFileCopySmoke.table(new String[]{"transformation_id","target_field_id","transformation"},defs));
  data.setOperationsTab(StudioFileCopySmoke.table(new String[]{"Operation_ID","Operation_Name","Operation_Type_ID","Source_ID","Target_ID","Ignore_History","Schedule_ID","Transformation_ID","Success_Operation_ID","Success_Message_ID","Failure_Operation_ID","Failure_Message_ID"},new String[]{hostOp,"Hosted Web Service - Echo","7",null,null,"1",null,host,null,null,null,null},new String[]{callOp,"Web Service Call - Local Echo","6",null,target,"1",null,request,null,null,null,null}));
  data.setOperationPipelines(StudioFileCopySmoke.table(new String[]{"Operation_ID","ActivityType_ID","Activity_ID"},new String[]{hostOp,"6",WS},new String[]{hostOp,"5",host},new String[]{callOp,"5",request},new String[]{callOp,"6",WS},new String[]{callOp,"5",response},new String[]{callOp,"4",target}));
  data.setTransformationChainLinksTab(StudioFileCopySmoke.table(new String[]{"operation_id","transformation_id","nexttransformation_id"},new String[]{null,WS,host},new String[]{null,host,WS},new String[]{null,request,WS},new String[]{null,WS,response},new String[]{callOp,request,WS},new String[]{callOp,WS,response}));
  DeployInterchangeData deploy=new DeployInterchangeDataServiceLocator().getkonga_deploy_interchange_data(new URL("http://127.0.0.1:47100/axis/konga_deploy_interchange_data"));((org.apache.axis.client.Stub)deploy).setTimeout(60000);
  CDeployResults result=deploy.deployInterchangeData("Admin",md5,new InterchangeIdHolder(),data);System.out.println("Deploy status "+result.getDeployStatus());if(result.getDeployStatus()>1)throw new AssertionError("Deployment failed"); if(args.length>0 && args[0].equals("run")){ java.util.Set<Path> before=new java.util.HashSet<Path>();try(java.util.stream.Stream<Path> paths=Files.list(Paths.get("C:/ETL/Samples/WebServices/output"))){paths.forEach(before::add);} JobControl jobs=new JobControlServiceLocator().getkonga_jobcontrol(new URL("http://127.0.0.1:47100/axis/konga_jobcontrol")); WsRunOperationResult run=jobs.runOperationInstanceMD5Password("Admin",md5,callOp); System.out.println("Queued Web Service Call instance "+run.getOperationInstanceId()); Files.write(Paths.get("C:/ETL/Reports/webservice-call-instance.txt"),Long.toString(run.getOperationInstanceId()).getBytes(StandardCharsets.UTF_8));
   long deadline=System.nanoTime()+60000000000L; Path output=null;
   while(System.nanoTime()<deadline && output==null){try(java.util.stream.Stream<Path> paths=Files.list(Paths.get("C:/ETL/Samples/WebServices/output"))){output=paths.filter(p->!before.contains(p)&&p.toString().endsWith(".xml")).findFirst().orElse(null);}if(output==null)Thread.sleep(200);}
   if(output==null)throw new AssertionError("No SOAP result file within 60 seconds");
   javax.xml.parsers.DocumentBuilderFactory factory=javax.xml.parsers.DocumentBuilderFactory.newInstance();factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true);org.w3c.dom.Document xml=factory.newDocumentBuilder().parse(output.toFile());
   if(!xml.getDocumentElement().getNodeName().equals("EchoResult") || xml.getElementsByTagName("Text").getLength()!=1 || !xml.getElementsByTagName("Text").item(0).getTextContent().equals("Hello from ETLForge 42 — SOAP round trip"))throw new AssertionError("SOAP output content mismatch");
   Files.write(Paths.get("C:/ETL/Reports/webservice-call-output.txt"),output.toString().getBytes(StandardCharsets.UTF_8));System.out.println("PASS: queued Web Service Call produced the exact Unicode SOAP round-trip result");
  }
 }
}













