import javax.swing.*;
import java.lang.reflect.*;
import java.nio.file.*;
import org.etlforge.integration.data.entity.ProjectVariable;
import org.etlforge.integration.client.ui.interchange.entity.projectvariable.ProjectVariablePage;
import org.etlforge.util.persist.XmlPersistor;
public class ProjectVariableEditorTest {
 static int checks;
 static void check(boolean ok,String message){if(!ok)throw new AssertionError(message);checks++;System.out.println("PASS "+message);}
 public static void main(String[] args)throws Exception {
 SwingUtilities.invokeAndWait(()->{try{
 ProjectVariable v=new ProjectVariable("ApiToken");v.setDefaultValue("preset");
 ProjectVariablePage.Fields f=new ProjectVariablePage.Fields(v);
 check(f.value.getEchoChar()==0,"ordinary value is visible");
 check(f.value.getColumns()==40,"single line value field");
 check(f.description.getRows()==4,"four line description");
 f.password.doClick();check(f.value.getEchoChar()=='*',"password typing is masked");
 check(new String(f.value.getPassword()).equals("preset"),"enabling password retains current value");
 f.value.setText("secret value");f.description.setText("Description café 世界 😀\nsecond line");f.apply(v);
 check(v.isPassword()&&v.getDefaultValue().equals("secret value"),"apply saves password flag and value");
 XmlPersistor xml=XmlPersistor.newRoot("Entity");v.writeTo(xml);java.io.File file=new java.io.File("C:/ETL/Server/build-probe/project-variable-editor.xml");xml.store(file);
 ProjectVariable restored=new ProjectVariable(v.getID(),"placeholder");restored.restoreFrom(XmlPersistor.load(file));
 check(restored.isPassword()&&restored.getDescription().equals(v.getDescription()),"save and reopen retains description and password mode");
 ProjectVariablePage.Fields reopened=new ProjectVariablePage.Fields(restored);check(reopened.value.getEchoChar()=='*',"reopened password stays masked");
 reopened.password.doClick();check(reopened.value.getPassword().length==0&&reopened.value.getEchoChar()==0,"unchecking clears instead of revealing password");
 reopened.apply(restored);check(!restored.isPassword()&&restored.getDefaultValue().isEmpty(),"cleared value and unchecked mode persist");
 Method method=ProjectVariablePage.class.getDeclaredMethod("createDataPanel");method.setAccessible(true);
 Object panel=method.invoke(new ProjectVariablePage(v));
 JComponent ui=(JComponent)panel.getClass().getMethod("getUi").invoke(panel);ui.setSize(760,400);layout(ui);
 java.awt.image.BufferedImage image=new java.awt.image.BufferedImage(760,400,java.awt.image.BufferedImage.TYPE_INT_RGB);java.awt.Graphics2D graphics=image.createGraphics();ui.printAll(graphics);graphics.dispose();javax.imageio.ImageIO.write(image,"png",new java.io.File("C:/ETL/Reports/project-variable-editor.png"));
 check(ui.getComponentCount()>0,"actual editor panel constructs and renders");
 System.out.println("Passed "+checks+" editor checks");
 }catch(Exception e){throw new RuntimeException(e);}});
 }
 static void layout(java.awt.Container c){c.doLayout();for(java.awt.Component child:c.getComponents())if(child instanceof java.awt.Container)layout((java.awt.Container)child);}
}
