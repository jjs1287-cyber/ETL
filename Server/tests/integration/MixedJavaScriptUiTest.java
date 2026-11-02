import java.util.*;
import org.etlforge.integration.data.script.*;
import org.etlforge.integration.data.entity.script.*;
import org.etlforge.integration.client.ui.script.partition.*;
import org.etlforge.integration.client.ui.script.builder.*;
import org.etlforge.ui.text.partition.Token;
public class MixedJavaScriptUiTest {
 static int count;
 static void check(boolean ok,String name){if(!ok)throw new AssertionError(name);count++;System.out.println("PASS "+name);}
 public static void main(String[] args)throws Exception{
  String source="before<javascript>var x='</javascript>';SetScriptResult(x);</javascript>middle<trans>x=1;x</trans>tail";
  ScriptTokenScanner scanner=new ScriptTokenScanner(source);scanner.setPosition(0);int begins=0,ends=0,keywords=0;
  for(Token token;(token=scanner.next())!=null;){if(token==Tokens.BEGIN_TRANS)begins++;if(token==Tokens.END_TRANS)ends++;if(token==Tokens.JAVASCRIPT_KEYWORD)keywords++;}
  check(begins==2&&ends==2,"mixed editor partitions preserve quoted delimiters");check(keywords==1,"JavaScript keyword highlighting");
  int position=source.indexOf("SetScriptResult");scanner.setPosition(position);check(scanner.next()==Tokens.FUNCTION,"editor partition resumes after quoted delimiter");
  check(JavaScriptFunctions.isInside(source,position),"completion language context uses parsed boundaries");
  check(!JavaScriptFunctions.isInside(source,source.indexOf("x=1")),"trans completion context remains separate");
  int functions=0;for(FunctionCategory category:JavaScriptFunctions.categories())for(Function function:category.getFunctions())functions++;
  check(functions==32,"31 reference functions plus mapping extension");
  check(JavaScriptFunctions.find("ETLForge.ReadFile").getDisplayName().contains("source"),"host signatures generated from policy");
  String refs="<javascript>$global=1;ETLForge.SetVar('etlforge.example',1);SetScriptResult(ETLForge.GetSource('id'));ETLForge.ReadFile('s.42470000-0000-4000-8000-000000000010');</javascript>";
  List<String> references=Arrays.asList(NativeJavaScript.references(refs));check(references.contains("G:$global")&&references.contains("G:$etlforge.example")&&references.contains("S:id"),"global and source dependency metadata");
  check(GrammarUtils.getAllIdsFromScript(new Transform(),refs,"fixture").size()==1,"JavaScript file connector deploy dependency");
  check(GrammarUtils.getAllIdsFromScript(new Transform(),"<javascript>var = ;</javascript>","fixture").isEmpty(),"incomplete scripts do not break dependency collection");
  final String id="s.42470000-0000-4000-8000-000000000010";
  GuidPathCache cache=new GuidPathCache(){public String getGuid(String path){return path.equals("Sources/input")?id:null;}public String getPathFromGuid(String value){return value.equals(id)?"Sources/input":null;}public void addGuidPathCacheListener(GuidPathCacheListener l){}public void removeGuidPathCacheListener(GuidPathCacheListener l){}};
  for(char quote:new char[]{'\'', '"'}){
   String script="<javascript>ETLForge.ReadFile("+quote+id+quote+");</javascript>";
   String display=GuidPathTranslator.translateToPath(script,cache);check(display.contains("<TAG>Sources/input</TAG>"),"project reference display "+quote);
   check(GuidPathTranslator.translateToGuid(display,cache,null).equals(script),"project reference round trip preserves quotes "+quote);
  }
  SourcePathTransferable transfer=new SourcePathTransferable("rows$row.name$");check(transfer.isDataFlavorSupported(SourcePathTransferable.FLAVOR)&&transfer.getTransferData(SourcePathTransferable.FLAVOR).equals("rows$row.name$"),"source drag exposes typed source-path payload");
  String regex="<javascript>var r=/[</javascript>]/;SetScriptResult(r.test('/'));</javascript>tail<trans>1</trans>";NativeJavaScript.inspect(regex);scanner=new ScriptTokenScanner(regex);begins=0;ends=0;for(Token token;(token=scanner.next())!=null;){if(token==Tokens.BEGIN_TRANS)begins++;if(token==Tokens.END_TRANS)ends++;}check(begins==2&&ends==2,"validated regex delimiters preserve editor partitions");
  ScriptValidator validator=new ScriptValidator();validator.setSourceDataElementContainer(new SourceDataElementContainer(){public boolean isFlat(){return true;}public boolean contains(String name){return name.equals("id");}});
  check(!validator.validate("<javascript>SetScriptResult(ETLForge.GetSource('id'));</javascript>").isInvalid(),"mapping source validator accepts known field");
  check(validator.validate("<javascript>SetScriptResult(ETLForge.GetSource('missing'));</javascript>").isInvalid(),"mapping source validator rejects unknown field");
  System.out.println("Passed "+count+" mixed-language UI/dependency checks");
 }
}
