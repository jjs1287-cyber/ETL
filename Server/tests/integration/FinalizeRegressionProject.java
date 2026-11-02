import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import javax.swing.SwingUtilities;
import com.fasterxml.jackson.databind.*;
import org.etlforge.application.Application;
import org.etlforge.integration.client.main.IntegrationMain;
import org.etlforge.integration.client.runtime.ClientPlatform;
import org.etlforge.integration.client.project.*;
import org.etlforge.integration.data.project.*;
import org.etlforge.integration.data.entity.*;
public final class FinalizeRegressionProject {
 public static void main(String[]args){try{run(args.length>0&&args[0].equals("installed"));Files.deleteIfExists(Paths.get("C:/ETL/Reports/regression-suite/project-load-error.txt"));System.exit(0);}catch(Throwable e){try{e.printStackTrace(new PrintStream("C:/ETL/Reports/regression-suite/project-load-error.txt","UTF-8"));}catch(Exception ignored){}System.exit(1);}}
 static void run(boolean installed)throws Exception{
  Path root=Paths.get("C:/ETL/Reports/regression-suite");IntegrationMain.main(new String[]{"-d",root.resolve("finalize-workspace").toString()});ClientPlatform.waitForInitialization(60,TimeUnit.SECONDS);Thread.sleep(3000);SwingUtilities.invokeAndWait(()->{});
  System.setOut(new PrintStream(root.resolve("project-loading.txt").toFile(),"UTF-8"));System.setErr(System.out);
  ProjectManager manager=Application.getBeansOfType(ProjectManager.class).get(0);IntegrationProjectLock.acquire();
  try{
   IntegrationProject project=manager.loadProject("Test Operations",root.resolve("Test Operations").toFile(),ProjectLoadingIssuesHandler.IGNORE);ManagedProject managed=manager.getProject(project);
   JsonNode groups=new ObjectMapper().readTree(Files.readAllBytes(root.resolve("groups.json")));Map<String,Folder> folders=new HashMap<>();int count=0;
   Set<String> deployedIds=new HashSet<>();if(installed){JsonNode tables=new ObjectMapper().readTree(Files.readAllBytes(root.resolve("deploy.json"))).path("tables");for(String table:new String[]{"SourcesTab","TargetsTab","ScriptsTab","OperationsTab"})for(JsonNode row:tables.path(table).path("rows"))deployedIds.add(row.get(0).asText());for(JsonNode row:tables.path("EntityProperties").path("rows"))deployedIds.add(row.get(1).asText());}
   for(IntegrationEntity e:new ArrayList<>(project.getAllEntities())){String group=groups.path(e.getID().toString()).asText("");if(group.isEmpty()||e instanceof Folder||e instanceof BuiltInIntegrationEntity)continue;String key=e.getEntityType()+"/"+group;Folder folder=folders.get(key);if(folder==null){folder=project.getRootFolder(e.getEntityType()).findSubFolder(group);if(folder==null){folder=new Folder(group,e.getEntityType());project.insertEntity(folder,project.getRootFolder(e.getEntityType()));}folders.put(key,folder);}boolean dirty=e.isDeployDirty(),deployed=e.hasBeenDeployed();e.setParent(folder);e.setDeployDirty(dirty);e.setDeployed(deployed);count++;}
   for(IntegrationEntity e:project.getAllEntities())if(deployedIds.contains(e.getID().toString())){e.setDeployed(true);e.setDeployDirty(false);}
   project.refreshItemLookup();managed.rebuildDependencies();managed.saveToDisk();System.out.println("PASS loaded and grouped "+count+" imported entities; "+project.getAllEntitiesOfClass(Operation.class).size()+" operations total");
   int missing=0;for(Operation op:project.getAllEntitiesOfClass(Operation.class))for(org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity a:op.getPipeline().getActivities())if(a instanceof org.etlforge.integration.data.entity.operation.pipeline.EntityActivity){Object content=((org.etlforge.integration.data.entity.operation.pipeline.EntityActivity)a).getContent();if(content==null||!a.getType().accepts(content)){missing++;System.out.println("FAIL unresolved or incorrectly typed pipeline "+op.getName());}}
   if(missing>0)throw new AssertionError(missing+" unresolved pipeline references");System.out.println("PASS every persisted pipeline reference resolves");
  }finally{IntegrationProjectLock.release();}
 }
}




