import java.nio.charset.StandardCharsets;
import java.util.*;
import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.*;
public class MixedJavaScriptRuntimeTest extends LocalVariablesRuntimeTest {
 static String runTemplate(String source)throws Exception{
  DebuggingParameters params=new DebuggingParameters();params.setCommand("Run");params.setDebuggingSessionId(UUID.randomUUID().toString());params.setBreakpoints(new Breakpoint[0]);params.setGlobalVariables(new WsDataElement[0]);
  try{
   WsTestResult result=api.testExpression("Admin",hash,"",Base64.getEncoder().encodeToString(source.getBytes(StandardCharsets.UTF_8)),params);
   if(result.getErrors()!=null&&!result.getErrors().isEmpty())return "ERROR:"+result.getErrors().replace('\n',' ');
   return new String(Base64.getMimeDecoder().decode(result.getResult()),StandardCharsets.UTF_8);
  }catch(Exception e){return "ERROR:"+e.getMessage();}
 }
 static String[][] tests={
  {"javascript","<javascript>SetScriptResult(2+3);</javascript>","5"},
  {"literal","before<javascript>SetScriptResult('middle');</javascript>after","beforemiddleafter"},
  {"multiple","<javascript>var x=3;</javascript>|<javascript>SetScriptResult(x+2);</javascript>","|5"},
  {"mixed globals","<trans>$hello='world';'A'</trans><javascript>SetScriptResult(ETLForge.GetVar('$hello'));</javascript><trans>'Z'</trans>","AworldZ"},
  {"dollar globals","<javascript>$count=4;SetScriptResult($count);</javascript><trans>$count+1</trans>","45"},
  {"trans refresh","<javascript>$count=1;</javascript><trans>$count=3;''</trans><javascript>SetScriptResult($count);</javascript>","3"},
  {"unicode","<javascript>SetScriptResult('Gr\\u00fc\\u00dfe \\u4e16\\u754c \\ud83d\\ude00');</javascript>","Grüße 世界 😀"},
  {"last setter","<javascript>SetScriptResult('a');SetScriptOutput('b');</javascript>","b"},
  {"json","<javascript>SetScriptResult(JSON.stringify(JSON.parse('{\"a\":1}')));</javascript>","{\"a\":1}"},
  {"boolean","<javascript>SetScriptResult(true);</javascript>","1"},
  {"quoted delimiter","<javascript>SetScriptResult('</javascript>');</javascript>","</javascript>"},
  {"comment delimiter","<javascript>/* </javascript> */SetScriptResult('ok');</javascript>","ok"},
  {"regex","<javascript>SetScriptResult(/<\\/javascript>/.test('</javascript>'));</javascript>","1"},
  {"math","<javascript>SetScriptResult(Math.sqrt(81));</javascript>","9"},
  {"globals array","<javascript>ETLForge.SetVar('arr',[1,2]);</javascript><trans>$arr[1]</trans>","2"},
  {"globals map","<javascript>$map={x:'value'};</javascript><trans>$map['x']</trans>","value"},
  {"strict","<javascript>accidental=1;</javascript>","ERROR"},
  {"sandbox","<javascript>Java.type('java.lang.Runtime');</javascript>","ERROR"},
  {"invalid","<javascript>var = ;</javascript>","ERROR"},
  {"undefined local","<javascript>SetScriptResult(x);</javascript>","ERROR"},
  {"legacy locals","<trans>x=1;x</trans><javascript>SetScriptResult(2);</javascript><trans>x</trans>","121"}
 };
 public static void main(String[] args)throws Exception{
  init();int failures=0;for(String[] test:tests){String actual=runTemplate(test[1]);boolean ok=test[2].equals("ERROR")?actual.startsWith("ERROR:"):actual.equals(test[2]);if(!ok)failures++;System.out.println((ok?"PASS ":"FAIL ")+test[0]+" => "+actual);}
  if(failures>0)throw new AssertionError(failures+" mixed JavaScript runtime failures");
  System.out.println("Passed "+tests.length+" live native JavaScript checks");
 }
}
