import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import org.etlforge.application.ui.look.LookAndFeelCustomizer;
import org.etlforge.integration.data.entity.*;
import org.etlforge.integration.data.entity.operation.*;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.client.ui.interchange.entity.xslt.XsltTransformPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPanel;
import org.etlforge.ui.UiProvider;

public final class XsltUiSmoke {
    static int stylesheets, labels;
    static void inspect(Component component) {
        if(component instanceof JTextArea && ((JTextArea)component).getText().contains("xsl:stylesheet")) stylesheets++;
        if(component instanceof JLabel && "XSLT".equals(((JLabel)component).getText())) labels++;
        if(component instanceof Container) for(Component child:((Container)component).getComponents()) inspect(child);
    }
    static void layout(Container container) {
        container.doLayout();
        for(Component child:container.getComponents()) if(child instanceof Container) layout((Container)child);
    }
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(()->{try {
            LookAndFeelCustomizer.customize();
            OperationType.load(TransformationOperationType.TYPE,ScriptOperationType.TYPE,ArchiveOperationType.TYPE,XsltOperationType.TYPE,WebServiceOperationType.TYPE,HostedWebServiceOperationType.TYPE);
            IntegrationProject project=IntegrationProject.createNewProject("XSLT UI smoke",Arrays.asList(EntityType.XsltTransform));
            XsltTransform asset=new XsltTransform("Expand Embedded XML"); project.getRootFolder(EntityType.XsltTransform).addItem(asset);
            XsltTransformPage page=new XsltTransformPage(asset);
            Method create=page.getClass().getDeclaredMethod("createDataPanel"); create.setAccessible(true);
            IntegrationEntityPanel panel=(IntegrationEntityPanel)create.invoke(page);
            JComponent ui=panel.getUi(); ui.setSize(850,500); layout(ui); inspect(ui);
            if(stylesheets!=1) throw new AssertionError("XSLT editor did not display its stylesheet");
            BufferedImage editor=new BufferedImage(850,500,BufferedImage.TYPE_INT_RGB); Graphics2D graphics=editor.createGraphics(); ui.paint(graphics); graphics.dispose();
            ImageIO.write(editor,"png",new File("C:/ETL/Reports/xslt-editor-preview.png"));
            Class<?> selectorClass=Class.forName("org.etlforge.integration.client.ui.interchange.entity.operation.OperationActivitySelector");
            Constructor<?> constructor=selectorClass.getDeclaredConstructor(); constructor.setAccessible(true);
            UiProvider selector=(UiProvider)constructor.newInstance();
            selector.decorate(component->{inspect(component);});
            if(labels!=1) throw new AssertionError("XSLT missing from New Operation selection");
            panel.apply();
            if(!asset.getXslt().contains("xsl:stylesheet")) throw new AssertionError("XSLT editor apply failed");
            System.out.println("PASS: packaged XSLT editor renders and applies; New Operation includes XSLT");
        } catch(Exception failure) {throw new RuntimeException(failure);} });
    }
}
