import org.etlforge.integration.data.script.*;
public class LocalVariablesValidatorTest {
 public static void main(String[] args){int checked=0;for(String[] row:LocalVariablesRuntimeTest.cases){boolean expected=!row[2].equals("ERROR")||row[0].equals("skippedBranch")||row[0].equals("zeroLoop")||row[0].equals("repeatScope");ScriptStatus status=new ScriptValidator().validate("<trans>"+row[1]+"</trans>");if(expected==status.isInvalid())throw new AssertionError(row[0]+": "+status.getMessage());checked++;}ScriptValidator flat = new ScriptValidator();
 flat.setSourceDataElementContainer(new SourceDataElementContainer(){public boolean isFlat(){return true;}public boolean contains(String name){return name.equals("CustomerName");}});
 if(flat.validate("<trans>CustomerName</trans>").isInvalid())throw new AssertionError("Source field misclassified as local"); checked++;
 if(new ScriptValidator().validate("<trans>x=1</trans><trans>x</trans>").isInvalid())throw new AssertionError("Template block boundary lost assignment"); checked++;
 ScriptStatus line=new ScriptValidator().validate("<trans>x=1;\nmissing</trans>");
 if(!line.isInvalid()||!line.getMessage().contains("line 2"))throw new AssertionError("Missing local diagnostic location"); checked++;
 System.out.println("PASS "+checked+" Studio validation checks");}
}
