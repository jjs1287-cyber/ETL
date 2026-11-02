package org.etlforge.tools.xml.xmlviewer.ui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import org.etlforge.tools.xml.xmlviewer.ExecutionMode;
import org.etlforge.tools.xml.xmlviewer.XmlFile;
import org.etlforge.tools.xml.xmlviewer.ui.event.DesktopDropEvent;
import org.etlforge.tools.xml.xmlviewer.ui.event.DesktopDropListener;
import org.etlforge.ui.util.find.FindActionDecorator;
import org.etlforge.ui.util.find.SearchTarget;
import org.etlforge.ui.util.find.SearchTextDialog;
import org.etlforge.ui.util.opensave.IFileOpener;
import org.etlforge.ui.util.opensave.IFileSaver;
import org.etlforge.ui.util.opensave.OpenAndSaveManager;
import org.etlforge.ui.wait.WaitLock;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.ui.wait.WindowWaitService;
import org.etlforge.ui.widget.menu.KongaMenu;
import org.etlforge.ui.widget.menu.KongaMenuItem;
import org.etlforge.ui.widget.text.KongaTextArea;

public class MainWindow extends JFrame implements WaitService {
   private Desktop deskTop;
   private FileMenu fileMenu;
   private WindowsMenu windowsMenu;
   private SearchTextDialog searchDialog;
   private final Action searchAction;
   private OpenAndSaveManager openAndSaveManager;
   private MainWindow.FileManager fileManager;
   private final WaitService waitImpl;
   private final ExecutionMode executionMode;

