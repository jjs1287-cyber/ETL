import javax.swing.*;
import java.awt.*;
import java.lang.reflect.*;
import org.etlforge.integration.data.entity.*;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.client.ui.script.builder.*;
import org.etlforge.integration.client.ui.interchange.entity.script.page.ScriptPanel;
public class ScriptTemplateAppearanceTest {
 static void check(boolean ok,String message){if(!ok)throw new AssertionError(message);System.out.println("PASS "+message);}
 static void layout(Container c){c.doLayout();for(Component child:c.getComponents())if(child instanceof Container)layout((Container)child);}
 public static void main(String[] args)throws Exception{SwingUtilities.invokeAndWait(()->{try{
  Method theme=org.etlforge.application.ui.look.LookAndFeelCustomizer.class.getDeclaredMethod("installEtlForgeDarkAppearance");theme.setAccessible(true);theme.invoke(null);
  Script script=new Script("New Script");check(script.getScript().equals("<trans>\n//App script goes between trans tags static txt is outside of any tag.\n\n</trans><javascript>\n//java script goes hear\n\n</javascript>"),"new scripts contain both blocks and requested instructions");
  IntegrationProject project=IntegrationProject.createNewProject("Appearance fixture");script.setParent(project.getRootFolder(EntityType.Script));
  ScriptBuilderModel model=new ScriptBuilderModel(project);model.setSource(script);
  ScriptBuilderUi builder=new ScriptBuilderUi(model,new CommunityScriptModeSelectorFactory().newSelector());builder.setExpression(script.getScript());
  ScriptPanel panel=new ScriptPanel(null,script,builder);panel.display();JComponent ui=panel.getUi();
  Field inline=ScriptPanel.class.getDeclaredField("inlineJavaScriptNote"),below=ScriptPanel.class.getDeclaredField("belowJavaScriptNote");inline.setAccessible(true);below.setAccessible(true);
  JLabel right=(JLabel)inline.get(panel),bottom=(JLabel)below.get(panel);
  Method position=ScriptPanel.class.getDeclaredMethod("updateJavaScriptNotePosition");position.setAccessible(true);
  for(int width:new int[]{1100,480}){
   ui.setSize(width,500);position.invoke(panel);layout(ui);
   check(width==1100?right.isVisible()&&!bottom.isVisible():!right.isVisible()&&bottom.isVisible(),"warning placement at width "+width);
   JLabel visible=right.isVisible()?right:bottom;check(visible.getText().equals("NOTE javascript is slower"),"exact warning text");
   java.awt.image.BufferedImage image=new java.awt.image.BufferedImage(width,500,java.awt.image.BufferedImage.TYPE_INT_RGB);Graphics2D g=image.createGraphics();ui.printAll(g);g.dispose();javax.imageio.ImageIO.write(image,"png",new java.io.File("C:/ETL/Reports/script-javascript-template-"+width+".png"));
  }
  script.setScript("<trans>1+2</trans>");check(script.getScript().equals("<trans>1+2</trans>"),"existing script content remains unchanged");
  panel.dispose();
 }catch(Exception e){throw new RuntimeException(e);}});System.exit(0);}
}
