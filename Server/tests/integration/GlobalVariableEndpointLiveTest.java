import java.net.URL;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.*;
import org.etlforge.integration.server.implementation.webservice.interchange.jobcontrol.client.*;
public class GlobalVariableEndpointLiveTest {
 static final Path ROOT=Paths.get("C:/ETL/Samples/GlobalVariableEndpoints");
 static String hash;
 static int checks;
 static String id(int n){return String.format("42500000-0000-4000-8000-%012d",n);}
 static InterchangeDataTable table(String[] names,String[]... rows){return StudioFileCopySmoke.table(names,rows);}
 static String encoded(String text){return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));}
 static void check(boolean condition,String label){if(!condition)throw new AssertionError(label);checks++;System.out.println("PASS "+label);}
 static String scriptOutput(int target,String value){return "WriteFile('t."+id(target)+"',"+value+");FlushFile('t."+id(target)+"');";}
 static void setup()throws Exception{
  Files.createDirectories(ROOT);Files.write(ROOT.resolve("input.txt"),"cafÃ© ä¸–ç•Œ ðŸ˜€\r\nsecond line\t\"quotes\"".getBytes(StandardCharsets.UTF_8));
  Files.write(ROOT.resolve("bad.bin"),new byte[]{(byte)0xc3,0x28});Files.write(ROOT.resolve("nul-bytes.bin"),new byte[]{'a',0,'b'});
  Files.write(ROOT.resolve("multi1.txt"),"first".getBytes(StandardCharsets.UTF_8));Files.write(ROOT.resolve("multi2.txt"),"second".getBytes(StandardCharsets.UTF_8));
  InterchangeDataHolder data=new InterchangeDataHolder();
  data.setInterchangeProjectsTab(table(new String[]{"InterchangeProject_ID","InterchangeProject_Name"},new String[]{id(1),"Global variable endpoints verification"}));
  data.setSourcesTab(table(new String[]{"Source_ID","Source_Name","Source_Type_ID","Locator","File_Name","Login","Password","Driver","DriverType","ConnectString"},
   new String[]{id(10),"Global text source","17","$Payload",null,null,null,null,null,null},
   new String[]{id(11),"Local reference text","16",ROOT.toString(),"input.txt",null,null,null,null,null},
   new String[]{id(12),"Unset global text source","17","MissingPayload",null,null,null,null,null,null},
   new String[]{id(13),"Malformed UTF8 fixture","16",ROOT.toString(),"bad.bin",null,null,null,null,null},
   new String[]{id(14),"NUL text fixture","16",ROOT.toString(),"nul-bytes.bin",null,null,null,null,null},
   new String[]{id(15),"Multiple files fixture","16",ROOT.toString(),"multi*.txt",null,null,null,null,null}));
  data.setTargetsTab(table(new String[]{"Target_ID","Target_Name","Source_Type_ID","Locator","File_Name","Login","Password","Driver","DriverType","ConnectString","Target_Target_ID"},
   new String[]{id(20),"Global text target","17","Payload",null,null,null,null,null,null,"-1"},
   new String[]{id(21),"Global endpoint output","16",ROOT.toString(),"result.txt",null,null,null,null,null,"-1"}));
  String[] expressions={
   "$Payload='cafÃ© ä¸–ç•Œ ðŸ˜€';"+scriptOutput(21,"ReadFile('s."+id(10)+"')"),
   scriptOutput(20,"'cafÃ© ä¸–ç•Œ ðŸ˜€\\r\\nsecond line'")+scriptOutput(21,"$Payload"),
   "$Payload='';"+scriptOutput(21,"'['+ReadFile('s."+id(10)+"')+']'"),
   "ReadFile('s."+id(12)+"');",
   "$Payload=123;ReadFile('s."+id(10)+"');",
   scriptOutput(20,"'replace me'")+scriptOutput(20,"'new value'")+scriptOutput(21,"$Payload"),
   scriptOutput(21,"$Payload")
  };
  List<String[]> scripts=new ArrayList<>(),ops=new ArrayList<>(),pipeline=new ArrayList<>();
  for(int i=0;i<expressions.length;i++){
   scripts.add(new String[]{id(30+i),"Global endpoint fixture "+i,encoded("<trans>"+expressions[i]+"</trans>")});
   ops.add(new String[]{id(40+i),"Global endpoint script "+i,"8",null,null,"1",null});
   pipeline.add(new String[]{id(40+i),"1",id(30+i)});
  }
  ops.add(new String[]{id(50),"Local file to global variable archive","1",id(11),id(20),"1",null});
  pipeline.add(new String[]{id(50),"3",id(11)});pipeline.add(new String[]{id(50),"4",id(20)});pipeline.add(new String[]{id(50),"1",id(36)});
  ops.add(new String[]{id(51),"Global variable to file archive","1",id(10),id(21),"1",null});
  pipeline.add(new String[]{id(51),"1",id(30)});pipeline.add(new String[]{id(51),"3",id(10)});pipeline.add(new String[]{id(51),"4",id(21)});
  NativeJavaScriptMappingTest.hash=hash;
  String schema="GlobalVariableEndpoint.xsd";
  NativeJavaScriptMappingTest.upload(schema,"<xs:schema xmlns:xs='http://www.w3.org/2001/XMLSchema'><xs:element name='rows'><xs:complexType><xs:sequence><xs:element name='row' minOccurs='0' maxOccurs='unbounded'><xs:complexType><xs:sequence><xs:element name='id' type='xs:string'/><xs:element name='name' type='xs:string'/></xs:sequence></xs:complexType></xs:element></xs:sequence></xs:complexType></xs:element></xs:schema>");
  data.setTransformationsTab_New(table(new String[]{"transformation_id","transformation_name","source_type_id","target_type_id","source_text_doc","target_text_doc","source_sourceid","target_targetid","source_xml","target_xml","natureofsource","natureoftarget","sourcedtd_root","targetdtd_root","sourcejmsmessageid","targetjmsmessageid"},new String[]{id(60),"Global endpoint XML identity","4","4",null,null,null,null,schema,schema,"1","1","rows","rows",null,null}));
  data.setTransformation_DefinitionsTab(table(new String[]{"transformation_id","target_field_id","transformation"},new String[]{id(60),"rows$row.id$",encoded("<trans>rows$row.id$</trans>")},new String[]{id(60),"rows$row.name$",encoded("<trans>rows$row.name$</trans>")}));
  scripts.add(new String[]{id(37),"Seed global XML",encoded("<trans>$Payload='<rows><row><id>1</id><name>cafÃ© ä¸–ç•Œ ðŸ˜€</name></row><row><id>2</id><name>second</name></row></rows>';</trans>")});
  ops.add(new String[]{id(52),"Global XML transformation","3",id(10),id(20),"1",id(60)});
  pipeline.add(new String[]{id(52),"1",id(37)});pipeline.add(new String[]{id(52),"3",id(10)});pipeline.add(new String[]{id(52),"5",id(60)});pipeline.add(new String[]{id(52),"4",id(20)});pipeline.add(new String[]{id(52),"1",id(36)});
  for(int i=0;i<3;i++){
   ops.add(new String[]{id(53+i),"Rejected global target fixture "+i,"1",id(13+i),id(20),"1",null});
   pipeline.add(new String[]{id(53+i),"3",id(13+i)});pipeline.add(new String[]{id(53+i),"4",id(20)});
  }
  data.setScriptsTab(table(new String[]{"Script_ID","Script_Name","Script_Text"},scripts.toArray(new String[0][])));
  for(int i=0;i<ops.size();i++)ops.set(i,Arrays.copyOf(ops.get(i),12));
  data.setOperationsTab(table(new String[]{"Operation_ID","Operation_Name","Operation_Type_ID","Source_ID","Target_ID","Ignore_History","Transformation_ID","Schedule_ID","Success_Operation_ID","Success_Message_ID","Failure_Operation_ID","Failure_Message_ID"},ops.toArray(new String[0][])));
  data.setOperationPipelines(table(new String[]{"Operation_ID","ActivityType_ID","Activity_ID"},pipeline.toArray(new String[0][])));
  CDeployResults result=new DeployInterchangeDataServiceLocator().getkonga_deploy_interchange_data(new URL("http://127.0.0.1:47100/axis/konga_deploy_interchange_data")).deployInterchangeData("Admin",hash,new InterchangeIdHolder(),data);
  check(result!=null&&result.getDeployStatus()<=1,"deploy distinct Global Variable source and target types");
 }
 static String run(int operation)throws Exception{
  Files.deleteIfExists(ROOT.resolve("result.txt"));
  JobControl jobs=new JobControlServiceLocator().getkonga_jobcontrol(new URL("http://127.0.0.1:47100/axis/konga_jobcontrol"));
  long instance=jobs.runOperationInstanceMD5Password("Admin",hash,id(operation)).getOperationInstanceId();
  for(int n=0;n<400;n++){
   String status=NativeJavaScriptMappingTest.status(instance);
   if(!status.isEmpty()&&!status.equals("0")&&!status.equals("2")){System.out.println("instance="+instance+" status="+status);return status;}
   Thread.sleep(100);
  }throw new AssertionError("Operation timeout "+instance);
 }
 static String output()throws Exception{return new String(Files.readAllBytes(ROOT.resolve("result.txt")),StandardCharsets.UTF_8);}
 static void success(int operation,String expected,String label)throws Exception{check(run(operation).equals("3"),label+" status");check(output().equals(expected),label+" exact output");}
 public static void main(String[] args)throws Exception{
  LocalVariablesRuntimeTest.init();hash=LocalVariablesRuntimeTest.hash;setup();
  success(40,"cafÃ© ä¸–ç•Œ ðŸ˜€","ReadFile reads a global variable with optional dollar prefix");
  success(41,"cafÃ© ä¸–ç•Œ ðŸ˜€\r\nsecond line","WriteFile/FlushFile sets the global variable");
  success(42,"[]","empty text is distinct from an unset variable");
  check(!run(43).equals("3"),"unset source fails");
  check(!run(44).equals("3"),"non-text source fails");
  success(45,"new value","a second flush replaces the previous value");
  success(50,new String(Files.readAllBytes(ROOT.resolve("input.txt")),StandardCharsets.UTF_8),"archive local file to global variable then read it in a script");
  success(51,"cafÃ© ä¸–ç•Œ ðŸ˜€","archive global variable to a local file");
  check(run(52).equals("3"),"global source to XML transformation to global target status");
  javax.xml.parsers.DocumentBuilderFactory factory=javax.xml.parsers.DocumentBuilderFactory.newInstance();factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true);
  org.w3c.dom.Document document=factory.newDocumentBuilder().parse(ROOT.resolve("result.txt").toFile());
  check(document.getElementsByTagName("row").getLength()==2&&document.getElementsByTagName("name").item(0).getTextContent().equals("cafÃ© ä¸–ç•Œ ðŸ˜€"),"XML mapping preserves all rows and Unicode");
  check(!run(53).equals("3"),"malformed UTF8 target is rejected");
  check(!run(54).equals("3"),"NUL-containing target is rejected");
  check(!run(55).equals("3"),"multiple output files are rejected instead of losing data");
  System.out.println("Passed "+checks+" live global-variable endpoint checks");
 }
}
