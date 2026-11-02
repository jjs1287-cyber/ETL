import java.net.URL;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.*;
import org.etlforge.integration.server.implementation.webservice.interchange.webservice.client.*;
import org.etlforge.integration.server.implementation.webservice.interchange.jobcontrol.client.*;
import org.etlforge.util.zip.ZipUtils;
public final class QueuedSalesforceOAuthSmoke {
 public static void main(String[] args)throws Exception{
  boolean fault=args.length>0 && "fault".equals(args[0]);
  java.util.Set<Path> previousTemp=new java.util.HashSet<Path>();try(java.util.stream.Stream<Path> paths=Files.list(Paths.get("C:/Windows/Temp/etlforge"))){paths.filter(Files::isDirectory).forEach(previousTemp::add);}
  String password=new String(Files.readAllBytes(Paths.get("C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt")),StandardCharsets.UTF_8).trim(), md5=StudioWebServicesSmoke.hash(password.getBytes(StandardCharsets.UTF_8));
  String project=StudioSalesforceOAuthSmoke.PROJECT, connector=StudioSalesforceOAuthSmoke.CONNECTOR;
  String ws="42420000-0000-4000-8000-000000000004",request="42420000-0000-4000-8000-000000000005",target="42420000-0000-4000-8000-000000000006",operation="42420000-0000-4000-8000-000000000007",query="42420000-0000-4000-8000-000000000003";
  Path original=Paths.get("C:/ETL/Studio/src/main/resources/etlforge-client-config/etlforge.salesforce.partner.29.wsdl");
  WebServiceInfoProvider info=new WebServiceInfoProviderServiceLocator().getkonga_webservice_infoprovider(new URL("http://127.0.0.1:47100/axis/konga_webservice_infoprovider"));
  ((org.apache.axis.client.Stub)info).setTimeout(60000);
  CWsdlFile upload=new CWsdlFile(); upload.setWsdlLocator(original.getFileName().toString());upload.setMD5(StudioWebServicesSmoke.hash(Files.readAllBytes(original))); upload.setZippedBase64Contents(ZipUtils.deflateAndBase64EncodeFileContents(original.toFile()));info.uploadWsdl("Admin",md5,upload,null);
  WebServiceOperationData definition=new WebServiceOperationData(original.getFileName().toString(),"SoapBinding","query",null,null,"document","literal");
  WebServiceJtr jtr=info.getInputAndOutputJTR("Admin",md5,definition);Files.write(Paths.get("C:/ETL/Reports/salesforce-oauth-inspection/query-input.jtr"),jtr.getInputJtr().getBytes(StandardCharsets.UTF_8));
  InterchangeDataHolder data=new InterchangeDataHolder();
  data.setInterchangeProjectsTab(StudioFileCopySmoke.table(new String[]{"InterchangeProject_ID","InterchangeProject_Name"},new String[]{project,"ETLForge isolated OAuth verification"}));
  data.setWebServicesTab(StudioFileCopySmoke.table(new String[]{"webservice_id","wsdl_id","webservice_name","webservice_url","service","port","binding","operation","soapversion","soapaction","soapstyle","soapbodyuse","inputxsdfilename","outputxsdfilename","httpusername","httppassword","targetnamespace","othernamespaces"},new String[]{ws,original.getFileName().toString(),"Isolated Salesforce query","[__Salesforce.ServiceURL]","SforceService","Soap","SoapBinding","query","1","query","document","literal",null,null,null,null,"urn:partner.soap.sforce.com",null}));
  Path output=Paths.get("C:/ETL/Samples/SalesforceOAuth/output");Files.createDirectories(output);
  data.setTargetsTab(StudioFileCopySmoke.table(new String[]{"Target_ID","Target_Name","Source_Type_ID","Locator","File_Name","Login","Password","Driver","DriverType","ConnectString","Target_Target_ID"},new String[]{target,"Isolated Salesforce query response","16",output.toString(),"user-query-[unique].xml",null,null,null,null,null,null}));
  data.setTransformationsTab_New(StudioFileCopySmoke.table(new String[]{"transformation_id","transformation_name","source_type_id","target_type_id","source_text_doc","target_text_doc","source_sourceid","target_targetid","source_xml","target_xml","natureofsource","natureoftarget","sourcedtd_root","targetdtd_root","sourcejmsmessageid","targetjmsmessageid"},new String[]{request,"Build Salesforce OAuth query","13","12",null,null,null,null,null,null,"0","1",null,"root",null,null}));
  String[][] maps={{request,"root$transaction.header$MruHeader$updateMru$","false"},{request,"root$transaction.header$SessionHeader$sessionId$","Get(\"__Salesforce.SessionID."+connector+"\")"},{request,"root$transaction.body$query$queryString$","\"SELECT Id FROM User LIMIT 1\""}};
  if(fault)maps[maps.length-1][2]="\"SELECT ETLForgeIntentionallyInvalidField FROM User\"";
  for(String[] row:maps)row[2]=java.util.Base64.getEncoder().encodeToString(("<trans>\n"+row[2]+"\n</trans>").getBytes(StandardCharsets.UTF_8));
  data.setTransformation_DefinitionsTab(StudioFileCopySmoke.table(new String[]{"transformation_id","target_field_id","transformation"},maps));
  data.setOperationsTab(StudioFileCopySmoke.table(new String[]{"Operation_ID","Operation_Name","Operation_Type_ID","Source_ID","Target_ID","Ignore_History","Schedule_ID","Transformation_ID","Success_Operation_ID","Success_Message_ID","Failure_Operation_ID","Failure_Message_ID"},new String[]{operation,"Salesforce OAuth - Read-only User Query","6",null,target,"1",null,request,null,null,null,null}));
  data.setOperationPipelines(StudioFileCopySmoke.table(new String[]{"Operation_ID","ActivityType_ID","Activity_ID"},new String[]{operation,"5",request},new String[]{operation,"6",ws},new String[]{operation,"4",target}));
  data.setTransformationChainLinksTab(StudioFileCopySmoke.table(new String[]{"operation_id","transformation_id","nexttransformation_id"},new String[]{null,request,ws},new String[]{operation,request,ws}));
  data.setSalesforceQueriesTab(StudioFileCopySmoke.table(new String[]{"salesforcequery_id","salesforcequery_name","query_string","salesforce_object","operation_id"},new String[]{query,"Isolated OAuth read-only query","SELECT Id FROM User LIMIT 1","User",operation}));
  data.setEntityProperties(StudioFileCopySmoke.table(new String[]{"EntityTypeId","EntityId","KeyName","KeyValue"},new String[]{"25",query,"connector_guid",connector}));
  DeployInterchangeData deploy=new DeployInterchangeDataServiceLocator().getkonga_deploy_interchange_data(new URL("http://127.0.0.1:47100/axis/konga_deploy_interchange_data"));((org.apache.axis.client.Stub)deploy).setTimeout(60000);
  CDeployResults result=deploy.deployInterchangeData("Admin",md5,new InterchangeIdHolder(),data);if(result.getDeployStatus()>1)throw new AssertionError("OAuth query operation deploy failed");
  java.util.Set<Path> before=new java.util.HashSet<Path>();try(java.util.stream.Stream<Path> paths=Files.list(output)){paths.forEach(before::add);}
  JobControl jobs=new JobControlServiceLocator().getkonga_jobcontrol(new URL("http://127.0.0.1:47100/axis/konga_jobcontrol"));WsRunOperationResult run=jobs.runOperationInstanceMD5Password("Admin",md5,operation);Files.write(Paths.get("C:/ETL/Reports/salesforce-oauth-inspection/query-instance.txt"),Long.toString(run.getOperationInstanceId()).getBytes(StandardCharsets.UTF_8));
  long deadline=System.nanoTime()+90000000000L;Path response=null;
  if(fault){
   while(System.nanoTime()<deadline){
    try(java.util.stream.Stream<Path> directories=Files.list(Paths.get("C:/Windows/Temp/etlforge"))){
     java.util.List<Path> candidates=new java.util.ArrayList<Path>();directories.filter(p->Files.isDirectory(p)&&!previousTemp.contains(p)).forEach(candidates::add);
     for(Path directory:candidates)try(java.util.stream.Stream<Path> files=Files.list(directory)){
      for(Path requestFile:(Iterable<Path>)files.filter(p->p.toString().endsWith("_req.xml"))::iterator){
       javax.xml.parsers.DocumentBuilderFactory factory=javax.xml.parsers.DocumentBuilderFactory.newInstance();factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true);
       org.w3c.dom.Document requestXml=factory.newDocumentBuilder().parse(requestFile.toFile());org.w3c.dom.Node session=requestXml.getElementsByTagName("urn:sessionId").item(0);
       javax.xml.xpath.XPath xpath=javax.xml.xpath.XPathFactory.newInstance().newXPath();session=(org.w3c.dom.Node)xpath.evaluate("//*[local-name()='sessionId']",requestXml,javax.xml.xpath.XPathConstants.NODE);
       if(session!=null&&session.getTextContent().matches("\\*+")){System.out.println("PASS: failed Salesforce SOAP operation retains a diagnostic request with its session token redacted");return;}
      }
     }
    }
    Thread.sleep(250);
   }
   throw new AssertionError("No redacted Salesforce diagnostic request found");
  }
  while(System.nanoTime()<deadline&&response==null){try(java.util.stream.Stream<Path> paths=Files.list(output)){response=paths.filter(p->!before.contains(p)&&p.toString().endsWith(".xml")).findFirst().orElse(null);}if(response==null)Thread.sleep(250);}
  if(response==null)throw new AssertionError("Queued Salesforce query produced no output within 90 seconds");
  javax.xml.parsers.DocumentBuilderFactory factory=javax.xml.parsers.DocumentBuilderFactory.newInstance();factory.setNamespaceAware(true);factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true);
  org.w3c.dom.Document xml=factory.newDocumentBuilder().parse(response.toFile());
  if(xml.getElementsByTagNameNS("urn:partner.soap.sforce.com","queryResponse").getLength()!=1||xml.getElementsByTagNameNS("urn:partner.soap.sforce.com","records").getLength()!=1)throw new AssertionError("Unexpected Salesforce query response");
  System.out.println("PASS: queued native x64 operation acquired OAuth session, transformed SOAP request, queried Salesforce and wrote validated XML; account values withheld");
 }
}
