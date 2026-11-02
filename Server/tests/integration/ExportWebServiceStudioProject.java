import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.data.entity.*;
import org.etlforge.integration.data.entity.id.*;
import org.etlforge.integration.data.entity.webservicecall.WsdlFile;
import org.etlforge.integration.data.entity.operation.*;
import org.etlforge.integration.data.entity.operation.pipeline.*;
import org.etlforge.integration.data.structure.*;
import org.etlforge.integration.data.location.LocalFileLocation;
import org.etlforge.integration.data.Password;
import org.etlforge.util.persist.XmlPersistor;
public class ExportWebServiceStudioProject {
 static Path root=Paths.get("C:/ETL/Samples/WebServices/StudioProject/ETLForge Web Service Examples");
 static String id(int n){return String.format("42000000-0000-4000-8000-%012d",n);}
 static void props(IntegrationEntity e,String...pairs){StringKeyValuePairs kv=new StringKeyValuePairs();for(int i=0;i<pairs.length;i+=2)kv.put(pairs[i],pairs[i+1]);e.setPropertyValues(kv);}
 static void save(IntegrationEntity e)throws Exception{Path dir=root.resolve("Data/"+e.getEntityType().name());Files.createDirectories(dir);e.setDeployed(true);e.setDeployDirty(false);XmlPersistor p=XmlPersistor.newRoot("Entity");p.putString("type",e.getEntityType().name());e.writeTo(p);p.store(dir.resolve(e.getID()+".xml").toFile());}
 public static void main(String[]args)throws Exception{
  Files.createDirectories(root);
  WebServiceCall ws=new WebServiceCall(new WebServiceCallId(id(2)),"Local Echo SOAP");WsdlFile wf=new WsdlFile("ETLForgeEcho.wsdl");wf.put("status","0");wf.put("wsdlname","ETLForgeEcho.wsdl");ws.setWsdlFile(wf);
  props(ws,"service","EchoService","port","EchoPort","binding","EchoBinding","operation","Echo","soapversion","1","soapstyle","document","soapbodyuse","literal");ws.setUrl("http://127.0.0.1:47100/etlforge/soap?id="+id(7));ws.setSoapAction("urn:etlforge:echo/Echo");ws.setTargetNamespace("urn:etlforge:echo");ws.setHttpUserName("Admin");String pw=new String(Files.readAllBytes(Paths.get("C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt")),StandardCharsets.UTF_8).trim();ws.setHttpPassword(com.google.common.base.Optional.of(Password.encrypt(pw)));
  Transformation host=new Transformation(new TransformationId(id(3)),"Hosted Echo request to response");host.setInputStructure(ws.getInputStructure());host.setOutputStructure(ws.getOutputStructure());host.addMapping("root$transaction$body$EchoResponse$Text$\t<trans>\nroot$transaction$body$Echo$Text$\n</trans>");
  Transformation req=new Transformation(new TransformationId(id(4)),"Build Echo SOAP request");req.setInputStructure(new NoStructure());req.setOutputStructure(ws.getInputStructure());req.addMapping("root$transaction.body$Echo$Text$\t<trans>\n\"Hello from ETLForge 42 — SOAP round trip\"\n</trans>");
  Transformation res=new Transformation(new TransformationId(id(5)),"Save Echo SOAP response");res.setInputStructure(ws.getOutputStructure());XmlStructure out=new XmlStructure();out.setFilePath("EchoResult.xsd");out.setRootNodeName("EchoResult");res.setOutputStructure(out);res.addMapping("EchoResult$Text$\t<trans>\nroot$transaction#1.response$body$EchoResponse$Text$\n</trans>");
  Target target=new Target(new TargetId(id(6)),"SOAP response file",new LocalFileLocation(Paths.get("C:/ETL/Samples/WebServices/output/echo-[unique].xml").toFile()));
  Operation hop=new Operation(new OperationId(id(7)),"Hosted Web Service - Echo");hop.setOperationType(HostedWebServiceOperationType.TYPE);hop.setPipeline(new OperationPipeline(HostedWebServiceOperationType.TYPE,Arrays.asList(WebServiceCallActivity.newInstance(ws,"Web Service Call"),TransformationActivity.newInstance(host,"Transformation"))));
  Operation cop=new Operation(new OperationId(id(8)),"Web Service Call - Local Echo");cop.setOperationType(WebServiceOperationType.TYPE);cop.setPipeline(new OperationPipeline(WebServiceOperationType.TYPE,Arrays.asList(TransformationActivity.newInstance(req,"Request"),WebServiceCallActivity.newInstance(ws,"Web Service Call"),TransformationActivity.newInstance(res,"Response"),TargetActivity.newInstance(target,"Target"))));
  IntegrationEntity[] all={ws,host,req,res,target,hop,cop};for(IntegrationEntity e:all)save(e);
  XmlPersistor p=XmlPersistor.newRoot("Project");p.putString("name","ETLForge Web Service Examples");p.putString("projectId",id(1));for(EntityType type:Arrays.asList(EntityType.Operation,EntityType.Transformation,EntityType.Target,EntityType.WebServiceCall)){org.etlforge.util.persist.Persistor t=p.createChild("EntityType");t.putString("name",type.name());for(IntegrationEntity e:all)if(e.getEntityType()==type){org.etlforge.util.persist.Persistor item=t.createChild("Entity");item.putString("entityId",e.getID().toString());item.putString("name",e.getName());}}p.store(root.resolve("ETLForge Web Service Examples.xml").toFile());
  Properties pr=new Properties();pr.setProperty("project-id",id(1));pr.setProperty("saved-version","42.0.0.0");pr.setProperty("client-version","42.0.0.0");pr.setProperty("requires_version","5.4.0.1");pr.setProperty("saved-edition","enterprise");pr.setProperty("structure-file","ETLForge Web Service Examples.xml");pr.setProperty("deployed","true");pr.setProperty("deployable","true");pr.setProperty("deploy-dirty","false");try(java.io.OutputStream os=Files.newOutputStream(root.resolve("ETLForge Web Service Examples.jip"))){pr.store(os,"ETLForge web service examples");}
  System.out.println("Created Studio examples project");
 }
}

