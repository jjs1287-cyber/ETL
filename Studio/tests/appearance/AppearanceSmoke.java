import java.awt.*;import java.awt.image.*;import java.lang.reflect.*;import javax.swing.*;import javax.imageio.ImageIO;import java.io.*;
import org.etlforge.application.ui.look.LookAndFeelCustomizer;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.application.ui.wizard.WizardSectionsUi;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
public final class AppearanceSmoke {
 static void checkLabels(Component c){if(c instanceof JLabel){String t=((JLabel)c).getText();if(t!=null&&(t.contains("SELECT TYPE")||t.contains("What do you want")))throw new AssertionError(t);}if(c instanceof JCheckBox)throw new AssertionError("unexpected checkbox");if(c instanceof Container)for(Component child:((Container)c).getComponents())checkLabels(child);}
 static void layout(Container c){c.doLayout();for(Component child:c.getComponents())if(child instanceof Container)layout((Container)child);}
 public static void main(String[] args)throws Exception{SwingUtilities.invokeAndWait(()->{try{
  LookAndFeelCustomizer.customize();
  org.etlforge.integration.data.entity.operation.OperationType.load(org.etlforge.integration.data.entity.operation.TransformationOperationType.TYPE, org.etlforge.integration.data.entity.operation.ScriptOperationType.TYPE, org.etlforge.integration.data.entity.operation.ArchiveOperationType.TYPE, org.etlforge.integration.data.entity.operation.WebServiceOperationType.TYPE, org.etlforge.integration.data.entity.operation.HostedWebServiceOperationType.TYPE);
  Class<?> cls=Class.forName("org.etlforge.integration.client.ui.interchange.entity.operation.OperationActivitySelector");Constructor<?> ctor=cls.getDeclaredConstructor();ctor.setAccessible(true);UiProvider selector=(UiProvider)ctor.newInstance();
  WizardStyleDialog dialog=new WizardStyleDialog((Window)null,"New Operation",WizardStyleDialog.wrapUi(selector),KongaDialog.CONTINUE_CANCEL,true){protected String getCaptionTitle(){return null;}};
  checkLabels(dialog.getContentPane());
  Field inactive=WizardSectionsUi.class.getDeclaredField("INACTIVE_TEXT");inactive.setAccessible(true);if(!Color.WHITE.equals(inactive.get(null)))throw new AssertionError("Inactive wizard text not white");
  if(!new Color(176,176,176).equals(UIManager.getColor("Separator.foreground")))throw new AssertionError("Wrong gray");
  Container container=dialog.getDefaultLayout().container();container.setSize(container.getPreferredSize());layout(container);
  BufferedImage image=new BufferedImage(container.getWidth(),container.getHeight(),BufferedImage.TYPE_INT_RGB);Graphics2D g=image.createGraphics();container.printAll(g);g.dispose();ImageIO.write(image,"png",new File(args[0]));dialog.dispose();
  System.out.println("PASS: operation dialog renders without removed text/checkbox; gray borders and white wizard text configured");
 }catch(Exception e){throw new RuntimeException(e);}});System.exit(0);}
}



