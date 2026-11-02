import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import javax.swing.*;
import org.etlforge.application.Application;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.main.IntegrationMain;
import org.etlforge.integration.client.runtime.ClientPlatform;
import org.etlforge.integration.client.ui.admin.cluster.*;
public final class ClusterStudioTest {
 static ClusterPanel panel;static int checks;
 interface Work{void run()throws Exception;}
 static void edt(Work w)throws Exception{Throwable[] e={null};SwingUtilities.invokeAndWait(()->{try{w.run();}catch(Throwable t){e[0]=t;}});if(e[0]!=null)throw new Exception(e[0]);}
 static void ok(boolean value,String text){if(!value)throw new AssertionError(text);checks++;System.out.println("PASS "+text);}
 static ClusterPanel find(Container root){for(Component c:root.getComponents()){if(c instanceof ClusterPanel)return (ClusterPanel)c;if(c instanceof Container){ClusterPanel p=find((Container)c);if(p!=null)return p;}}return null;}
 static Object field(String name)throws Exception{java.lang.reflect.Field f=ClusterPanel.class.getDeclaredField(name);f.setAccessible(true);return f.get(panel);}
 static void layout(Container c){c.doLayout();for(Component p:c.getComponents())if(p instanceof Container)layout((Container)p);}
 static void idle()throws Exception{for(int i=0;i<300;i++){boolean[] ready={false};edt(()->ready[0]=((JButton)field("refresh")).isEnabled());if(ready[0])return;Thread.sleep(100);}throw new AssertionError("Panel request timeout");}
 public static void main(String[] args){try{run();System.exit(0);}catch(Throwable e){e.printStackTrace(System.out);System.exit(1);}}
 static void run()throws Exception{
  Path report=Paths.get("C:/ETL/Reports/multi-server-ui");Files.createDirectories(report);Path fixture=Files.createTempDirectory(report,"studio-");
  IntegrationMain.main(new String[]{"-d",fixture.resolve("workspace").toString()});ClientPlatform.waitForInitialization(60,java.util.concurrent.TimeUnit.SECONDS);Thread.sleep(2000);edt(()->{});
  System.setOut(new java.io.PrintStream(report.resolve("results.txt").toFile(),"UTF-8"));int errors=ErrorLog.getNumberOfErrors();
  org.etlforge.integration.client.server.login.LoginService login=Application.getBean("platform.LoginService",org.etlforge.integration.client.server.login.LoginService.class);
  String password=new String(Files.readAllBytes(Paths.get("C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt")),StandardCharsets.UTF_8).trim();
  login.login(new org.etlforge.integration.client.server.login.LoginCredentials(org.etlforge.integration.client.server.ServerAddressFactory.usingPorts(47100,47101),"localhost:47100","Admin",password,false,false));password=null;
  ShowClusterAction action=Application.getBean("admin.action.Cluster",ShowClusterAction.class);ok(action!=null,"Administration action loads from installed Spring configuration");edt(()->action.execute(null));
  for(int i=0;i<100&&panel==null;i++){edt(()->{org.etlforge.application.ui.window.editor.Editor e=Application.getBean("ui.AppWin",org.etlforge.application.ui.window.ApplicationWindow.class).getEditorService().getActiveEditor();if(e instanceof org.etlforge.application.ui.window.editor.GenericEditor){JComponent root=((org.etlforge.application.ui.window.editor.GenericEditor)e).getMainPanel();panel=root instanceof ClusterPanel?(ClusterPanel)root:find(root);e.render(new Dimension(1150,800));}});Thread.sleep(100);}ok(panel!=null,"Multi-server Configuration opens in Administration");idle();
  edt(()->{
   JTable table=(JTable)field("table");ok(table.getRowCount()>=1,"native registry populates node table");ok(((JLabel)field("mode")).getText().contains("not yet certified"),"screen reports active-active readiness accurately");ok(!((JLabel)field("clusterId")).getText().equals("Not loaded"),"cluster ID displays");ok(((JButton)field("save")).isEnabled(),"connected administrator can save configuration");table.setRowSelectionInterval(0,0);ok(((JTextField)field("nodeName")).getText().length()>0,"node selection populates editor");ok(((JButton)field("saveNode")).isEnabled(),"selected node address can be saved");((JButton)field("save")).doClick();
  });idle();edt(()->{ok(!((JLabel)field("status")).getText().startsWith("Multi-server:"),"Save Configuration completes through native API");((JButton)field("saveNode")).doClick();});idle();
  edt(()->{
   ok(!((JLabel)field("status")).getText().startsWith("Multi-server:"),"Save Node Address completes through native API");
   JFrame preview=new JFrame("ETLForge multi-server verification");preview.setContentPane(panel);preview.setSize(1150,800);preview.setVisible(true);layout(panel);BufferedImage image=new BufferedImage(panel.getWidth(),panel.getHeight(),BufferedImage.TYPE_INT_RGB);panel.paint(image.createGraphics());javax.imageio.ImageIO.write(image,"png",report.resolve("configuration.png").toFile());preview.dispose();
   panel.disconnect();ok(!((JButton)field("save")).isEnabled()&&!((JButton)field("saveNode")).isEnabled(),"disconnect disables server mutations");
  });ok(ErrorLog.getNumberOfErrors()==errors,"no new Studio errors");System.out.println("Passed "+checks+" installed Studio multi-server checks");edt(()->{for(Window w:Window.getWindows())w.dispose();});
 }
}
