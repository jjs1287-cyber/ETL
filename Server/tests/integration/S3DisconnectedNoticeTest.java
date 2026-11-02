package org.etlforge.integration.client.ui.interchange.locatable.actions;

import java.awt.*;
import java.lang.reflect.Proxy;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.*;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.server.*;
import org.etlforge.integration.client.server.webservice.*;
import org.etlforge.integration.client.ui.admin.audit.AuditLogService;
import org.etlforge.integration.data.entity.*;
import org.etlforge.integration.data.test.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class S3DisconnectedNoticeTest {
 static int checks;
 static void check(boolean ok,String label){if(!ok)throw new AssertionError(label);++checks;System.out.println("PASS "+label);}
 static ServerConnectionService connection(ConnectionStatus status){return (ServerConnectionService)Proxy.newProxyInstance(S3DisconnectedNoticeTest.class.getClassLoader(),new Class[]{ServerConnectionService.class},(p,m,a)->m.getName().equals("getConnectionStatus")?status:null);}
 static String text(Container root){StringBuilder out=new StringBuilder();for(Component c:root.getComponents()){if(c instanceof JLabel)out.append(((JLabel)c).getText());if(c instanceof AbstractButton)out.append(((AbstractButton)c).getText());if(c instanceof javax.swing.text.JTextComponent)out.append(((javax.swing.text.JTextComponent)c).getText());if(c instanceof Container)out.append(text((Container)c));}return out.toString();}
 static void dialog(IntegrationEntity entity)throws Exception{
  int before=ErrorLog.getNumberOfErrors();CountDownLatch done=new CountDownLatch(1);AtomicReference<Throwable> failure=new AtomicReference<>();
  SwingUtilities.invokeAndWait(()->{
   JFrame owner=new JFrame("ETLForge test owner");owner.setSize(300,100);
   javax.swing.Timer timer=new javax.swing.Timer(100,null);int[] ticks={0};
   timer.addActionListener(event->{
    for(Window window:Window.getWindows())if(window instanceof JDialog&&window.isVisible()&&((JDialog)window).getTitle().equals("Test Connection")){
     try{String content=text((JDialog)window);check(content.contains(ServerConnectionService.NOT_CONNECTED_MESSAGE),entity.getClass().getSimpleName()+" shows plain disconnected message");check(!content.toLowerCase().contains("audit"),"no audit wording");check(!content.toLowerCase().contains("stack trace")&&!content.toLowerCase().contains("details"),"no stack trace or Details button");check(ErrorLog.getNumberOfErrors()==before,"expected disconnected state adds no error-log entry");
      if(entity instanceof Source){java.awt.image.BufferedImage image=new java.awt.image.BufferedImage(window.getWidth(),window.getHeight(),java.awt.image.BufferedImage.TYPE_INT_RGB);Graphics2D graphics=image.createGraphics();window.paint(graphics);graphics.dispose();javax.imageio.ImageIO.write(image,"png",new java.io.File("C:/ETL/Reports/s3-not-connected.png"));}
     }catch(Throwable e){failure.set(e);}finally{timer.stop();window.dispose();owner.dispose();done.countDown();}return;
    }
    if(++ticks[0]>100){failure.set(new AssertionError("Disconnected notice did not appear"));timer.stop();owner.dispose();done.countDown();}
   });timer.start();
   TestConnectionResult result=TestConnectionResult.error(entity,new TestResultException(new IllegalStateException(ServerConnectionService.NOT_CONNECTED_MESSAGE)));
   new TestConnectionResultDisplayer(owner).showResult(entity,result);
  });
  if(!done.await(15,TimeUnit.SECONDS))throw new AssertionError("Dialog test timed out");if(failure.get()!=null)throw new AssertionError(failure.get());
 }
 public static void main(String[] args)throws Exception{
  final int[] notices={0};check(!TestConnectionAction.checkConnected(connection(ConnectionStatus.DISCONNECTED),()->notices[0]++),"disconnected click does not start a test");check(notices[0]==1,"one simple notice requested");check(TestConnectionAction.checkConnected(connection(ConnectionStatus.CONNECTED),()->notices[0]++),"connected click can proceed");check(notices[0]==1,"connected click shows no disconnected notice");
  ServerPorts ports=new ServerPorts(){public int getDefaultPort(){return 47100;}public int getDefaultHttpsPort(){return 46909;}};
  ServerManager manager=new ServerManager(ServerInformationStore.EMPTY,ports,null);AuditLogService service=new AuditLogService(new CppWebServiceConfiguration(manager,ports,result->false));
  for(String host:new String[]{"","127.0.0.1:1"}){manager.getLoginService().getConfiguration().setServerName(host);boolean denied=false;try{service.request(new ObjectMapper().createObjectNode().put("action","s3.test"));}catch(IllegalStateException expected){denied=true;check(expected.getMessage().equals(ServerConnectionService.NOT_CONNECTED_MESSAGE),"shared helper uses feature-neutral wording");}check(denied,"disconnected helper rejects empty or stale saved host before network access");}
  dialog(new Source("S3 source"));dialog(new Target("S3 target"));System.out.println(checks+" disconnected-state checks passed");
 }
}
