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
public class GlobalVariableEndpointStudioTest {
 static int checks;
 static void check(boolean ok,String message){if(!ok)throw new AssertionError(message);checks++;System.out.println("PASS "+message);}
 static void layout(Container c){c.doLayout();for(Component child:c.getComponents())if(child instanceof Container)layout((Container)child);}
 static java.util.List<JTextField> fields=new ArrayList<>();
 static void inspect(Container c){for(Component child:c.getComponents()){if(child instanceof JTextField)fields.add((JTextField)child);if(child instanceof Container)inspect((Container)child);}}
 public static void main(String[] args)throws Exception{SwingUtilities.invokeAndWait(()->{try{
  java.lang.reflect.Method theme=org.etlforge.application.ui.look.LookAndFeelCustomizer.class.getDeclaredMethod("installEtlForgeDarkAppearance");theme.setAccessible(true);theme.invoke(null);
  check(DataLocationType.fromId(17)==DataLocationType.GlobalVariable,"distinct wire type 17");
  org.etlforge.integration.data.location.metadata.DataLocationMetaData metadata=org.etlforge.integration.data.location.metadata.DataLocationMetaData.getInstance();
  for(SourceTarget direction:SourceTarget.values()) {
   DataLocationType[] supported=direction==SourceTarget.Source?metadata.getSupportedSourceTypes():metadata.getSupportedTargetTypes();
   check(Arrays.asList(supported).contains(DataLocationType.GlobalVariable),direction+" installed chooser metadata includes Global Variable");
   org.etlforge.integration.client.ui.interchange.entity.LocationTypeSelectorUi chooser=new org.etlforge.integration.client.ui.interchange.entity.LocationTypeSelectorUi(Arrays.asList(supported));
   JComponent choices=org.etlforge.application.ui.widget.WizardStyleDialog.wrapUi(chooser);
   java.util.List<JRadioButton> buttons=new ArrayList<>();collectButtons(choices,buttons);
   JRadioButton global=null;for(JRadioButton button:buttons)if(button.getText().equals("Global Variable"))global=button;
   check(global!=null,direction+" actual chooser displays Global Variable");global.doClick();
   check(chooser.getSelectedType().get()==DataLocationType.GlobalVariable,direction+" chooser selects the correct endpoint type");
   choices.setSize(340,330);layout(choices);
   java.awt.image.BufferedImage preview=new java.awt.image.BufferedImage(340,330,java.awt.image.BufferedImage.TYPE_INT_RGB);Graphics2D graphics=preview.createGraphics();choices.printAll(graphics);graphics.dispose();javax.imageio.ImageIO.write(preview,"png",new java.io.File("C:/ETL/Reports/global-variable-"+direction.name().toLowerCase()+"-chooser.png"));
  }
  check(DataLocationType.getFileBasedTypes().contains(DataLocationType.GlobalVariable),"available to file transformations and archive operations");
  for(String name:new String[]{"Payload","$Payload","project.body","_value","a1"})check(GlobalVariableLocation.isValidVariableName(name),"valid name "+name);
  for(String name:new String[]{"","$","bad name","$$Payload","__internal","a/b","1name"})check(!GlobalVariableLocation.isValidVariableName(name),"reject invalid name "+name);
  GlobalVariableLocation location=(GlobalVariableLocation)DataLocationType.GlobalVariable.createLocation();location.setVariableName("$Payload");
  Source source=new Source("Global source");source.setLocation(location);check(source.isArchivable(),"source supports archive");
  XmlPersistor xml=XmlPersistor.newRoot("Endpoint");DataLocationPersistor.save(location,xml);Path file=Paths.get("C:/ETL/Server/build-probe/global-variable-endpoint.xml");xml.store(file.toFile());
  GlobalVariableLocation restored=(GlobalVariableLocation)DataLocationPersistor.restore(XmlPersistor.load(file.toFile()));check(restored.getVariableName().equals("$Payload"),"save/reopen preserves variable reference");
  check(location.getPropertiesForSource().get("locator").equals("$Payload"),"source wire locator contains the variable name");
  check(location.getPropertiesForTarget().get("locator").equals("$Payload"),"target wire locator contains the variable name");
  check(LocationConnectionTester.forLocation(location).asSourceConnectionTester(source,null).testConnection().getOutcome()==org.etlforge.integration.data.test.TestResult.SUCCESS,"Test Connection validates configuration without a runtime variable");
  GlobalVariableLocationPanel panel=new GlobalVariableLocationPanel(location,SourceTarget.Source);panel.display();
  JComponent ui=panel.getUi();ui.setSize(900,160);layout(ui);inspect(ui);
  check(fields.size()==1,"only one variable field, no file or login controls");check(fields.get(0).getText().equals("$Payload"),"editor resets from saved name");
  fields.get(0).setText("Response");panel.apply();check(location.getVariableName().equals("Response"),"editor applies a changed variable name");
  java.awt.image.BufferedImage image=new java.awt.image.BufferedImage(900,160,java.awt.image.BufferedImage.TYPE_INT_RGB);Graphics2D g=image.createGraphics();ui.printAll(g);g.dispose();javax.imageio.ImageIO.write(image,"png",new java.io.File("C:/ETL/Reports/global-variable-endpoint-editor.png"));
  Target target=new Target("Global target");target.setLocation(location.clone());
  DataLocationPanel targetPanel=new DataLocationPanelFactory(target,SourceTarget.Target).getPanel(target.getLocation());targetPanel.display();check(targetPanel instanceof GlobalVariableLocationPanel,"target uses the same single-field editor");
  System.out.println("Passed "+checks+" Studio endpoint checks");
 }catch(Exception e){throw new RuntimeException(e);}});}
 static void collectButtons(Container container,java.util.List<JRadioButton> buttons){for(Component component:container.getComponents()){if(component instanceof JRadioButton)buttons.add((JRadioButton)component);if(component instanceof Container)collectButtons((Container)component,buttons);}}
}
