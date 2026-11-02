import org.etlforge.integration.data.script.*;
public class LocalBoundaryCheck {
 public static void main(String[] args){String[] tests={"<trans>x=1</trans><trans>x</trans>","<trans>result=1;\nresult=notSet</trans>"};for(String s:tests){ScriptStatus status=new ScriptValidator().validate(s);System.out.println(status.getMessage());}}
}
