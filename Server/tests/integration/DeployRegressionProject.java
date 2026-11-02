import java.nio.file.*;
import java.net.URL;
import java.util.*;
import java.sql.*;
import com.fasterxml.jackson.databind.*;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.*;
import org.etlforge.integration.server.implementation.webservice.interchange.jobcontrol.client.*;
public final class DeployRegressionProject {
 public static void main(String[] args)throws Exception{
  LocalVariablesRuntimeTest.init();ObjectMapper json=new ObjectMapper();JsonNode config=json.readTree(Files.readAllBytes(Paths.get("C:/ETL/Reports/regression-suite/deploy.json")));
  if(args.length==0||args[0].equals("deploy")){
   InterchangeDataHolder data=new InterchangeDataHolder();Iterator<Map.Entry<String,JsonNode>> tables=config.path("tables").fields();
   while(tables.hasNext()){Map.Entry<String,JsonNode> entry=tables.next();JsonNode t=entry.getValue();String[] cols=json.convertValue(t.path("columns"),String[].class);ArrayList<InterchangeDataRow> rows=new ArrayList<>();for(JsonNode r:t.path("rows"))rows.add(new InterchangeDataRow(json.convertValue(r,String[].class)));InterchangeDataHolder.class.getMethod("set"+entry.getKey(),InterchangeDataTable.class).invoke(data,new InterchangeDataTable(cols,rows.toArray(new InterchangeDataRow[0])));}
   DeployInterchangeData api=new DeployInterchangeDataServiceLocator().getkonga_deploy_interchange_data(new URL("http://127.0.0.1:47100/axis/konga_deploy_interchange_data"));((org.apache.axis.client.Stub)api).setTimeout(45000);
   for(String table:new String[]{"SourcesTab","TargetsTab","EntityProperties","ScriptsTab","OperationsTab"}){
    InterchangeDataTable original=(InterchangeDataTable)InterchangeDataHolder.class.getMethod("get"+table).invoke(data);if(original==null)continue;
    for(int offset=0;offset<original.getRows().length;offset+=(table.equals("EntityProperties")?10000:50)){InterchangeDataHolder batch=new InterchangeDataHolder();batch.setInterchangeProjectsTab(data.getInterchangeProjectsTab());
     InterchangeDataRow[] slice=Arrays.copyOfRange(original.getRows(),offset,Math.min(offset+(table.equals("EntityProperties")?10000:50),original.getRows().length));InterchangeDataHolder.class.getMethod("set"+table,InterchangeDataTable.class).invoke(batch,new InterchangeDataTable(original.getColumnNames(),slice));
     if(table.equals("OperationsTab")){Set<String> ids=new HashSet<>();for(InterchangeDataRow r:slice)ids.add(r.getFields()[0]);List<InterchangeDataRow> pipes=new ArrayList<>();for(InterchangeDataRow r:data.getOperationPipelines().getRows())if(ids.contains(r.getFields()[0]))pipes.add(r);batch.setOperationPipelines(new InterchangeDataTable(data.getOperationPipelines().getColumnNames(),pipes.toArray(new InterchangeDataRow[0])));}
     CDeployResults result=api.deployInterchangeData("Admin",LocalVariablesRuntimeTest.hash,new InterchangeIdHolder(),batch);if(result==null||result.getDeployStatus()>1)throw new AssertionError("Regression deployment failed for "+table);System.out.println("PASS deploy "+table+" batch "+offset);System.out.flush();
    }
   }System.out.println("PASS regression project deployment");return;
  }
  String operation=args.length>1?args[1]:config.path("master").asText();
  long start=System.nanoTime();long instance=new JobControlServiceLocator().getkonga_jobcontrol(new URL("http://127.0.0.1:47100/axis/konga_jobcontrol")).runOperationInstanceMD5Password("Admin",LocalVariablesRuntimeTest.hash,operation).getOperationInstanceId();System.out.println("Started instance="+instance);System.out.flush();
  Class.forName("org.postgresql.Driver");try(Connection db=DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/etlforge_dev_tran","etlforge_dev",NativeJavaScriptMappingTest.databasePassword())){
   for(int n=0;n<3600;n++){String status="";try(Statement s=db.createStatement();ResultSet r=s.executeQuery("select status from operationlogtab where operation_instance_id="+instance)){if(r.next())status=r.getString(1);}if(!status.isEmpty()&&!status.equals("0")&&!status.equals("2")){long elapsed=(System.nanoTime()-start)/1000000;System.out.println("Finished instance="+instance+" status="+status+" elapsedMs="+elapsed);if(!status.equals("3")&&!status.equals("4"))throw new AssertionError("Regression operation failed; inspect instance "+instance);return;}Thread.sleep(500);}
   throw new AssertionError("Regression operation exceeded 30-minute monitoring limit; inspect running instance "+instance);
  }
 }
}


