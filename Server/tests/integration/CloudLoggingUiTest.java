package org.etlforge.integration.client.ui.admin.cloud;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import javax.imageio.ImageIO;
import javax.swing.*;
import org.etlforge.application.ui.look.LookAndFeelCustomizer;
public final class CloudLoggingUiTest {
 static int checks;static void require(boolean ok){if(!ok)throw new AssertionError("Cloud logging UI check failed");checks++;}
 static <T> java.util.List<T> find(Container parent,Class<T> type){java.util.List<T> result=new ArrayList<>();for(Component child:parent.getComponents()){if(type.isInstance(child))result.add(type.cast(child));if(child instanceof Container)result.addAll(find((Container)child,type));}return result;}
 public static void main(String[] args)throws Exception {
  LookAndFeelCustomizer.customize();ObjectMapper json=new ObjectMapper();AtomicInteger calls=new AtomicInteger();CloudLoggingPanel[] panel={null};
  SwingUtilities.invokeAndWait(()->{panel[0]=new CloudLoggingPanel(request->{calls.incrementAndGet();ObjectNode response=json.createObjectNode();response.put("revision",3);ObjectNode settings=response.putObject("settings");settings.put("enabled",false);settings.put("type","sftp");settings.put("port",22);settings.put("environment","dev");settings.put("host","127.0.0.1");settings.put("path","/CloudLogging");response.putObject("counts").put("1",12).put("2",2);return response;});panel[0].start();});
  for(int i=0;i<40;i++){Thread.sleep(50);SwingUtilities.invokeAndWait(()->{});if(calls.get()>0)break;}Thread.sleep(100);
  SwingUtilities.invokeAndWait(()->{
   CloudLoggingPanel p=panel[0];require(find(p,JPasswordField.class).size()==2);require(find(p,JComboBox.class).size()==1);
   JComboBox<?> type=find(p,JComboBox.class).get(0);require(type.getItemCount()==3);
   java.util.Map<String,JTextField> fields=new HashMap<>();for(JTextField f:find(p,JTextField.class))if(f.getName()!=null)fields.put(f.getName(),f);
   require(fields.get("host").isVisible());require(!fields.get("url").isVisible());require(fields.get("port").getText().equals("22"));
   type.setSelectedIndex(1);require(fields.get("url").isVisible());require(!fields.get("path").isVisible());require(!fields.get("hostKeySha256").isVisible());
   type.setSelectedIndex(2);require(fields.get("path").isVisible());require(!fields.get("url").isVisible());require(fields.get("login").isVisible());
   p.setSize(1050,660);layout(p);BufferedImage image=new BufferedImage(1050,660,BufferedImage.TYPE_INT_RGB);Graphics2D graphics=image.createGraphics();p.paint(graphics);graphics.dispose();try{ImageIO.write(image,"png",Paths.get("C:/ETL/Reports/cloud-logging-ui.png").toFile());}catch(Exception e){throw new RuntimeException(e);}
   p.disconnect();for(JButton button:find(p,JButton.class))if(button.getText().equals("Save Settings"))require(!button.isEnabled());
  });System.out.println("PASS: "+checks+" cloud logging UI checks");
 }
 static void layout(Container parent){parent.doLayout();for(Component child:parent.getComponents())if(child instanceof Container)layout((Container)child);}
}
