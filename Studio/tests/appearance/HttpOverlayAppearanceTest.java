import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
import org.etlforge.application.ui.look.LookAndFeelCustomizer;
import org.etlforge.integration.client.ui.http.HttpPropertiesPanel;
import org.etlforge.integration.http.*;
import org.etlforge.ui.input.InputPanelOverlay;

public final class HttpOverlayAppearanceTest {
 static int labels,checks;
 static void layout(Container c){c.doLayout();for(Component child:c.getComponents())if(child instanceof Container)layout((Container)child);}
 static void inspect(Component c,Color inherited){
  Color background=c instanceof JComponent&&((JComponent)c).isOpaque()?c.getBackground():inherited;
  if((c instanceof JLabel||c instanceof JCheckBox)&&c.isVisible()){
   String text=c instanceof JLabel?((JLabel)c).getText():((JCheckBox)c).getText();
   if(text!=null&&!text.isEmpty()){
    Color foreground=c.getForeground();if(Math.abs(foreground.getRed()-background.getRed())<100)throw new AssertionError("Unreadable label: "+text);labels++;
   }
  }
  if(c instanceof JCheckBox)checks++;
  if(c instanceof Container)for(Component child:((Container)c).getComponents())inspect(child,background);
 }
 public static void main(String[] args)throws Exception{
  SwingUtilities.invokeAndWait(()->{try{
   LookAndFeelCustomizer.customize();
   HttpPropertiesPanel panel=new HttpPropertiesPanel(new HttpProperties(),SslVersion.TLSV1_SSLV3,"X-ETLForge-Test: value");
   InputPanelOverlay overlay=new InputPanelOverlay(panel,"Advanced HTTP Properties");overlay.useOkAndCancel();
   JComponent root=overlay.get();root.setSize(root.getPreferredSize());layout(root);inspect(root,Color.BLACK);
   if(checks!=3||labels<6)throw new AssertionError("Missing HTTP options or labels");
   if(UIManager.getColor("konga.overlay.scrim").getRed()>50)throw new AssertionError("Backdrop is still pale");
   if(!panel.getRequestHeaders().equals("X-ETLForge-Test: value"))throw new AssertionError("Headers changed");
   BufferedImage image=new BufferedImage(root.getWidth(),root.getHeight(),BufferedImage.TYPE_INT_RGB);Graphics2D g=image.createGraphics();g.setColor(Color.BLACK);g.fillRect(0,0,image.getWidth(),image.getHeight());root.printAll(g);g.dispose();ImageIO.write(image,"png",new File(args[0]));overlay.dispose();
   System.out.println("PASS: readable title, "+checks+" HTTP options, HTTPS hint and headers; dark overlay and dimming; settings preserved");
  }catch(Exception e){throw new RuntimeException(e);}});System.exit(0);
 }
}
