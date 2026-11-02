import java.nio.file.*;
import java.net.URL;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.*;
public class EndpointCompressionCleanupLiveTest extends EndpointCompressionLiveTest {
 public static void main(String[] args)throws Exception{
  LocalVariablesRuntimeTest.init();hash=LocalVariablesRuntimeTest.hash;setup();Files.copy(ROOT.resolve("input.zip"),ROOT.resolve("delete.zip"));
  InterchangeDataHolder data=new InterchangeDataHolder();data.setInterchangeProjectsTab(table(new String[]{"InterchangeProject_ID","InterchangeProject_Name"},new String[]{id(1),"Compression source cleanup verification"}));
  data.setSourcesTab(table(new String[]{"Source_ID","Source_Name","Source_Type_ID","Locator","File_Name","Login","Password","Driver","DriverType","ConnectString","RenameFileTo"},new String[]{id(10),"Delete original archive once","16",ROOT.toString(),"delete.zip",null,null,null,null,null,"/\\delete/\\"}));
  data.setEntityProperties(table(new String[]{"EntityType_ID","Entity_ID","KeyName","KeyValue"},new String[]{"2",id(10),"compression_enabled","true"}));
  CDeployResults deployed=new DeployInterchangeDataServiceLocator().getkonga_deploy_interchange_data(new URL("http://127.0.0.1:47100/axis/konga_deploy_interchange_data")).deployInterchangeData("Admin",hash,new InterchangeIdHolder(),data);
  check(deployed.getDeployStatus()<=1,"deploy compressed source with original-file delete action");check(run(0).equals("3"),"multiple archive members finish without duplicate cleanup warnings");check(!Files.exists(ROOT.resolve("delete.zip")),"success action deletes original archive");bytes(out(0).resolve("entry_alpha.dat"),A,"first member delivered before source deletion");bytes(out(0).resolve("entry_beta.dat"),B,"second member delivered before source deletion");System.out.println("Passed "+checks+" source cleanup checks");
 }
}
