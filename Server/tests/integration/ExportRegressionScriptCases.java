import java.nio.file.*;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
public final class ExportRegressionScriptCases {
 public static void main(String[]args)throws Exception{
  Map<String,Object> cases=new LinkedHashMap<>();cases.put("local",LocalVariablesRuntimeTest.cases);cases.put("javascript",MixedJavaScriptRuntimeTest.tests);
  new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(Paths.get("C:/ETL/Reports/regression-suite/script-cases.json").toFile(),cases);
  System.out.println("Exported native scripting regression cases");
 }
}
