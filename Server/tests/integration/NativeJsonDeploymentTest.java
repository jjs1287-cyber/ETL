import java.net.URL;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.*;
import org.etlforge.integration.server.implementation.webservice.interchange.xml.client.*;
import org.etlforge.integration.server.implementation.webservice.interchange.jobcontrol.client.*;
import org.etlforge.util.zip.ZipUtils;
public final class NativeJsonDeploymentTest {
 static final Path ROOT=Paths.get("C:/ETL/Samples/NativeJsonDeployment");
 static final String PROJECT="42440000-0000-4000-8000-000000000001";
 static String guid(int n){return String.format("42440000-0000-4000-8000-%012d",n);}
 static String hash(byte[] value)throws Exception{StringBuilder s=new StringBuilder();for(byte b:MessageDigest.getInstance("MD5").digest(value))s.append(String.format("%02x",b&255));return s.toString();}
 static InterchangeDataTable table(String[] c,String[]...r){return StudioFileCopySmoke.table(c,r);}
 public static void main(String[] args)throws Exception {
  String phase=args.length==0?"Java":args[0];boolean setup=phase.equals("setup");
  String password=new String(Files.readAllBytes(Paths.get("C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt")),StandardCharsets.UTF_8).trim(),md5=hash(password.getBytes(StandardCharsets.UTF_8));
  Files.createDirectories(ROOT.resolve("input"));Files.createDirectories(ROOT.resolve("output"));
  if(setup){
   String xsd="<?xml version='1.0' encoding='UTF-8'?><xs:schema xmlns:xs='http://www.w3.org/2001/XMLSchema'><xs:element name='json'><xs:complexType><xs:sequence><xs:element name='name' type='xs:string'/><xs:element name='amount' type='xs:double'/><xs:element name='flag' type='xs:boolean'/></xs:sequence><xs:attribute name='object' type='xs:string' fixed='true' use='required'/></xs:complexType></xs:element></xs:schema>";
   Path schema=ROOT.resolve("NativeJsonRuntime.xsd");Files.write(schema,xsd.getBytes(StandardCharsets.UTF_8));
   CXmlStructureFile file=new CXmlStructureFile();file.setPath("NativeJsonRuntime.xsd");file.setMD5(hash(Files.readAllBytes(schema)));file.setModifiedTime(Files.getLastModifiedTime(schema).toMillis());file.setSchemaType(0);file.setZippedBase64Contents(ZipUtils.deflateAndBase64EncodeFileContents(schema.toFile()));
   new XmlInfoProviderServiceLocator().getkonga_xml_infoprovider(new URL("http://127.0.0.1:47100/axis/konga_xml_infoprovider")).uploadStructureFile("Admin",md5,file,null);
   String jtr=new org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.TreeMapperInfoProviderServiceLocator().getkonga_treemapper_infoprovider(new URL("http://127.0.0.1:47100/axis/konga_treemapper_infoprovider")).getJtrForStructureFile("Admin",md5,"NativeJsonRuntime.xsd","json",true);
   Files.write(ROOT.resolve("target.jtr"),jtr.getBytes(StandardCharsets.UTF_8));
   InterchangeDataHolder data=new InterchangeDataHolder();data.setInterchangeProjectsTab(table(new String[]{"InterchangeProject_ID","InterchangeProject_Name"},new String[]{PROJECT,"Native JSON runtime verification"}));
   List<String[]> sources=new ArrayList<>(),targets=new ArrayList<>(),transforms=new ArrayList<>(),defs=new ArrayList<>(),ops=new ArrayList<>(),pipeline=new ArrayList<>();
   for(int i=0;i<4;i++){
    String source=guid(10+i),target=guid(20+i),transform=guid(30+i),operation=guid(40+i);
    sources.add(new String[]{source,"JSON verification input "+i,"16",ROOT.resolve("input").toString(),i==3?"batch*.json":"input"+i+(i==1?".xml":".json"),null,null,null,null,null});
    targets.add(new String[]{target,"JSON verification output "+i,"16",ROOT.resolve("output").toString(),i==3?"batch-[unique].json":"result"+i+(i==0?".xml":".json"),null,null,null,null,null,null});
    transforms.add(new String[]{transform,"JSON verification mapping "+i,i==1?"4":"17",i==0?"4":"17",null,null,null,null,"NativeJsonRuntime.xsd","NativeJsonRuntime.xsd","1","1","json","json",null,null});
    for(String field:new String[]{"name","amount","flag"})defs.add(new String[]{transform,"json$"+field+"$",Base64.getEncoder().encodeToString(("<trans>json$"+field+"$</trans>").getBytes(StandardCharsets.UTF_8))});
    defs.add(new String[]{transform,"json$object",Base64.getEncoder().encodeToString("<trans>\"true\"</trans>".getBytes(StandardCharsets.UTF_8))});
    ops.add(new String[]{operation,new String[]{"JSON source to XML","XML source to JSON","JSON source to JSON","JSON multiple files"}[i],"3",source,target,"1",null,transform,null,null,null,null});
    pipeline.add(new String[]{operation,"3",source});pipeline.add(new String[]{operation,"5",transform});pipeline.add(new String[]{operation,"4",target});
   }
   data.setSourcesTab(table(new String[]{"Source_ID","Source_Name","Source_Type_ID","Locator","File_Name","Login","Password","Driver","DriverType","ConnectString"},sources.toArray(new String[0][])));
   data.setTargetsTab(table(new String[]{"Target_ID","Target_Name","Source_Type_ID","Locator","File_Name","Login","Password","Driver","DriverType","ConnectString","Target_Target_ID"},targets.toArray(new String[0][])));
   data.setTransformationsTab_New(table(new String[]{"transformation_id","transformation_name","source_type_id","target_type_id","source_text_doc","target_text_doc","source_sourceid","target_targetid","source_xml","target_xml","natureofsource","natureoftarget","sourcedtd_root","targetdtd_root","sourcejmsmessageid","targetjmsmessageid"},transforms.toArray(new String[0][])));
   data.setTransformation_DefinitionsTab(table(new String[]{"transformation_id","target_field_id","transformation"},defs.toArray(new String[0][])));
   data.setOperationsTab(table(new String[]{"Operation_ID","Operation_Name","Operation_Type_ID","Source_ID","Target_ID","Ignore_History","Schedule_ID","Transformation_ID","Success_Operation_ID","Success_Message_ID","Failure_Operation_ID","Failure_Message_ID"},ops.toArray(new String[0][])));data.setOperationPipelines(table(new String[]{"Operation_ID","ActivityType_ID","Activity_ID"},pipeline.toArray(new String[0][])));
   CDeployResults result=new DeployInterchangeDataServiceLocator().getkonga_deploy_interchange_data(new URL("http://127.0.0.1:47100/axis/konga_deploy_interchange_data")).deployInterchangeData("Admin",md5,new InterchangeIdHolder(),data);
   if(result==null||result.getDeployStatus()>1)throw new AssertionError("JSON fixture deployment failed");System.out.println("PASS JSON fixtures deployed");return;
  }
  String value="ETLForge 日本 café & text";
  byte[] json=("{\"name\":\""+value+"\",\"amount\":12.5,\"flag\":true}").getBytes(StandardCharsets.UTF_8);
  byte[] xml=("<?xml version='1.0' encoding='UTF-8'?><json object='true'><name>"+value.replace("&","&amp;")+"</name><amount>12.5</amount><flag>true</flag></json>").getBytes(StandardCharsets.UTF_8);
  if(phase.equals("NativeEmoji")){value+=" 😀";json=("{\"name\":\""+value+"\",\"amount\":12.5,\"flag\":true}").getBytes(StandardCharsets.UTF_8);xml=("<json object='true'><name>"+value.replace("&","&amp;")+"</name><amount>12.5</amount><flag>true</flag></json>").getBytes(StandardCharsets.UTF_8);}
  JobControl jobs=new JobControlServiceLocator().getkonga_jobcontrol(new URL("http://127.0.0.1:47100/axis/konga_jobcontrol"));
  if(phase.equals("StudioTest")){
   org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.TreeMapperInfoProvider api=new org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.TreeMapperInfoProviderServiceLocator().getkonga_treemapper_infoprovider(new URL("http://127.0.0.1:47100/axis/konga_treemapper_infoprovider"));
   ((org.apache.axis.client.Stub)api).setTimeout(30000);
   org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.DebuggingParameters params=new org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.DebuggingParameters();params.setCommand("Run");params.setDebuggingSessionId(UUID.randomUUID().toString());params.setBreakpoints(new org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.Breakpoint[0]);params.setGlobalVariables(new org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.WsDataElement[0]);
   Path input=ROOT.resolve("studio-test.json");Files.write(input,json);
   org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.WsLoadSourceDataInput load=new org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.WsLoadSourceDataInput(guid(32),"",ZipUtils.deflateAndBase64EncodeFileContents(input.toFile()),new org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.PipelinePositionStruct[0]);
   org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.WsLoadSourceResult loaded=api.loadSourceData("Admin",md5,load);
   org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.WsTestTransformationResult result=api.testTransformation("Admin",md5,guid(32),loaded.getGuid(),null,params);
   if(result.getErrors()!=null&&!result.getErrors().isEmpty())throw new AssertionError(result.getErrors());
   if(result.getTargetFile()==null)throw new AssertionError("Studio test target is missing");
   String output=org.etlforge.integration.client.server.ETLForgeServerEncoding.base64DecodeAndInflateString(result.getTargetFile().getCompressedData());
   org.w3c.dom.Document document=javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new java.io.ByteArrayInputStream(output.getBytes(StandardCharsets.UTF_8)));
   if(!document.getElementsByTagName("name").item(0).getTextContent().equals(value))throw new AssertionError("Studio test intermediate XML differs");
   System.out.println("PASS native Studio Test transformation endpoint");return;
  }
  if(phase.equals("Multi")){
   try(java.util.stream.Stream<Path> files=Files.list(ROOT.resolve("output"))){for(Path p:(Iterable<Path>)files.filter(p->p.getFileName().toString().startsWith("batch-"))::iterator)Files.delete(p);}
   for(int i=0;i<2;i++)Files.write(ROOT.resolve("input/batch"+i+".json"),json);
   WsRunOperationResult run=jobs.runOperationInstanceMD5Password("Admin",md5,guid(43));
   long deadline=System.nanoTime()+45000000000L;List<Path> outputs=new ArrayList<>();
   while(System.nanoTime()<deadline){try(java.util.stream.Stream<Path> files=Files.list(ROOT.resolve("output"))){outputs.clear();files.filter(p->p.getFileName().toString().startsWith("batch-")).forEach(outputs::add);}if(outputs.size()==2)break;Thread.sleep(100);}
   if(outputs.size()!=2)throw new AssertionError("Multiple-file outputs missing for instance "+run.getOperationInstanceId());
   for(Path p:outputs)if(!new com.fasterxml.jackson.databind.ObjectMapper().readTree(p.toFile()).get("name").asText().equals(value))throw new AssertionError("Multiple-file output differs");
   System.out.println("PASS multiple JSON files, instance "+run.getOperationInstanceId());return;
  }
  if(phase.equals("SubmitBad")){
   Files.write(ROOT.resolve("input/input0.json"),"{broken".getBytes(StandardCharsets.UTF_8));Files.deleteIfExists(ROOT.resolve("output/result0.xml"));
   WsRunOperationResult run=jobs.runOperationInstanceMD5Password("Admin",md5,guid(40));Files.write(Paths.get("C:/ETL/Reports/native-json-negative-instance.txt"),Long.toString(run.getOperationInstanceId()).getBytes(StandardCharsets.UTF_8));System.out.println("Queued malformed JSON instance "+run.getOperationInstanceId());return;
  }
  List<String> results=new ArrayList<>();
  WsRunOperationResult[] submitted=new WsRunOperationResult[3];
  if(phase.equals("Concurrent")){
   for(int i=0;i<3;i++){Files.write(ROOT.resolve("input/input"+i+(i==1?".xml":".json")),i==1?xml:json);Files.deleteIfExists(ROOT.resolve("output/result"+i+(i==0?".xml":".json")));}
   for(int i=0;i<3;i++)submitted[i]=jobs.runOperationInstanceMD5Password("Admin",md5,guid(40+i));
  }
  for(int i=0;i<3;i++){
   Path input=ROOT.resolve("input/input"+i+(i==1?".xml":".json")),output=ROOT.resolve("output/result"+i+(i==0?".xml":".json"));byte[] bytes=i==1?xml:json;if(!phase.equals("Concurrent")){Files.write(input,bytes);Files.deleteIfExists(output);}
   long start=System.nanoTime();WsRunOperationResult run=submitted[i]!=null?submitted[i]:jobs.runOperationInstanceMD5Password("Admin",md5,guid(40+i));long deadline=System.nanoTime()+45000000000L;while(!Files.exists(output)&&System.nanoTime()<deadline)Thread.sleep(25);
   if(!Files.exists(output))throw new AssertionError("Missing output for instance "+run.getOperationInstanceId());Thread.sleep(100);
   String actual=new String(Files.readAllBytes(output),StandardCharsets.UTF_8);
   if(i==0){javax.xml.parsers.DocumentBuilderFactory f=javax.xml.parsers.DocumentBuilderFactory.newInstance();org.w3c.dom.Document d=f.newDocumentBuilder().parse(output.toFile());if(!d.getElementsByTagName("name").item(0).getTextContent().equals(value))throw new AssertionError("XML name differs: "+actual);}else{com.fasterxml.jackson.databind.JsonNode result=new com.fasterxml.jackson.databind.ObjectMapper().readTree(actual);if(!result.get("name").asText().equals(value)||result.get("amount").asDouble()!=12.5||!result.get("flag").asBoolean())throw new AssertionError("JSON result differs: "+actual);}
   if(!Arrays.equals(bytes,Files.readAllBytes(input)))throw new AssertionError("Original input changed");
   Files.copy(output,ROOT.resolve(phase+"-result"+i+(i==0?".xml":".json")),StandardCopyOption.REPLACE_EXISTING);
   results.add(phase+","+i+","+run.getOperationInstanceId()+","+(System.nanoTime()-start)/1000000.0);System.out.println("PASS "+phase+" route "+i+" instance "+run.getOperationInstanceId());
  }
  Files.write(Paths.get("C:/ETL/Reports/native-json-deployed-"+phase+".csv"),results,StandardCharsets.UTF_8);
 }
}
