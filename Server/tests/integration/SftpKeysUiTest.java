import java.awt.*;
import javax.swing.*;
import java.util.*;
import org.etlforge.integration.data.*;
import org.etlforge.integration.data.location.*;
import org.etlforge.integration.client.ui.interchange.location.ftp.*;
import org.etlforge.integration.client.ui.admin.ssh.*;
import org.etlforge.util.persist.XmlPersistor;
public final class SftpKeysUiTest {
 static int checks;
 static void check(boolean b,String label){if(!b)throw new AssertionError(label);checks++;System.out.println("PASS "+label);}
 static java.util.List<Component> components(Container c){java.util.List<Component> a=new ArrayList<>();for(Component x:c.getComponents()){a.add(x);if(x instanceof Container)a.addAll(components((Container)x));}return a;}
 static void layout(Container c){c.doLayout();for(Component x:c.getComponents())if(x instanceof Container)layout((Container)x);}
 static void render(JComponent ui,int width,int height,String name)throws Exception{ui.setSize(width,height);layout(ui);java.awt.image.BufferedImage image=new java.awt.image.BufferedImage(width,height,java.awt.image.BufferedImage.TYPE_INT_RGB);Graphics2D g=image.createGraphics();ui.printAll(g);g.dispose();javax.imageio.ImageIO.write(image,"png",new java.io.File("C:/ETL/Reports/sftp-"+name+".png"));}
 public static void main(String[] args)throws Exception{SwingUtilities.invokeAndWait(()->{try{
  java.lang.reflect.Method theme=org.etlforge.application.ui.look.LookAndFeelCustomizer.class.getDeclaredMethod("installEtlForgeDarkAppearance");theme.setAccessible(true);theme.invoke(null);
  for(SourceTarget side:SourceTarget.values()){
   FtpLocation location=new FtpLocation();SftpKeyPanel panel=new SftpKeyPanel();panel.reset(location);panel.apply(location);check(location.getProperty("sftp_key_id")==null,side+" ordinary resave adds no key setting");
   location.setProperty("sftp_key_id","00000000-0000-0000-0000-000000000001");location.setProperty("sftp_host_sha256","SHA256:AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");panel.reset(location);
   panel.setSendInputChangeEvents(true);final int[] events={0};panel.addInputChangeListener(e->events[0]++);JTextField field=null;JComboBox combo=null;for(Component c:components(panel.getUi())){if(c instanceof JTextField)field=(JTextField)c;if(c instanceof JComboBox)combo=(JComboBox)c;}
   check(combo!=null&&combo.getSelectedIndex()==1,side+" saved reference remains selected before server reload");field.setText("SHA256:BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");check(events[0]>0,side+" edits mark endpoint dirty");panel.apply(location);
   check(location.getPropertiesForSource().containsKey("sftp_key_id")&&location.getPropertiesForTarget().containsKey("sftp_key_id"),side+" key reference deploys on both endpoint types");
   XmlPersistor xml=XmlPersistor.newRoot("Endpoint");DataLocationPersistor.save(location,xml);java.io.File f=new java.io.File("C:/ETL/Server/build-probe/sftp-"+side+".xml");xml.store(f);FtpLocation restored=(FtpLocation)DataLocationPersistor.restore(XmlPersistor.load(f));check(restored.getProperty("sftp_key_id").equals(location.getProperty("sftp_key_id")),side+" key reference survives save/reopen");
   combo.setSelectedIndex(0);panel.apply(location);check(location.getProperty("sftp_key_id").equals(""),side+" password authentication clears key reference");render(panel.getUi(),760,200,"endpoint-"+side);
  }
  SshKeysPanel admin=new SshKeysPanel(r->new com.fasterxml.jackson.databind.ObjectMapper().readTree("{\"keys\":[],\"canManage\":true}"));check(components(admin).stream().filter(c->c instanceof JPasswordField).count()==1,"key import passphrase masked");check(components(admin).stream().filter(c->c instanceof JButton&&((JButton)c).getText().equals("Save key")).allMatch(c->!c.isEnabled()),"key manager disabled when disconnected");render(admin,1050,550,"key-manager");
  System.out.println("Passed "+checks+" SFTP UI checks");
 }catch(Exception e){throw new RuntimeException(e);}});}
}
