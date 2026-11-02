import java.net.URL;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.etlforge.integration.data.CC;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.data.entity.id.SalesforceConnectorId;
import org.etlforge.integration.salesforce.*;
import org.etlforge.util.persist.XmlPersistor;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.*;
import org.etlforge.integration.server.implementation.webservice.sfdc.client.*;
public final class StudioSalesforceOAuthSmoke {
 static final String PROJECT="42420000-0000-4000-8000-000000000001", CONNECTOR="42420000-0000-4000-8000-000000000002";
 static void require(boolean ok,String message){if(!ok)throw new AssertionError(message);}
 public static void main(String[] args)throws Exception {
  Map<String,String> fields=new HashMap<String,String>();
  for(String line:Files.readAllLines(Paths.get("C:/SFTP/endowance/NewETL/Salesforce Dev account.txt"),StandardCharsets.UTF_8)){int colon=line.indexOf(':');if(colon>0)fields.put(line.substring(0,colon).trim().toLowerCase(Locale.ROOT),line.substring(colon+1).trim());}
  LoginCredentials creds=new LoginCredentials("","","",false,"oauth",fields.get("instance url"),fields.get("client id"),fields.get("client secret"));
  SalesforceConnector connector=new SalesforceConnector(new SalesforceConnectorId(CONNECTOR),"ETLForge isolated OAuth verification");
  connector.setOrgIdentifier(new OrgIdentifier(ServerHost.defaultHost(),SalesforceVersion.V_61,creds));
  require(!connector.getProperty("client_secret").equals(creds.clientSecret),"Plaintext persisted secret");
  XmlPersistor persist=XmlPersistor.newRoot("Entity");connector.writeTo(persist);
  SalesforceConnector reopened=new SalesforceConnector("reopened");require(!persist.toXml().contains(creds.clientSecret),"Plaintext secret in saved XML"); reopened.restoreFrom(XmlPersistor.load(persist.toXml()));
  require(reopened.getLoginCredentials().equals(creds),"OAuth save/reopen mismatch");
  System.out.println("PASS: connector persistence round trip with protected secret");
  final java.util.List<org.etlforge.integration.client.project.jitterpack.PostExportJob> jobs = new java.util.ArrayList<org.etlforge.integration.client.project.jitterpack.PostExportJob>();
  org.etlforge.integration.client.project.jitterpack.PostExportManager manager = new org.etlforge.integration.client.project.jitterpack.PostExportManager(){
   public boolean blankOutPasswords(){return true;}
   public void addJob(org.etlforge.integration.client.project.jitterpack.PostExportJob job){jobs.add(job);}
   public void runJobs(org.etlforge.integration.client.project.jitterpack.PostExportJob.Condition condition){for(org.etlforge.integration.client.project.jitterpack.PostExportJob job:jobs)if(job.getCondition().fulfills(condition))job.doJob();}
  };
  new org.etlforge.integration.client.project.export.SalesforceConnectorPreparer(connector).prepareItem(manager);
  require(connector.getProperty("client_secret")==null,"Secret not omitted from password-free backup");
  manager.runJobs(org.etlforge.integration.client.project.jitterpack.PostExportJob.Condition.ON_SUCCESS);
  require(connector.getLoginCredentials().equals(creds),"Export did not restore original connector");
  System.out.println("PASS: password-free backup preparer omits OAuth secret and restores working connector afterward");
  String password=new String(Files.readAllBytes(Paths.get("C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt")),StandardCharsets.UTF_8).trim();
  String md5=StudioWebServicesSmoke.hash(password.getBytes(StandardCharsets.UTF_8));
  SalesforceInfoProvider provider=new SalesforceInfoProviderServiceLocator().getkonga_salesforce_infoprovider(new URL("http://127.0.0.1:47112/axis/services/konga_salesforce_infoprovider"));
  ((org.apache.axis.client.Stub)provider).setTimeout(60000);
  WsOrgIdentifier org=new WsOrgIdentifier(); org.setUrl("https://login.salesforce.com/services/Soap/u/61.0");org.setSalesforceVersion("61.0");org.setUsername("");org.setPassword("");org.setSecurityToken("");org.setAuthType("oauth");org.setInstanceUrl(creds.instanceUrl);org.setClientId(creds.clientId);org.setClientSecret(creds.clientSecret);
  WsCredentials login=new WsCredentials("Admin",md5);
  if(args.length>0 && "lightning".equals(args[0])) org.setInstanceUrl(creds.instanceUrl.replace(".my.salesforce.com", ".lightning.force.com"));
  WsLoginResult result=provider.openConnection(login,org);
  require(result!=null && result.getSessionData()!=null && result.getSessionData().getSessionId()!=null,"Server OAuth login failed");
  require(provider.query(login,result.getSessionData(),"SELECT Id FROM User LIMIT 1").getError().getMessage()==null,"Server read-only query failed");
  require(provider.describeObject(login,result.getSessionData(),"Account").getError().getMessage()==null,"Server metadata describe failed");
  System.out.println("PASS: Studio SOAP credential transport, server OAuth login, read-only query and metadata");
  InterchangeDataHolder data=new InterchangeDataHolder();
  data.setInterchangeProjectsTab(StudioFileCopySmoke.table(new String[]{"InterchangeProject_ID","InterchangeProject_Name"},new String[]{PROJECT,"ETLForge isolated OAuth verification"}));
  data.setSalesforceConnectorsTab(StudioFileCopySmoke.table(new String[]{"salesforceconnector_id","salesforceconnector_name","api_version","server_host","user_name","salesforce_password","security_token","sandbox","auth_type","instance_url","client_id","client_secret"},new String[]{CONNECTOR,"ETLForge isolated OAuth verification","61.0","https://login.salesforce.com/","",CC.hide(""),CC.hide(""),"0","oauth",creds.instanceUrl,creds.clientId,connector.getProperty("client_secret")}));
  DeployInterchangeData deploy=new DeployInterchangeDataServiceLocator().getkonga_deploy_interchange_data(new URL("http://127.0.0.1:47100/axis/konga_deploy_interchange_data"));
  ((org.apache.axis.client.Stub)deploy).setTimeout(60000);
  CDeployResults deployed=deploy.deployInterchangeData("Admin",md5,new InterchangeIdHolder(),data);
  require(deployed!=null && deployed.getDeployStatus()<=1,"Isolated connector deployment failed");
  System.out.println("PASS: isolated OAuth connector deployed through native SOAP endpoint");
 }
}
