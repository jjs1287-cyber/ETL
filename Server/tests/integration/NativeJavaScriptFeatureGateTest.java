import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.etlforge.integration.server.implementation.webservice.interchange.jobcontrol.client.*;
public final class NativeJavaScriptFeatureGateTest {
 public static void main(String[] args)throws Exception{
  LocalVariablesRuntimeTest.init();JobControl jobs=new JobControlServiceLocator().getkonga_jobcontrol(new URL("http://127.0.0.1:47100/axis/konga_jobcontrol"));
  long instance=jobs.runOperationInstanceMD5Password("Admin",LocalVariablesRuntimeTest.hash,NativeJavaScriptMappingTest.guid(51)).getOperationInstanceId();
  boolean failed=false;for(int i=0;i<100;i++){String status=NativeJavaScriptMappingTest.status(instance);if(status.equals("6")){failed=true;break;}if(status.equals("3")||status.equals("4")||status.equals("5"))throw new AssertionError("Disabled JavaScript succeeded");Thread.sleep(100);}
  if(!failed)throw new AssertionError("Disabled JavaScript did not finish with failure");
  ProcessBuilder builder=new ProcessBuilder("C:/ETL/Server/runtime/postgresql-18/pgsql/bin/psql.exe","-X","-w","-A","-t","-h","127.0.0.1","-p","5432","-U","etlforge_dev","-d","etlforge_dev_tran","-c","SELECT count(*) FROM operationlogview WHERE operation_instance_id="+instance+" AND message ILIKE '%disabled%'");builder.environment().put("PGPASSFILE","C:/ETL/Server/private/pgbouncer-dev.pgpass");Process process=builder.start();java.io.ByteArrayOutputStream bytes=new java.io.ByteArrayOutputStream();byte[] buffer=new byte[1024];for(int n;(n=process.getInputStream().read(buffer))>=0;)bytes.write(buffer,0,n);if(process.waitFor()!=0||Integer.parseInt(new String(bytes.toByteArray(),StandardCharsets.UTF_8).trim())<1)throw new AssertionError("Missing explicit disabled-feature message");
  System.out.println("PASS disabled feature reports failed status and explicit error; instance="+instance);
 }
}
