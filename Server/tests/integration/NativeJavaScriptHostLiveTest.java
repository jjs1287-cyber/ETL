import java.net.URL;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import org.etlforge.integration.server.implementation.webservice.interchange.jobcontrol.client.*;
public class NativeJavaScriptHostLiveTest extends MixedJavaScriptRuntimeTest {
 static int checks;
 static void check(String name,String template,String expected)throws Exception{
  String actual=runTemplate(template);if(!expected.equals(actual))throw new AssertionError(name+": "+actual);checks++;System.out.println("PASS "+name);
 }
 static void reject(String name,String code)throws Exception{
  String actual=runTemplate("<javascript>"+code+"</javascript>");if(!actual.startsWith("ERROR:"))throw new AssertionError(name+" accepted: "+actual);checks++;System.out.println("PASS "+name);
 }
 static void waitFor(long instance,boolean success)throws Exception{
  for(int n=0;n<600;n++){String status=NativeJavaScriptMappingTest.status(instance);if(status.equals("3")||status.equals("4")||status.equals("5")){if(!success)throw new AssertionError("Negative operation succeeded");return;}if(status.equals("6")){if(success)throw new AssertionError("Host operation failed; instance "+instance);return;}Thread.sleep(100);}
  throw new AssertionError("Operation remained pending/running: "+instance);
 }
 public static void main(String[] args)throws Exception{
  init();
  check("parent/child JavaScript realms","<javascript>var parentScoped='parent';</javascript><trans>$childScope=RunScript('sc."+NativeJavaScriptMappingTest.guid(61)+"');''</trans><javascript>SetScriptResult(parentScoped+':'+$childScope);</javascript>","parent:undefined");
  check("child error rolls back global overlay","<trans>$JsRollback=1;result=Eval(RunScript('sc."+NativeJavaScriptMappingTest.guid(62)+"'),'caught');$JsRollback</trans>","1");
  check("no setter emits empty text","<javascript>var x=1;</javascript>","");
  check("connector sees staged charset global","<javascript>ETLForge.SetVar('etlforge.source.text.character_encoding','windows-1252');SetScriptResult(ETLForge.ReadFile('s."+NativeJavaScriptMappingTest.guid(17)+"'));</javascript>","café");
  reject("loop deadline","while(true){}");
  reject("regex deadline","SetScriptResult(/(a+)+$/.test(Array(100).join('a')+'!'));");
  reject("source path outside mapping","SetScriptResult(ETLForge.GetSource('unknown$path$'));");
  reject("filesystem override traversal","ETLForge.ReadFile('s."+NativeJavaScriptMappingTest.guid(10)+"','../private.txt');");
  reject("shell unavailable","Function('return this')().require('child_process');");
  reject("module unavailable","import('fs');");
  reject("unregistered host unavailable","ETLForge.RunOperation('anything');");
  JobControl jobs=new JobControlServiceLocator().getkonga_jobcontrol(new URL("http://127.0.0.1:47100/axis/konga_jobcontrol"));
  WsRunOperationResult host=jobs.runOperationInstanceMD5Password("Admin",hash,NativeJavaScriptMappingTest.guid(50));waitFor(host.getOperationInstanceId(),true);
  if(!Arrays.equals(Files.readAllBytes(NativeJavaScriptMappingTest.ROOT.resolve("input/input.xml")),Files.readAllBytes(NativeJavaScriptMappingTest.ROOT.resolve("output/host-functions.txt"))))throw new AssertionError("Host file output differs");
  checks++;System.out.println("PASS queued file/database/log/global host functions, instance="+host.getOperationInstanceId());
  WsRunOperationResult timeout=jobs.runOperationInstanceMD5Password("Admin",hash,NativeJavaScriptMappingTest.guid(51));waitFor(timeout.getOperationInstanceId(),false);checks++;System.out.println("PASS timeout finalizes failed operation, instance="+timeout.getOperationInstanceId());
  check("worker usable after failures","<javascript>SetScriptResult(42);</javascript>","42");
  ExecutorService workers=Executors.newFixedThreadPool(4);
  try{
   java.util.List<Future<String>> results=new ArrayList<>();
   for(int n=0;n<12;n++){final int number=n;results.add(workers.submit(()->runTemplate("<javascript>var row="+number+";SetScriptResult(row);</javascript>")));}
   for(int n=0;n<12;n++)if(!Integer.toString(n).equals(results.get(n).get()))throw new AssertionError("Concurrent JavaScript scope leak");
   checks++;System.out.println("PASS 12 concurrent isolated JavaScript requests");
  }finally{workers.shutdownNow();}
  System.out.println("Passed "+checks+" live host/safety checks");
 }
}
