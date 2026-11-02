package org.etlforge.tools.xml.xmlviewer;

import java.awt.EventQueue;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.etlforge.tools.xml.xmlviewer.ui.MainWindow;

public class XmlViewer {
   public static final String[] ALLOWED_EXTENSIONS = new String[]{"XML", "XSD", "WSDL"};
   private MainWindow m_mainWindow;
   private List<File> m_xmlFiles;

   public static void main(String[] var0) {
      setLookAndFeel();
      MainWindow var1 = new MainWindow(ExecutionMode.STAND_ALONE);

      for (String var5 : var0) {
         String var6 = var5.trim();
         if (var6.length() > 0) {
            var1.getFileOpener().openFile(new File(var6));
         }
      }

      var1.setVisible(true);
   }

   public XmlViewer() {
      this(new File[0]);
   }

   public XmlViewer(File[] var1) {
      this.m_xmlFiles = Arrays.asList(var1);
      this.m_mainWindow = new MainWindow(ExecutionMode.BUNDLED);
   }

   public void show() {
      if (!this.m_mainWindow.isVisible()) {
         for (File var2 : this.m_xmlFiles) {
            this.m_mainWindow.getFileOpener().openFile(var2);
         }

         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               XmlViewer.this.m_mainWindow.setVisible(true);
            }
         });
      }
   }

   public void addFile(File var1) {
      this.m_mainWindow.getFileOpener().openFile(var1);
   }

   public void forceToFront() {
      this.m_mainWindow.forceToFront();
   }

   public void addWindowListener(WindowListener var1) {
      this.m_mainWindow.addWindowListener(var1);
   }

   public void removeWindowListener(WindowListener var1) {
      this.m_mainWindow.removeWindowListener(var1);
   }

   private static void setLookAndFeel() {
      try {
         String var0 = UIManager.getSystemLookAndFeelClassName();
         UIManager.setLookAndFeel(var0);
      } catch (ClassNotFoundException var1) {
         var1.printStackTrace();
      } catch (InstantiationException var2) {
         var2.printStackTrace();
      } catch (IllegalAccessException var3) {
         var3.printStackTrace();
      } catch (UnsupportedLookAndFeelException var4) {
         var4.printStackTrace();
      }
   }
}
