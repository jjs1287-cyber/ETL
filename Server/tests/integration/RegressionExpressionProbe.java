import java.nio.file.*;
import com.fasterxml.jackson.databind.*;
public final class RegressionExpressionProbe {
 public static void main(String[]args)throws Exception{
  LocalVariablesRuntimeTest.init();JsonNode config=new ObjectMapper().readTree(Files.readAllBytes(Paths.get("C:/ETL/Reports/regression-suite/deploy.json")));
  for(JsonNode c:config.path("cases"))if(c.path("number").asInt()==231)System.out.println(LocalVariablesRuntimeTest.run("ok=RunOperation('op."+c.path("fixture").asText()+"',true);String(ok)+':'+GetLastError()"));
 }
}
