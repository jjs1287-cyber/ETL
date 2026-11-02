import java.nio.file.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import org.etlforge.application.Application;
import org.etlforge.integration.client.main.IntegrationMain;
import org.etlforge.integration.client.runtime.ClientPlatform;
import org.etlforge.integration.client.project.*;
import org.etlforge.integration.client.project.compare.ProjectComparison;
import org.etlforge.integration.client.ui.admin.compare.*;
import org.etlforge.integration.data.entity.*;
import org.etlforge.integration.data.project.*;
public final class ProjectComparisonStudioTest {
 static int checks;static ProjectComparisonPanel livePanel;static File leftFile,rightFile;static void check(boolean b,String s){if(!b)throw new AssertionError(s);System.out.println("PASS "+s);checks++;}
 static <T extends IntegrationEntity>T add(ManagedProject p,T e){p.getProject().insertEntityUnderRoot(e);return e;}
 public static void main(String[] args){try{run();System.exit(0);}catch(Throwable t){t.printStackTrace(System.out);System.exit(1);}}
 static void run()throws Exception{
 Path report=Paths.get("C:/ETL/Reports/pgp-compare");Files.createDirectories(report);Path root=Files.createTempDirectory(report,"studio-");IntegrationMain.main(new String[]{"-d",root.resolve("workspace").toString()});ClientPlatform.waitForInitialization(60,java.util.concurrent.TimeUnit.SECONDS);Thread.sleep(2500);SwingUtilities.invokeAndWait(()->{});
 System.setOut(new PrintStream(report.resolve("studio-results.txt").toFile(),"UTF-8"));ProjectManager manager=Application.getBeansOfType(ProjectManager.class).get(0);IntegrationProjectLock.acquire();try{
 ManagedProject a=manager.getProject(manager.createNewProject("CompareLeft",root.resolve("Left").toFile())),b=manager.getProject(manager.createNewProject("CompareRight",root.resolve("Right").toFile()));Script x=add(a,new Script("Example")),y=add(b,new Script("Example"));x.setScript("<trans>'same';</trans>");y.setScript(x.getScript());
 check(ProjectComparison.compare(a,b).isEmpty(),"managed equivalent projects match");a.getEntityResources().getFileResource(x,"GraphLayout.xml").write("<layout x='1'/>");b.getEntityResources().getFileResource(y,"GraphLayout.xml").write("<layout x='999'/>");check(ProjectComparison.compare(a,b).isEmpty(),"object positioning resources ignored");
 a.getEntityResources().getFileResource(x,"example.xml").write("<data>left</data>");b.getEntityResources().getFileResource(y,"example.xml").write("<data>right</data>");check(ProjectComparison.compare(a,b).size()==1,"resource content changes detected");
 Path file=ProjectFolders.getFileStoreRootFolder(a.getLocation()).toPath().resolve("sample.xsd");Files.createDirectories(file.getParent());Files.write(file,"<schema/>".getBytes(java.nio.charset.StandardCharsets.UTF_8));check(ProjectComparison.compare(a,b).size()==2,"project assets compared");a.saveToDisk();b.saveToDisk();
 check(!Application.getBeansOfType(ShowProjectComparisonAction.class).isEmpty(),"administration comparison action wired");
 final ProjectComparisonPanel[] panel={null};SwingUtilities.invokeAndWait(()->{panel[0]=new ProjectComparisonPanel(manager);panel[0].setSize(1050,700);layout(panel[0]);java.awt.image.BufferedImage image=new java.awt.image.BufferedImage(1050,700,java.awt.image.BufferedImage.TYPE_INT_RGB);panel[0].paint(image.getGraphics());try{javax.imageio.ImageIO.write(image,"png",report.resolve("project-differences.png").toFile());}catch(Exception e){throw new RuntimeException(e);}});check(panel[0].getComponentCount()==3,"comparison screen loads and renders");
 org.etlforge.integration.client.ui.interchange.location.panel.EndpointPgpPanel sourcePanel=new org.etlforge.integration.client.ui.interchange.location.panel.EndpointPgpPanel(org.etlforge.integration.data.SourceTarget.Source),targetPanel=new org.etlforge.integration.client.ui.interchange.location.panel.EndpointPgpPanel(org.etlforge.integration.data.SourceTarget.Target);org.etlforge.integration.data.location.LocalFileLocation loc=new org.etlforge.integration.data.location.LocalFileLocation();loc.setProperty("pgp_enabled","true");loc.setProperty("pgp_key_id","00000000-0000-0000-0000-000000000001");loc.setProperty("pgp_armor","true");sourcePanel.reset(loc);targetPanel.reset(loc);org.etlforge.integration.data.location.LocalFileLocation saved=new org.etlforge.integration.data.location.LocalFileLocation();targetPanel.apply(saved);check(saved.getBooleanProperty("pgp_enabled",false)&&saved.getBooleanProperty("pgp_armor",false),"endpoint PGP settings round trip");check(saved.getProperty("pgp_key_id").equals(loc.getProperty("pgp_key_id")),"endpoint key reference persists");
 leftFile=a.getProjectFile().getFile();rightFile=b.getProjectFile().getFile();livePanel=panel[0];manager.closeProject(a);manager.closeProject(b);
 }finally{IntegrationProjectLock.release();}
 SwingUtilities.invokeAndWait(()->{try{((JTextField)field("left")).setText(leftFile.getAbsolutePath());((JTextField)field("right")).setText(rightFile.getAbsolutePath());((JButton)field("compare")).doClick();}catch(Exception e){throw new RuntimeException(e);}});
 boolean finished=false;for(int n=0;n<300;n++){final boolean[] done={false};SwingUtilities.invokeAndWait(()->{try{done[0]=((JButton)field("compare")).isEnabled();}catch(Exception e){throw new RuntimeException(e);}});if(done[0]){finished=true;break;}Thread.sleep(100);}check(finished,"asynchronous Compare button completes");
 SwingUtilities.invokeAndWait(()->{try{javax.swing.table.DefaultTableModel model=(javax.swing.table.DefaultTableModel)field("rows");check(model.getRowCount()==2,"Compare button displays real project differences");JTable table=(JTable)field("table");table.setRowSelectionInterval(0,0);check(((javax.swing.table.DefaultTableModel)field("fields")).getRowCount()>0,"selecting an object displays changed fields");JFrame frame=new JFrame("ETLForge Project Differences verification");frame.setContentPane(livePanel);frame.setSize(1100,760);frame.setVisible(true);layout(livePanel);java.awt.image.BufferedImage image=new java.awt.image.BufferedImage(livePanel.getWidth(),livePanel.getHeight(),java.awt.image.BufferedImage.TYPE_INT_RGB);livePanel.paint(image.getGraphics());javax.imageio.ImageIO.write(image,"png",Paths.get("C:/ETL/Reports/pgp-compare/project-differences-loaded.png").toFile());frame.dispose();}catch(Exception e){throw new RuntimeException(e);}});
 System.out.println("PASSED "+checks);for(java.awt.Window w:java.awt.Window.getWindows())w.dispose();
 }
 static Object field(String name)throws Exception{java.lang.reflect.Field f=ProjectComparisonPanel.class.getDeclaredField(name);f.setAccessible(true);return f.get(livePanel);}
 static void layout(java.awt.Container c){c.doLayout();for(java.awt.Component p:c.getComponents())if(p instanceof java.awt.Container)layout((java.awt.Container)p);}
}


