import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.*;
import java.net.URL;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.*;
public class EndpointCompressionXmlLiveTest extends EndpointCompressionLiveTest {
 static String encoded(String value){return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));}
 static void xmlMapping()throws Exception{
  Files.createDirectories(out(15));try(ZipOutputStream z=new ZipOutputStream(Files.newOutputStream(ROOT.resolve("xml.zip")))){for(int n=1;n<=2;n++){z.putNextEntry(new ZipEntry("record"+n+".xml"));z.write(("<rows><row><id>"+n+"</id><name>café 世界 😀</name></row></rows>").getBytes(StandardCharsets.UTF_8));z.closeEntry();}}
  NativeJavaScriptMappingTest.hash=hash;String schema="EndpointCompressionVerification.xsd";
  NativeJavaScriptMappingTest.upload(schema,"<xs:schema xmlns:xs='http://www.w3.org/2001/XMLSchema'><xs:element name='rows'><xs:complexType><xs:sequence><xs:element name='row' minOccurs='0' maxOccurs='unbounded'><xs:complexType><xs:sequence><xs:element name='id' type='xs:string'/><xs:element name='name' type='xs:string'/></xs:sequence></xs:complexType></xs:element></xs:sequence></xs:complexType></xs:element></xs:schema>");
  InterchangeDataHolder data=new InterchangeDataHolder();data.setInterchangeProjectsTab(table(new String[]{"InterchangeProject_ID","InterchangeProject_Name"},new String[]{id(1),"Compression XML verification"}));
  data.setSourcesTab(table(new String[]{"Source_ID","Source_Name","Source_Type_ID","Locator","File_Name","Login","Password","Driver","DriverType","ConnectString"},new String[]{id(25),"Compressed XML input","16",ROOT.toString(),"xml.zip",null,null,null,null,null}));
  data.setTargetsTab(table(new String[]{"Target_ID","Target_Name","Source_Type_ID","Locator","File_Name","Login","Password","Driver","DriverType","ConnectString","Target_Target_ID"},new String[]{id(65),"Compressed XML output","16",out(15).toString(),"mapped_[file].[ext]",null,null,null,null,null,"-1"}));
  data.setTransformationsTab_New(table(new String[]{"transformation_id","transformation_name","source_type_id","target_type_id","source_text_doc","target_text_doc","source_sourceid","target_targetid","source_xml","target_xml","natureofsource","natureoftarget","sourcedtd_root","targetdtd_root","sourcejmsmessageid","targetjmsmessageid"},new String[]{id(150),"Compressed XML identity","4","4",null,null,null,null,schema,schema,"1","1","rows","rows",null,null}));
  data.setTransformation_DefinitionsTab(table(new String[]{"transformation_id","target_field_id","transformation"},new String[]{id(150),"rows$row.id$",encoded("<trans>rows$row.id$</trans>")},new String[]{id(150),"rows$row.name$",encoded("<trans>rows$row.name$</trans>")}));
  data.setOperationsTab(table(new String[]{"Operation_ID","Operation_Name","Operation_Type_ID","Source_ID","Target_ID","Ignore_History","Schedule_ID","Transformation_ID","Success_Operation_ID","Success_Message_ID","Failure_Operation_ID","Failure_Message_ID"},new String[]{id(115),"Compressed XML transformation","3",id(25),id(65),"1",null,id(150),null,null,null,null}));
  data.setOperationPipelines(table(new String[]{"Operation_ID","ActivityType_ID","Activity_ID"},new String[]{id(115),"3",id(25)},new String[]{id(115),"5",id(150)},new String[]{id(115),"4",id(65)}));
  data.setEntityProperties(table(new String[]{"EntityType_ID","Entity_ID","KeyName","KeyValue"},new String[]{"2",id(25),"compression_enabled","true"},new String[]{"3",id(65),"compression_enabled","true"},new String[]{"3",id(65),"compression_archive_name","mapped"}));
  CDeployResults deployed=new DeployInterchangeDataServiceLocator().getkonga_deploy_interchange_data(new URL("http://127.0.0.1:47100/axis/konga_deploy_interchange_data")).deployInterchangeData("Admin",hash,new InterchangeIdHolder(),data);check(deployed.getDeployStatus()<=1,"deploy real compressed XML mapping");check(run(15).equals("3"),"transform every XML member and create combined ZIP");
  Path extracted=ROOT.resolve("xml-verified");unpack(out(15).resolve("mapped.zip"),extracted,"");
  javax.xml.parsers.DocumentBuilderFactory factory=javax.xml.parsers.DocumentBuilderFactory.newInstance();factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true);
  for(int n=1;n<=2;n++){org.w3c.dom.Document document=factory.newDocumentBuilder().parse(extracted.resolve("mapped_record"+n+".xml").toFile());check(document.getElementsByTagName("id").item(0).getTextContent().equals(Integer.toString(n))&&document.getElementsByTagName("name").item(0).getTextContent().equals("café 世界 😀"),"mapped archive member preserves values "+n);}
 }

 public static void main(String[] args)throws Exception{LocalVariablesRuntimeTest.init();hash=LocalVariablesRuntimeTest.hash;xmlMapping();System.out.println("Passed "+checks+" live XML compression checks");}
}
