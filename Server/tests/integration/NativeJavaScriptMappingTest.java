import java.net.URL;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.*;
import org.etlforge.integration.server.implementation.webservice.interchange.xml.client.*;
import org.etlforge.integration.server.implementation.webservice.interchange.jobcontrol.client.*;
import org.etlforge.util.zip.ZipUtils;
public final class NativeJavaScriptMappingTest {
 static final Path ROOT=Paths.get("C:/ETL/Samples/NativeJavaScriptMapping");
 static String guid(int n){return String.format("42470000-0000-4000-8000-%012d",n);}
 static String hash;
 static String digest(byte[] bytes)throws Exception{StringBuilder text=new StringBuilder();for(byte b:java.security.MessageDigest.getInstance("MD5").digest(bytes))text.append(String.format("%02x",b&255));return text.toString();}
 static String databasePassword()throws Exception{
  String line=Files.readAllLines(Paths.get("C:/ETL/Server/private/pgbouncer-dev.pgpass"),StandardCharsets.UTF_8).get(0);
  List<String> fields=new ArrayList<>();StringBuilder value=new StringBuilder();boolean escape=false;
  for(char c:line.toCharArray()){if(escape){value.append(c);escape=false;}else if(c=='\\')escape=true;else if(c==':'&&fields.size()<4){fields.add(value.toString());value.setLength(0);}else value.append(c);}fields.add(value.toString());
  if(fields.size()!=5||!fields.get(3).equals("etlforge_dev"))throw new AssertionError("Expected local test database credential");return fields.get(4);
 }
 static InterchangeDataTable table(String[] names,String[]... rows){return StudioFileCopySmoke.table(names,rows);}
 static void upload(String name,String xml)throws Exception{
  Path file=ROOT.resolve(name);Files.write(file,xml.getBytes(StandardCharsets.UTF_8));
  CXmlStructureFile structure=new CXmlStructureFile();structure.setPath(name);structure.setMD5(digest(Files.readAllBytes(file)));structure.setModifiedTime(Files.getLastModifiedTime(file).toMillis());structure.setSchemaType(0);structure.setZippedBase64Contents(ZipUtils.deflateAndBase64EncodeFileContents(file.toFile()));
  new XmlInfoProviderServiceLocator().getkonga_xml_infoprovider(new URL("http://127.0.0.1:47100/axis/konga_xml_infoprovider")).uploadStructureFile("Admin",hash,structure,null);
 }
 static void setup()throws Exception{
  upload("NativeJavaScriptRows.xsd","<xs:schema xmlns:xs='http://www.w3.org/2001/XMLSchema'><xs:element name='rows'><xs:complexType><xs:sequence><xs:element name='row' minOccurs='0' maxOccurs='unbounded'><xs:complexType><xs:sequence><xs:element name='id' type='xs:double'/><xs:element name='name' type='xs:string'/></xs:sequence></xs:complexType></xs:element></xs:sequence></xs:complexType></xs:element></xs:schema>");
  upload("NativeJavaScriptRecord.xsd","<xs:schema xmlns:xs='http://www.w3.org/2001/XMLSchema'><xs:element name='json'><xs:complexType><xs:sequence><xs:element name='name' type='xs:string'/><xs:element name='amount' type='xs:double'/><xs:element name='flag' type='xs:boolean'/></xs:sequence><xs:attribute name='object' type='xs:string' fixed='true' use='required'/></xs:complexType></xs:element></xs:schema>");
  InterchangeDataHolder data=new InterchangeDataHolder();data.setInterchangeProjectsTab(table(new String[]{"InterchangeProject_ID","InterchangeProject_Name"},new String[]{guid(1),"Native JavaScript mapping verification"}));
  List<String[]> sources=new ArrayList<>(),targets=new ArrayList<>(),transforms=new ArrayList<>(),defs=new ArrayList<>(),ops=new ArrayList<>(),pipeline=new ArrayList<>();
  for(int i=0;i<6;i++){
   boolean js=(i%2)==1,csv=i>=4,json=i>=2&&!csv;String source=guid(10+i),target=guid(20+i),transform=guid(30+i),operation=guid(40+i);
   String schema=json?"NativeJavaScriptRecord.xsd":"NativeJavaScriptRows.xsd",root=json?"json":"rows";
   sources.add(new String[]{source,"JS verification input "+i,"16",ROOT.resolve("input").toString(),csv?"input.csv":json?"input.json":"input.xml",null,null,null,null,null});
   targets.add(new String[]{target,"JS verification output "+i,"16",ROOT.resolve("output").toString(),"result"+i+extension(i),null,null,null,null,null,null});
   transforms.add(new String[]{transform,(js?"JavaScript":"Trans")+(csv?" CSV":json?" JSON":" XML"),csv?"1":json?"17":"4",csv?"1":json?"17":"4",csv?guid(90):null,csv?guid(90):null,null,null,csv?null:schema,csv?null:schema,csv?"Flat":"1",csv?"Flat":"1",csv?null:root,csv?null:root,null,null});
   String[] fields=csv?new String[]{"id","name"}:json?new String[]{"json$name$","json$amount$","json$flag$"}:new String[]{"rows$row.id$","rows$row.name$"};
   for(String field:fields){String expression=js?"<javascript>SetScriptResult(ETLForge.GetSource('"+field+"'));</javascript>":"<trans>"+field+"</trans>";defs.add(new String[]{transform,field,Base64.getEncoder().encodeToString(expression.getBytes(StandardCharsets.UTF_8))});}
   if(json)defs.add(new String[]{transform,"json$object",Base64.getEncoder().encodeToString((js?"<javascript>SetScriptResult('true');</javascript>":"<trans>'true'</trans>").getBytes(StandardCharsets.UTF_8))});
   ops.add(new String[]{operation,"Native JS verification "+i,"3",source,target,"1",null,transform,null,null,null,null});
   pipeline.add(new String[]{operation,"3",source});pipeline.add(new String[]{operation,"5",transform});pipeline.add(new String[]{operation,"4",target});
  }
  sources.add(new String[]{guid(16),"Native JS readonly database verification","2","127.0.0.1",null,"etlforge_dev",databasePassword(),"PostgreSQL Unicode(x64)","1","DRIVER={PostgreSQL Unicode(x64)};SERVER=127.0.0.1;PORT=5432;DATABASE=etlforge_dev_tran;UID=etlforge_dev;"});
  Files.write(ROOT.resolve("input/latin.dat"),new byte[]{'c','a','f',(byte)0xe9});
  sources.add(new String[]{guid(17),"Native JS encoded input verification","16",ROOT.resolve("input").toString(),"latin.dat",null,null,null,null,null});
  String host="<javascript>var data=ETLForge.ReadFile('s."+guid(10)+"');ETLForge.WriteFile('t."+guid(20)+"',data,'host-functions.txt');var one=ETLForge.DbLookup('s."+guid(16)+"',\"SELECT 'database value'\");var rows=ETLForge.DbExecute('s."+guid(16)+"',\"SELECT 7 AS n, 'café' AS label\");if(one!=='database value'||String(rows[0][0])!=='7'||rows[0][1]!=='café')throw Error('Database host result differs');ETLForge.SetVar('JsHostResult','OK');WriteToOperationLog('JS_HOST:'+ETLForge.GetVar('JsHostResult'));SetScriptOutput('JS_HOST:OK');</javascript>";
  data.setDocumentsTab(table(new String[]{"document_id","document_name","format_type_id","delimiter","text_qualifier","numeric_qualifier"},new String[]{guid(90),"Native JavaScript CSV verification","2",",","\"",null}));
  data.setDocument_DefinitionsTab(table(new String[]{"document_id","field_id","parent_id","field_name","sequence","var_type_id","start_pos","end_pos","required","default_value","format","uni_id"},new String[]{guid(90),"1","0","id","1","1","-1","-1","1",null,null,null},new String[]{guid(90),"2","0","name","2","1","-1","-1","1",null,null,null}));
  data.setScriptsTab(table(new String[]{"Script_ID","Script_Name","Script_Text"},new String[]{guid(60),"JavaScript host verification",Base64.getEncoder().encodeToString(host.getBytes(StandardCharsets.UTF_8))},new String[]{guid(61),"JavaScript child scope",Base64.getEncoder().encodeToString("<javascript>SetScriptResult(typeof parentScoped);</javascript>".getBytes(StandardCharsets.UTF_8))},new String[]{guid(62),"JavaScript rollback failure fixture",Base64.getEncoder().encodeToString(("<javascript>ETLForge.SetVar('JsRollback',99);ETLForge.ReadFile('s."+guid(10)+"');throw Error('intentional rollback fixture');</javascript>").getBytes(StandardCharsets.UTF_8))},new String[]{guid(63),"JavaScript deadline failure fixture",Base64.getEncoder().encodeToString("<javascript>while(true){}</javascript>".getBytes(StandardCharsets.UTF_8))}));
  ops.add(new String[]{guid(50),"JavaScript host verification","8",null,null,"1",null,null,null,null,null,null});pipeline.add(new String[]{guid(50),"1",guid(60)});
  ops.add(new String[]{guid(51),"JavaScript timeout verification","8",null,null,"1",null,null,null,null,null,null});pipeline.add(new String[]{guid(51),"1",guid(63)});
  data.setSourcesTab(table(new String[]{"Source_ID","Source_Name","Source_Type_ID","Locator","File_Name","Login","Password","Driver","DriverType","ConnectString"},sources.toArray(new String[0][])));
  data.setTargetsTab(table(new String[]{"Target_ID","Target_Name","Source_Type_ID","Locator","File_Name","Login","Password","Driver","DriverType","ConnectString","Target_Target_ID"},targets.toArray(new String[0][])));
  data.setTransformationsTab_New(table(new String[]{"transformation_id","transformation_name","source_type_id","target_type_id","source_text_doc","target_text_doc","source_sourceid","target_targetid","source_xml","target_xml","natureofsource","natureoftarget","sourcedtd_root","targetdtd_root","sourcejmsmessageid","targetjmsmessageid"},transforms.toArray(new String[0][])));
  data.setTransformation_DefinitionsTab(table(new String[]{"transformation_id","target_field_id","transformation"},defs.toArray(new String[0][])));
  data.setOperationsTab(table(new String[]{"Operation_ID","Operation_Name","Operation_Type_ID","Source_ID","Target_ID","Ignore_History","Schedule_ID","Transformation_ID","Success_Operation_ID","Success_Message_ID","Failure_Operation_ID","Failure_Message_ID"},ops.toArray(new String[0][])));
  data.setOperationPipelines(table(new String[]{"Operation_ID","ActivityType_ID","Activity_ID"},pipeline.toArray(new String[0][])));
  CDeployResults result=new DeployInterchangeDataServiceLocator().getkonga_deploy_interchange_data(new URL("http://127.0.0.1:47100/axis/konga_deploy_interchange_data")).deployInterchangeData("Admin",hash,new InterchangeIdHolder(),data);
  if(result==null||result.getDeployStatus()>1)throw new AssertionError("JavaScript fixture deployment failed");
  System.out.println("PASS isolated XML/JSON fixtures deployed");
 }
 static JobControl jobs;
 static String extension(int n){return n>=4?".csv":n>=2?".json":".xml";}
 static String status(long instance)throws Exception{
  ProcessBuilder builder=new ProcessBuilder("C:/ETL/Server/runtime/postgresql-18/pgsql/bin/psql.exe","-X","-w","-A","-t","-h","127.0.0.1","-p","5432","-U","etlforge_dev","-d","etlforge_dev_tran","-c","SELECT status FROM operationlogtab WHERE operation_instance_id="+instance);
  builder.environment().put("PGPASSFILE","C:/ETL/Server/private/pgbouncer-dev.pgpass");builder.redirectErrorStream(true);Process process=builder.start();java.io.ByteArrayOutputStream out=new java.io.ByteArrayOutputStream();byte[] bytes=new byte[1024];for(int n;(n=process.getInputStream().read(bytes))>=0;)out.write(bytes,0,n);if(process.waitFor()!=0)throw new AssertionError("Cannot query test operation status");return new String(out.toByteArray(),StandardCharsets.UTF_8).trim();
 }
 static double run(int index)throws Exception{
  Path output=ROOT.resolve("output/result"+index+extension(index));Files.deleteIfExists(output);
  long start=System.nanoTime();WsRunOperationResult run=jobs.runOperationInstanceMD5Password("Admin",hash,guid(40+index));
  if(run==null||run.getOperationInstanceId()<1)throw new AssertionError("Operation submission failed");
  for(int n=0;n<3000;n++){String state=status(run.getOperationInstanceId());if(state.equals("3")||state.equals("4")||state.equals("5")){if(!Files.exists(output))throw new AssertionError("Completed operation has no output");System.out.println("instance="+run.getOperationInstanceId()+" output="+output);return (System.nanoTime()-start)/1e6;}if(!state.isEmpty()&&!state.equals("0")&&!state.equals("2"))throw new AssertionError("Mapping instance "+run.getOperationInstanceId()+" failed with status "+state);Thread.sleep(100);}
  throw new AssertionError("Output timeout for instance "+run.getOperationInstanceId());
 }
 static byte[] output(int n)throws Exception{return Files.readAllBytes(ROOT.resolve("output/result"+n+extension(n)));}
 static void verifyXml(byte[] bytes,int rows)throws Exception{
  javax.xml.parsers.DocumentBuilderFactory factory=javax.xml.parsers.DocumentBuilderFactory.newInstance();factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true);
  org.w3c.dom.Document document=factory.newDocumentBuilder().parse(new java.io.ByteArrayInputStream(bytes));org.w3c.dom.NodeList records=document.getElementsByTagName("row");
  if(records.getLength()!=rows)throw new AssertionError("Expected "+rows+" records; got "+records.getLength());
  for(int i=0;i<rows;i++){org.w3c.dom.Element record=(org.w3c.dom.Element)records.item(i);if(Double.parseDouble(record.getElementsByTagName("id").item(0).getTextContent())!=i||!record.getElementsByTagName("name").item(0).getTextContent().equals("café 世界 😀 "+i))throw new AssertionError("Incorrect mapping record "+i);}
 }
 static void input(int rows)throws Exception{
  StringBuilder xml=new StringBuilder("<?xml version='1.0' encoding='UTF-8'?><rows>");for(int i=0;i<rows;i++)xml.append("<row><id>").append(i).append("</id><name>café 世界 😀 ").append(i).append("</name></row>");xml.append("</rows>");Files.write(ROOT.resolve("input/input.xml"),xml.toString().getBytes(StandardCharsets.UTF_8));
 }
 public static void main(String[] args)throws Exception{
  LocalVariablesRuntimeTest.init();hash=LocalVariablesRuntimeTest.hash;Files.createDirectories(ROOT.resolve("input"));Files.createDirectories(ROOT.resolve("output"));
  if(args.length>0&&args[0].equals("setup")){setup();return;}
  jobs=new JobControlServiceLocator().getkonga_jobcontrol(new URL("http://127.0.0.1:47100/axis/konga_jobcontrol"));
  input(3);run(0);run(1);if(!Arrays.equals(output(0),output(1)))throw new AssertionError("XML trans/JavaScript mismatch");verifyXml(output(1),3);System.out.println("PASS XML output byte parity and all expected records");
  Files.write(ROOT.resolve("input/input.json"),"{\"name\":\"café 世界 😀\",\"amount\":12.5,\"flag\":true}".getBytes(StandardCharsets.UTF_8));run(2);run(3);if(!Arrays.equals(output(2),output(3)))throw new AssertionError("JSON trans/JavaScript mismatch");System.out.println("PASS JSON output byte parity");
  Files.write(ROOT.resolve("input/input.csv"),"1,\"café, 世界 😀\"\r\n2,\"quote \"\"here\"\"\"\r\n3,plain\r\n".getBytes(StandardCharsets.UTF_8));run(4);run(5);if(!Arrays.equals(output(4),output(5)))throw new AssertionError("CSV trans/JavaScript mismatch");System.out.println("PASS CSV output byte parity");
  if(args.length>0&&args[0].startsWith("benchmark")){
   boolean csv=args[0].equals("benchmark-csv");
   List<String> lines=new ArrayList<>();lines.add("rows,repeat,trans_ms,javascript_ms,output_sha256");
   for(int rows:new int[]{1000,10000,100000}){if(csv){StringBuilder text=new StringBuilder();for(int i=0;i<rows;i++)text.append(i).append(",café 世界 😀 ").append(i).append("\r\n");Files.write(ROOT.resolve("input/input.csv"),text.toString().getBytes(StandardCharsets.UTF_8));}else input(rows);for(int repeat=0;repeat<5;repeat++){
    double trans=run(csv?4:0),js=run(csv?5:1);byte[] expected=output(csv?4:0),actual=output(csv?5:1);if(!Arrays.equals(expected,actual))throw new AssertionError("Benchmark output mismatch");if(csv){String[] records=new String(actual,StandardCharsets.UTF_8).split("\\r?\\n");if(records.length!=rows)throw new AssertionError("CSV record count differs");for(int i=0;i<rows;i++)if(!records[i].equals(i+",café 世界 😀 "+i))throw new AssertionError("CSV record differs at "+i);}else verifyXml(actual,rows);
    byte[] digest=java.security.MessageDigest.getInstance("SHA-256").digest(actual);StringBuilder sha=new StringBuilder();for(byte b:digest)sha.append(String.format("%02x",b&255));
    String row=rows+","+repeat+","+trans+","+js+","+sha;lines.add(row);System.out.println(row);Files.write(Paths.get("C:/ETL/Reports/javascript-mapping-benchmark.csv"),lines,StandardCharsets.UTF_8);
   }}
  }
 }
}
