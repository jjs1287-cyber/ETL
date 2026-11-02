import java.net.URL;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.*;
public class LocalVariablesRuntimeTest {
 static TreeMapperInfoProvider api; static String hash;
 static String[][] cases={
 {"assignment","x='hello script';x","hello script"},
 {"reassign","x=1;x=2;x","2"},
 {"global","$x='global';x='local';$x+':'+x","global:local"},
 {"unassigned","x","ERROR"},
 {"later","x;x=1","ERROR"},
 {"self","x=x+1","ERROR"},
 {"rhs","x=1+x","ERROR"},
 {"empty","x='';Length(x)","0"},
 {"null","x=Null();IsNull(x)","1"},
 {"zero","x=0;x","0"},
 {"false","x=false;If(x,'bad','ok')","ok"},
 {"unicode","x='Gr\u00fc\u00dfe \u4e16\u754c \ud83d\ude00';x","Gr\u00fc\u00dfe \u4e16\u754c \ud83d\ude00"},
 {"compound","x=1;x+=2;x-=1;x","2"},
 {"compoundUnset","x+=1","ERROR"},
 {"postfix","x=1;y=x++;y+':'+x","1:2"},
 {"prefix","x=1;++x;x","2"},
 {"incrementUnset","x++","ERROR"},
 {"chain","x=y=4;x+y","8"},
 {"array","x={1,2};x[0]=3;x[0]","3"},
 {"arrayUnset","x[0]=3","ERROR"},
 {"map","x=Map();x['a']='ok';x['a']","ok"},
 {"skippedBranch","If(false,x=1);x","ERROR"},
 {"takenBranch","If(true,x=1);x","1"},
 {"zeroLoop","While(false,x=1);x","ERROR"},
 {"case","x=1;X","ERROR"},
 {"comment","x=1;/* undefined */x","1"},
 {"argumentNull","IsNull(_1)","0"},
 {"argumentList","ArgumentList(first);IsNull(first)","0"},
 {"repeatScope","If(false,x=2);x","ERROR"}
 };
 static String run(String script)throws Exception{
  DebuggingParameters params=new DebuggingParameters();params.setCommand("Run");params.setDebuggingSessionId(UUID.randomUUID().toString());params.setBreakpoints(new Breakpoint[0]);params.setGlobalVariables(new WsDataElement[0]);
  try {
   WsTestResult result=api.testExpression("Admin",hash,"",Base64.getEncoder().encodeToString(("<trans>"+script+"</trans>").getBytes(StandardCharsets.UTF_8)),params);
   if(result.getErrors()!=null&&!result.getErrors().isEmpty())return "ERROR:"+result.getErrors().replace('\n',' ');
   return new String(Base64.getMimeDecoder().decode(result.getResult()),StandardCharsets.UTF_8);
  }catch(Exception e){return "ERROR:"+e.getMessage();}
 }
 public static void init()throws Exception{
  String password=new String(Files.readAllBytes(Paths.get("C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt")),StandardCharsets.UTF_8).trim();
  StringBuilder digest=new StringBuilder();for(byte b:MessageDigest.getInstance("MD5").digest(password.getBytes(StandardCharsets.UTF_8)))digest.append(String.format("%02x",b&255));hash=digest.toString();
  api=new TreeMapperInfoProviderServiceLocator().getkonga_treemapper_infoprovider(new URL("http://127.0.0.1:47100/axis/konga_treemapper_infoprovider"));((org.apache.axis.client.Stub)api).setTimeout(30000);
 }
 public static void main(String[] args)throws Exception{
  init(); int failures=0;
  for(String[] test:cases){String actual=run(test[1]);boolean ok=test[2].equals("ERROR")?actual.startsWith("ERROR:")&&actual.toLowerCase().contains("assign"):actual.equals(test[2]);if(!ok)failures++;System.out.println((ok?"PASS ":"FAIL ")+test[0]+" => "+actual);}
  if(args.length==0 && failures>0)throw new AssertionError(failures+" local-variable checks failed");
  System.out.println("Checked "+cases.length+" cases; failures="+failures);
 }
}
