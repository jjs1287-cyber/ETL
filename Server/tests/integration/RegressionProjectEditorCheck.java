import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import javax.swing.SwingUtilities;
import org.etlforge.application.Application;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.main.IntegrationMain;
import org.etlforge.integration.client.runtime.ClientPlatform;
import org.etlforge.integration.client.project.*;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfig;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.data.entity.*;
import org.etlforge.integration.data.project.*;
public final class RegressionProjectEditorCheck {
 public static void main(String[]args){try{run(args.length>0);System.exit(0);}catch(Throwable e){try{e.printStackTrace(new PrintStream("C:/ETL/Reports/regression-suite/editor-check-error.txt","UTF-8"));}catch(Exception ignored){}System.exit(1);}}
 static void run(boolean installed)throws Exception{
  Path root=Paths.get("C:/ETL/Reports/regression-suite");IntegrationMain.main(new String[]{"-d",root.resolve("editor-workspace").toString()});ClientPlatform.waitForInitialization(60,TimeUnit.SECONDS);Thread.sleep(3000);SwingUtilities.invokeAndWait(()->{});System.setOut(new PrintStream(root.resolve("editor-check.txt").toFile(),"UTF-8"));System.setErr(System.out);
  ProjectManager manager=Application.getBeansOfType(ProjectManager.class).get(0);IntegrationProjectLock.acquire();IntegrationProject project;
  try{project=manager.loadProject("Test Operations",(installed?Paths.get("C:/ETL/Studio/run-user-home-etlforge/application/projects/Test Operations"):root.resolve("Test Operations")).toFile(),ProjectLoadingIssuesHandler.IGNORE);}finally{IntegrationProjectLock.release();}
  SwingUtilities.invokeAndWait(()->manager.setCurrentProject(project));Map<EntityType,EntityConfig> configs=new HashMap<>();for(EntityConfig c:Application.getBeansOfType(EntityConfig.class))configs.put(c.getType(),c);
  List<IntegrationEntity> selected=new ArrayList<>();for(IntegrationEntity e:project.getAllEntities())if(e.getName().equals("00 - Run All Regression Tests")||e.getName().equals("00 - Run All Regression Tests script"))selected.add(e);
  for(EntityType type:new EntityType[]{EntityType.Source,EntityType.Target,EntityType.Transformation,EntityType.XsltTransform,EntityType.ProjectVariable,EntityType.WebServiceCall})for(IntegrationEntity e:project.getAllEntities())if(e.getEntityType()==type&&!(e instanceof Folder)&&!(e instanceof BuiltInIntegrationEntity)){selected.add(e);break;}
  ApplicationWindow window=Application.getBean("ui.AppWin",ApplicationWindow.class);int before=ErrorLog.getNumberOfErrors();
  for(IntegrationEntity e:selected){final IntegrationEntityPage[] page={null};SwingUtilities.invokeAndWait(()->{page[0]=configs.get(e.getEntityType()).getPageFactory().createPage(e);window.getEditorService().openEditor(page[0],"Design");});Thread.sleep(400);SwingUtilities.invokeAndWait(()->{page[0].setClean();window.getEditorService().closeEditor(page[0]);});if(ErrorLog.getNumberOfErrors()>before)throw new AssertionError("Editor exception opening "+e.getEntityType()+": "+ErrorLog.lastError().getReport());System.out.println("PASS persisted project editor "+e.getEntityType());}
  System.out.println("PASS "+selected.size()+" persisted project editors; no new application errors");
 }
}