   public MainWindow(ExecutionMode var1) {
      super("ETLForge XML Viewer");
      this.executionMode = var1;
      this.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent var1) {
            MainWindow.this.onExit(0);
         }
      });
      this.waitImpl = new WindowWaitService(this);
      this.searchAction = new MainWindow.SearchAction();
      this.initFileManagers();
      this.initUi();
   }

   private void initFileManagers() {
      this.fileManager = new MainWindow.FileManager();
      String[] var1 = new String[]{"xml", "xsd", "wsdl"};
      this.openAndSaveManager = new OpenAndSaveManager(this, this.fileManager, this.fileManager, var1);
   }

   private void initUi() {
      this.deskTop = new Desktop();
      this.deskTop.addDesktopDropListener(new DesktopDropListener() {
         @Override
         public void fileDroppedOnDesktop(DesktopDropEvent var1) {
            File var2 = var1.getDroppedFile();
            MainWindow.this.fileManager.openFile(var2);
            MainWindow.this.openAndSaveManager.addRecentFile(var2);
            MainWindow.this.openAndSaveManager.setCurrentFile(var2);
            MainWindow.this.openAndSaveManager.setCurrentDirectory(var2.getParentFile());
         }
      });
      this.setContentPane(this.deskTop);
      this.setJMenuBar(this.createMenuBar());
      this.pack();
      byte var1 = 50;
      Dimension var2 = Toolkit.getDefaultToolkit().getScreenSize();
      this.setBounds(var1, var1, var2.width - var1 * 2, var2.height - var1 * 2);
   }

   @Override
   public WaitLock startWait() {
      return this.waitImpl.startWait();
   }

   @Override
   public WaitLock startWait(int var1) {
      return this.waitImpl.startWait(var1);
   }

   public void forceToFront() {
      if (this.getState() == 1) {
         this.setState(0);
      }

      this.toFront();
   }

   private JMenuBar createMenuBar() {
      JMenuBar var1 = new JMenuBar();
      this.fileMenu = new FileMenu(this.openAndSaveManager, this);
      var1.add(this.fileMenu);
      KongaMenu var2 = new KongaMenu("&Search");
      var2.add(new KongaMenuItem(this.searchAction));
      var1.add(var2);
      this.windowsMenu = new WindowsMenu();
      var1.add(this.windowsMenu);
      return var1;
   }

   public IFileOpener getFileOpener() {
      return this.fileManager;
   }

   void onInternalFrameClosed(XmlDocumentFrame var1) {
      this.deskTop.remove(var1);
      this.windowsMenu.onFrameClosed(var1);
      boolean var2 = this.deskTop.getAllFrames().length > 0;
      this.fileMenu.setSaveEnabled(var2);
      this.searchAction.setEnabled(var2);
   }

   public Desktop getDesktop() {
      return this.deskTop;
   }

   void closeAll() {
      for (JInternalFrame var4 : this.deskTop.getAllFrames()) {
         var4.dispose();
      }

      this.deskTop.removeAll();
   }

   void saveAll() {
      WaitLock var1 = this.startWait();

      try {
         for (JInternalFrame var5 : this.deskTop.getAllFrames()) {
            try {
               ((XmlDocumentFrame)var5).saveFile();
            } catch (IOException var10) {
               JOptionPane.showMessageDialog(this, var10.getMessage(), "Save Failed", 0);
            }
         }
      } finally {
         var1.release();
      }
   }

   void onExit(int var1) {
      this.openAndSaveManager.savePreferences();
      this.dispose();
      if (this.executionMode == ExecutionMode.STAND_ALONE) {
         System.exit(var1);
      }
   }

   private class FileManager implements IFileOpener, IFileSaver {
      private FileManager() {
      }

      @Override
      public void openFile(final File var1) {
         final WaitLock var2 = MainWindow.this.startWait();
         (new SwingWorker<XmlFile, Void>() {
            public XmlFile doInBackground() throws Exception {
               return XmlFile.createXmlFile(var1);
            }

            @Override
            public void done() {
               try {
                  XmlFile var1x = this.get();
                  XmlDocumentFrame var11 = new XmlDocumentFrame(MainWindow.this, var1x);
                  var11.setVisible(true);
                  MainWindow.this.windowsMenu.addFile(var1, var11);
                  MainWindow.this.deskTop.add(var11);
                  MainWindow.this.searchAction.setEnabled(true);
                  MainWindow.this.fileMenu.refresh();

                  try {
                     var11.setSelected(true);
                  } catch (PropertyVetoException var8) {
                     var8.printStackTrace();
                  }
               } catch (Exception var9) {
                  KongaTextArea var2x = new KongaTextArea(var9.toString(), 5, 40);
                  String var3 = "Failed to open '" + var1.getName() + "'";
                  JOptionPane.showMessageDialog(null, new JScrollPane(var2x), var3, 0);
               } finally {
                  var2.release();
               }
            }
         }).execute();
      }

      @Override
      public void saveToFile(final File var1) {
         final XmlDocumentFrame var2 = MainWindow.this.deskTop.getSelectedFrame();
         final StringBuffer var3 = new StringBuffer();
         if (var2 != null) {
            (new Thread() {
               @Override
               public void run() {
                  final WaitLock var1x = MainWindow.this.startWait();

                  try {
                     var2.saveFileAs(var1);
                  } catch (IOException var3x) {
                     var3.append(var3x.getMessage());
                  }

                  SwingUtilities.invokeLater(new Runnable() {
                     @Override
                     public void run() {
                        var1x.release();
                        if (var3.length() > 0) {
                           JOptionPane.showMessageDialog(MainWindow.this, var3.toString(), "Save Failed", 0);
                        }
                     }
                  });
               }
            }).start();
         }
      }
   }

   private class SearchAction extends AbstractAction {
      public SearchAction() {
         FindActionDecorator var2 = new FindActionDecorator();
         var2.decorate(this);
         this.setEnabled(false);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               SearchAction.this.openDialog();
            }
         });
      }

      private void openDialog() {
         XmlDocumentFrame var1 = MainWindow.this.deskTop.getSelectedFrame();
         if (var1 != null) {
            SearchTarget var2 = var1.getSearchTarget();
            SearchTextDialog var3 = this.createDialog(var2);
            var3.show(MainWindow.this);
         }
      }

      private SearchTextDialog createDialog(SearchTarget var1) {
         if (MainWindow.this.searchDialog == null) {
            MainWindow.this.searchDialog = new SearchTextDialog(var1);
         } else {
            MainWindow.this.searchDialog.setTarget(var1);
         }

         return MainWindow.this.searchDialog;
      }
   }
}
