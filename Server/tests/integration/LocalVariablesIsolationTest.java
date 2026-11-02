import java.net.URL;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.*;
public class LocalVariablesIsolationTest extends LocalVariablesRuntimeTest {
 static InterchangeDataTable table(String[] columns,String[]... values){InterchangeDataRow[] rows=new InterchangeDataRow[values.length];for(int i=0;i<values.length;i++)rows[i]=new InterchangeDataRow(values[i]);return new InterchangeDataTable(columns,rows);}
 static String quote(String s){return "'"+s+"'";}
 static String call(String guid,String args){return "RunScript('sc."+guid+"'"+args+")";}
 static void check(String name,String script,String expected)throws Exception{String actual=run(script);if(!actual.equals(expected))throw new AssertionError(name+": "+actual);System.out.println("PASS "+name);}
 public static void main(String[] args)throws Exception{
  init();String project=UUID.randomUUID().toString();String child=UUID.randomUUID().toString(),bad=UUID.randomUUID().toString(),argument=UUID.randomUUID().toString(),recurse=UUID.randomUUID().toString(),array=UUID.randomUUID().toString(),operation=UUID.randomUUID().toString(),operationChild=UUID.randomUUID().toString();
  String[][] scripts={
   {operationChild,"Operation scope check","<trans>If(false,x='wrong');$localIsolation=Eval(x,'isolated');WriteToOperationLog('LOCAL_SCOPE:'+$localIsolation);$localIsolation</trans>"},
   {child,"Own local","<trans>x='child';x</trans>"},
   {bad,"Unassigned isolated local","<trans>If(false,x='wrong');x</trans>"},
   {argument,"Explicit argument","<trans>ArgumentList(value);value+' child'</trans>"},
   {array,"Array argument","<trans>ArgumentList(value);value[0]='child';value[0]</trans>"},
   {recurse,"Recursive local","<trans>n=Int(_1);If(n>0,result="+call(recurse,",n-1")+",result='end');String(n)+':'+result</trans>"}
  };
  InterchangeDataHolder data=new InterchangeDataHolder();
  data.setInterchangeProjectsTab(table(new String[]{"InterchangeProject_ID","InterchangeProject_Name"},new String[]{project,"ETLForge Local Variables Verification "+project.substring(0,8)}));
  for(String[] row:scripts)row[2]=Base64.getEncoder().encodeToString(row[2].getBytes(StandardCharsets.UTF_8));
  data.setScriptsTab(table(new String[]{"Script_ID","Script_Name","Script_Text"},scripts));
  data.setOperationsTab(table(new String[]{"Operation_ID","Operation_Name","Operation_Type_ID","Source_ID","Target_ID","Schedule_ID","Transformation_ID","Success_Operation_ID","Success_Message_ID","Failure_Operation_ID","Failure_Message_ID"},new String[]{operation,"Local namespace isolation","8",null,null,null,null,null,null,null,null}));
  data.setOperationPipelines(table(new String[]{"Operation_ID","ActivityType_ID","Activity_ID"},new String[]{operation,"1",operationChild}));
  DeployInterchangeData deploy=new DeployInterchangeDataServiceLocator().getkonga_deploy_interchange_data(new URL("http://127.0.0.1:47100/axis/konga_deploy_interchange_data"));
  CDeployResults result=deploy.deployInterchangeData("Admin",hash,new InterchangeIdHolder(),data);
  if(result==null||result.getDeployStatus()>1)throw new AssertionError("Fixture deploy failed");
  Files.write(Paths.get("C:/ETL/Reports/locals-isolation-fixture.txt"),(project+"\n"+operation+"\n").getBytes(StandardCharsets.UTF_8));
  check("parent restored after child","x='parent';y="+call(child,"")+";x+':'+y","parent:child");
  check("parent restored after caught child error","x='parent';y=Eval("+call(bad,"")+",'caught');x+':'+y","parent:caught");
  check("explicit arguments","x='parent';y="+call(argument,",x")+";x+':'+y","parent:parent child");
  check("array arguments copied","x={'parent'};y="+call(array,",x")+";x[0]+':'+y","parent:child");
  check("recursive scopes",call(recurse,",3"),"3:2:1:0:end");
  check("repeat child calls","x='parent';"+call(child,"")+";"+call(child,"")+";x","parent");
  check("RunOperation receives no locals","x='parent';RunOperation('op."+operation+"',true);x+':'+$localIsolation","parent:isolated");
  check("async parent retained","x='parent';RunOperation('op."+operation+"',false);x","parent");
  ExecutorService pool=Executors.newFixedThreadPool(4);
  try {List<Future<String>> jobs=new ArrayList<>();for(int i=0;i<12;i++){final int n=i;jobs.add(pool.submit(()->run("x='thread"+n+"';y="+call(child,"")+";x+':'+y")));}for(int i=0;i<jobs.size();i++)if(!jobs.get(i).get().equals("thread"+i+":child"))throw new AssertionError("Concurrent scope leak");System.out.println("PASS 12 concurrent parent/child requests");}finally{pool.shutdownNow();}
 }
}
