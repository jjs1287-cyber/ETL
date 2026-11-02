import java.awt.*;
import javax.swing.*;
import java.nio.file.*;
import java.util.*;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.*;
import org.etlforge.integration.data.location.*;
import org.etlforge.integration.client.ui.interchange.location.panel.*;
import org.etlforge.integration.client.ui.interchange.location.LocationConnectionTester;
import org.etlforge.util.persist.XmlPersistor;
public final class S3StudioTest {
 static int checks;
 static void check(boolean ok,String message){if(!ok)throw new AssertionError(message);checks++;System.out.println("PASS "+message);}
 static java.util.List<Component> components(Container c){java.util.List<Component> out=new ArrayList<>();for(Component child:c.getComponents()){out.add(child);if(child instanceof Container)out.addAll(components((Container)child));}return out;}
 static void layout(Container c){c.doLayout();for(Component child:c.getComponents())if(child instanceof Container)layout((Container)child);}
 public static void main(String[] args)throws Exception{SwingUtilities.invokeAndWait(()->{try{
  java.lang.reflect.Method theme=org.etlforge.application.ui.look.LookAndFeelCustomizer.class.getDeclaredMethod("installEtlForgeDarkAppearance");theme.setAccessible(true);theme.invoke(null);
  check(DataLocationType.fromId(18)==DataLocationType.AmazonS3,"wire type 18 is Amazon S3");
  check(DataLocationType.getFileBasedTypes().contains(DataLocationType.AmazonS3),"available to archive and file transformations");
  org.etlforge.integration.data.location.metadata.DataLocationMetaData metadata=org.etlforge.integration.data.location.metadata.DataLocationMetaData.getInstance();
  for(SourceTarget side:SourceTarget.values()){
   DataLocationType[] types=side==SourceTarget.Source?metadata.getSupportedSourceTypes():metadata.getSupportedTargetTypes();check(Arrays.asList(types).contains(DataLocationType.AmazonS3),side+" chooser metadata");
   org.etlforge.integration.client.ui.interchange.entity.LocationTypeSelectorUi chooser=new org.etlforge.integration.client.ui.interchange.entity.LocationTypeSelectorUi(Arrays.asList(types));JComponent choice=org.etlforge.application.ui.widget.WizardStyleDialog.wrapUi(chooser);
   JRadioButton selected=null;for(Component c:components(choice))if(c instanceof JRadioButton&&((JRadioButton)c).getText().equals("Amazon S3"))selected=(JRadioButton)c;check(selected!=null,side+" visible Amazon S3 chooser option");selected.doClick();check(chooser.getSelectedType().get()==DataLocationType.AmazonS3,side+" chooser selects correct type");
   S3Location l=new S3Location();l.setBucket("testing-source-target");l.setRegion("us-east-1");l.setPrefix("input/");l.setCredential("s3_access_key_protected","AKIA0000000000000000");l.setCredential("s3_secret_key_protected","fixture-secret-only");l.setCredential("s3_session_token_protected","fixture-session-only");
   S3LocationPanel panel=new S3LocationPanel(l,side);panel.display();panel.display();check(LocationConnectionTester.forLocation(l)!=null,side+" connection tester resolves");
   java.util.List<Component> all=components(panel.getUi());check(all.stream().filter(c->c instanceof JPasswordField).count()>=2,side+" secret and token use masked controls");
   check(all.stream().filter(c->c instanceof JPasswordField).allMatch(c->((JPasswordField)c).getEchoChar()!=0),side+" password echo disabled");
   panel.apply();check(l.getRegion().equals("us-east-1"),side+" friendly region label stores its AWS code");check(l.getCredential("s3_secret_key_protected").equals("fixture-secret-only"),side+" generic file options do not overwrite S3 secret");check(l.getCredential("s3_session_token_protected").isEmpty(),side+" access-key authentication clears obsolete token");
   check(l.getPropertiesForSource().containsKey("s3_secret_key_protected")&&l.getPropertiesForTarget().containsKey("s3_access_key_protected"),side+" encrypted properties deploy");
   XmlPersistor xml=XmlPersistor.newRoot("Endpoint");DataLocationPersistor.save(l,xml);Path path=Paths.get("C:/ETL/Server/build-probe/s3-"+side+".xml");xml.store(path.toFile());String saved=new String(Files.readAllBytes(path),java.nio.charset.StandardCharsets.UTF_8);
   check(!saved.contains("fixture-secret-only")&&!saved.contains("AKIA0000000000000000"),side+" project contains ciphertext, not plaintext credentials");
   S3Location restored=(S3Location)DataLocationPersistor.restore(XmlPersistor.load(path.toFile()));check(restored.getCredential("s3_secret_key_protected").equals("fixture-secret-only")&&restored.getBucket().equals(l.getBucket()),side+" save/reopen retains portable encrypted credentials");
   S3Location copied=(S3Location)l.getCopyForSourceOrTarget(side);check(copied.getCredential("s3_access_key_protected").equals(l.getCredential("s3_access_key_protected")),side+" endpoint copy preserves encryption");
   panel.setSendInputChangeEvents(true);int[] events={0};panel.addInputChangeListener(e->events[0]++);for(Component c:all)if(c instanceof JTextField&&!(c instanceof JPasswordField)&&((JTextField)c).getText().equals("input/")){((JTextField)c).setText("changed/");break;}check(events[0]>0,side+" edits mark endpoint dirty");panel.apply();check(l.getPrefix().equals("changed/"),side+" prefix edit applied");
   for(Component c:all)if(c instanceof JComboBox){JComboBox combo=(JComboBox)c;if(combo.getItemCount()==2&&combo.getItemAt(0).equals("Access Key"))combo.setSelectedIndex(1);}check(all.stream().anyMatch(c->c instanceof JPasswordField&&c.isVisible()),side+" temporary credentials selectable");
   JComponent ui=panel.getUi();ui.setSize(1050,1100);layout(ui);java.awt.image.BufferedImage image=new java.awt.image.BufferedImage(1050,1100,java.awt.image.BufferedImage.TYPE_INT_RGB);Graphics2D g=image.createGraphics();ui.printAll(g);g.dispose();javax.imageio.ImageIO.write(image,"png",new java.io.File("C:/ETL/Reports/s3-"+side+".png"));
  }
  System.out.println(checks+" S3 Studio checks passed");
 }catch(Exception e){throw new RuntimeException(e);}});}
}
