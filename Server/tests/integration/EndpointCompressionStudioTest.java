import java.awt.*;
import javax.swing.*;
import java.util.*;
import org.etlforge.integration.data.*;
import org.etlforge.integration.data.location.*;
import org.etlforge.integration.client.ui.interchange.location.panel.*;
import org.etlforge.util.persist.XmlPersistor;
public class EndpointCompressionStudioTest {
 static int checks; static void check(boolean ok,String label){if(!ok)throw new AssertionError(label);checks++;System.out.println("PASS "+label);}
 static java.util.List<Component> components(Container c){java.util.List<Component> all=new ArrayList<>();for(Component child:c.getComponents()){all.add(child);if(child instanceof Container)all.addAll(components((Container)child));}return all;}
 static void layout(Container c){c.doLayout();for(Component child:c.getComponents())if(child instanceof Container)layout((Container)child);}
 public static void main(String[] args)throws Exception{SwingUtilities.invokeAndWait(()->{try{
  java.lang.reflect.Method theme=org.etlforge.application.ui.look.LookAndFeelCustomizer.class.getDeclaredMethod("installEtlForgeDarkAppearance");theme.setAccessible(true);theme.invoke(null);
  for(SourceTarget side:SourceTarget.values()){
   FileLocation location=new LocalFileLocation();EndpointCompressionPanel panel=new EndpointCompressionPanel(side);panel.reset(location);JComponent ui=panel.getUi();
   JCheckBox enable=null;JPasswordField password=null;JTextField name=null;java.util.List<JComboBox> combos=new ArrayList<>();
   for(Component c:components(ui)){if(c instanceof JCheckBox)enable=(JCheckBox)c;if(c instanceof JPasswordField)password=(JPasswordField)c;else if(c instanceof JTextField)name=(JTextField)c;if(c instanceof JComboBox)combos.add((JComboBox)c);}
   panel.apply(location);check(location.getProperty("compression_enabled")==null,side+" ordinary resave adds no compression settings");check(enable!=null&&!enable.isSelected(),side+" disabled by default");check(password!=null&&!password.isEnabled(),side+" inactive password disabled");
   panel.setSendInputChangeEvents(true);final int[] events={0};panel.addInputChangeListener(e->events[0]++);enable.doClick();password.setText("sample-secret");
   check(events[0]>0,side+" changes mark editor dirty");check(password.getEchoChar()!=0,side+" password masked");panel.apply(location);
   check(location.getBooleanProperty("compression_enabled",false),side+" enabled saved");check(!location.getProperty("compression_password").contains("sample-secret"),side+" clear password not persisted");
   check(PasswordUtils.getPassword(location,"compression_password").get().clearText().equals("sample-secret"),side+" protected password restored");
   check(location.getPropertiesForSource().containsKey("compression_enabled")&&location.getPropertiesForTarget().containsKey("compression_enabled"),side+" compression deploys as extended properties");
   XmlPersistor xml=XmlPersistor.newRoot("Endpoint");DataLocationPersistor.save(location,xml);java.io.File saved=new java.io.File("C:/ETL/Server/build-probe/compression-"+side+".xml");xml.store(saved);
   FileLocation restored=(FileLocation)DataLocationPersistor.restore(XmlPersistor.load(saved));check(restored.getBooleanProperty("compression_enabled",false),side+" save/reopen retains settings");
   if(side==SourceTarget.Target){combos.get(0).setSelectedIndex(3);check(!password.isEnabled(),"7z encryption unavailable in UI");combos.get(1).setSelectedIndex(1);check(!name.isEnabled(),"combined name disabled for individual output");panel.apply(location);check(location.getProperty("compression_format").equals("7z")&&location.getProperty("compression_mode").equals("individual"),"target format/mode saved");check(!PasswordUtils.getPassword(location,"compression_password").isPresent(),"unsupported target password not sent");combos.get(0).setSelectedIndex(0);combos.get(1).setSelectedIndex(0);}
   ui.setSize(950,270);layout(ui);java.awt.image.BufferedImage image=new java.awt.image.BufferedImage(950,270,java.awt.image.BufferedImage.TYPE_INT_RGB);Graphics2D g=image.createGraphics();ui.printAll(g);g.dispose();javax.imageio.ImageIO.write(image,"png",new java.io.File("C:/ETL/Reports/compression-"+side+"-editor.png"));
  }
  System.out.println("Passed "+checks+" compression UI checks");
 }catch(Exception e){throw new RuntimeException(e);}});}
}
