import java.nio.file.*;
import java.util.*;
import org.etlforge.integration.data.entity.*;
import org.etlforge.integration.data.project.*;
import org.etlforge.integration.client.project.compare.ProjectComparison;
import org.etlforge.util.persist.XmlPersistor;
public final class ProjectComparisonTest {
 static int checks;static void check(boolean ok,String name){if(!ok)throw new AssertionError(name);System.out.println("PASS "+name);checks++;}
 static <T extends IntegrationEntity>T add(IntegrationProject p,T e){p.insertEntityUnderRoot(e);return e;}
 public static void main(String[] args)throws Exception{
  IntegrationProjectLock.acquire();try{
  IntegrationProject a=IntegrationProject.createNewProject("Left"),b=IntegrationProject.createNewProject("Right");
  Script x=add(a,new Script("Script")),y=add(b,new Script("Script"));x.setScript("<trans>\n$g='caf\u00e9 \u4e2d\u6587';\n</trans>");y.setScript(x.getScript());
  check(ProjectComparison.compare(a,b).isEmpty(),"different IDs and project names ignored");
  y.setDeployDirty(true);y.setDeployed(true);check(ProjectComparison.compare(a,b).isEmpty(),"deployment flags ignored");
  y.setScript("<trans>$g='changed';</trans>");check(ProjectComparison.compare(a,b).size()==1,"changed script detected");
  check(ProjectComparison.compare(a,b).get(0).left.toString().contains("caf\u00e9"),"Unicode text retained");
  y.setScript(x.getScript());y.setDescription("new description");check(ProjectComparison.compare(a,b).size()==1,"description change detected");y.setDescription(x.getDescription());
  Script extra=add(a,new Script("LeftOnly"));check(ProjectComparison.compare(a,b).get(0).change.equals("Left only"),"left-only object");
  add(b,new Script("RightOnly"));check(ProjectComparison.compare(a,b).size()==2,"right-only object");
  ProjectVariable v=add(a,new ProjectVariable("Secret")),w=add(b,new ProjectVariable("Secret"));v.setPassword(true);w.setPassword(true);v.setDefaultValue("not-for-display");w.setDefaultValue("different-secret");
  ProjectComparison.Difference secret=null;for(ProjectComparison.Difference d:ProjectComparison.compare(a,b))if(d.object.equals("Secret"))secret=d;
  check(secret!=null,"secret change detected");check(!secret.left.toString().contains("not-for-display")&&!secret.right.toString().contains("different-secret"),"secret values redacted");
  w.setDefaultValue(v.getDefaultValue());check(ProjectComparison.compare(a,b).size()==2,"same protected value matches");
  Source s=add(a,new Source("Input")),t=add(b,new Source("Input"));org.etlforge.integration.data.location.LocalFileLocation l=new org.etlforge.integration.data.location.LocalFileLocation(),r=new org.etlforge.integration.data.location.LocalFileLocation();l.setFolderPath("C:/Input");r.setFolderPath("C:/Input");s.setLocation(l);t.setLocation(r);check(ProjectComparison.compare(a,b).size()==2,"same endpoint settings match");r.setFolderPath("C:/Changed");check(ProjectComparison.compare(a,b).size()==3,"endpoint change detected");
  XmlPersistor p=XmlPersistor.newRoot("Object");s.writeTo(p);Files.write(Paths.get("C:/ETL/Reports/comparison-source.xml"),p.toXml().getBytes(java.nio.charset.StandardCharsets.UTF_8));
  System.out.println("PASSED "+checks);
  }finally{IntegrationProjectLock.release();}
 }
}



