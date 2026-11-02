import org.etlforge.integration.data.script.ScriptValidator;
import org.etlforge.integration.data.script.ScriptStatus;
import org.etlforge.integration.data.script.NativeJavaScript;
public class MixedJavaScriptValidatorTest {
 static int count;
 static void check(String name,String text,boolean valid){
  ScriptStatus status=new ScriptValidator().validate(text);
  if((!status.isInvalid())!=valid)throw new AssertionError(name+": "+status);
  System.out.println("PASS "+name);count++;
 }
 public static void main(String[] args){
  check("javascript", "<javascript>SetScriptResult(1+2);</javascript>",true);
  check("mixed", "before<trans>x=1;x</trans>between<javascript>SetScriptResult('after');</javascript>tail",true);
  check("multiple javascript", "<javascript>var x=1;</javascript><javascript>SetScriptResult(x);</javascript>",true);
  check("quoted closing tag", "<javascript>SetScriptResult('</javascript>');</javascript>",true);
  check("comment closing tag", "<javascript>// </javascript>\nSetScriptResult(1);</javascript>",true);
  check("regexp", "<javascript>SetScriptResult(/<\\/javascript>/.test('x'));</javascript>",true);
  check("invalid syntax", "<javascript>var = ;</javascript>",false);
  check("missing closing", "<javascript>SetScriptResult(1);",false);
  check("mismatch", "<javascript>1</trans>",false);
  check("nested", "<javascript><trans>1</trans></javascript>",false);
  check("trans locals still reject", "<trans>x;x=1</trans><javascript>SetScriptResult(1);</javascript>",false);
  check("validation never executes", "<javascript>while(true){};ETLForge.WriteFile('unavailable','data');</javascript>",true);
  int[] b=NativeJavaScript.inspect("\ud83d\ude00<javascript>SetScriptResult('hello');</javascript>").get(0);
  if(b[1]!=2)throw new AssertionError("UTF-16 offset mismatch");count++;
  System.out.println("Passed "+count+" Studio JavaScript checks");
 }
}
