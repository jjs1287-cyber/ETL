package org.etlforge.integration.client.ui.core.appdir;

import java.awt.EventQueue;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.concurrent.CountDownLatch;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.widget.ApplicationFileChooserFactory;
import org.etlforge.integration.client.runtime.appdir.DefaultLocation;
import org.etlforge.system.SystemUtils;
import org.etlforge.ui.Empty;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.button.KongaButtonPanel;
import org.etlforge.ui.widget.text.KongaTextField;

final class AppDirLocationFrame {
   private final JFrame frame = new JFrame(PackageResources.LocationFrame.TITLE);
   private JLabel caption;
   private JTextField textField;
   private JButton browseButton;
   private KongaButtonPanel buttonPanel;
   private File directory;
   private JFileChooser fileChooser;

   private AppDirLocationFrame(final CountDownLatch var1) {
      this.createComponents();
      this.layoutComponents();
      this.frame.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosed(WindowEvent var1x) {
            var1.countDown();
         }
      });
   }

   static File getDirectory() {
      final CountDownLatch var0 = new CountDownLatch(1);
      final AppDirLocationFrame[] var1 = new AppDirLocationFrame[1];
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            AppDirLocationFrame var1x = new AppDirLocationFrame(var0);
            var1[0] = var1x;
            var1x.frame.pack();
            var1x.frame.setResizable(false);
            var1x.frame.setLocationRelativeTo(null);
            var1x.frame.setVisible(true);
            var1x.frame.toFront();
         }
      });

      try {
         var0.await();
      } catch (InterruptedException var3) {
         Thread.currentThread().interrupt();
         var3.printStackTrace();
      }

      return var1[0] != null ? var1[0].directory : null;
   }

   private void createComponents() {
      this.caption = TextStyles.LargeInfoText.makeLabel(PackageResources.LocationFrame.CAPTION);
      this.textField = new KongaTextField(DefaultLocation.LOCATION.getAbsolutePath(), 35);
      this.browseButton = new KongaButton(new AppDirLocationFrame.BrowseAction());
      this.buttonPanel = new AppDirLocationFrame.Buttons();
   }

   private void layoutComponents() {
      Insets var1 = new Insets(10, 5, 10, 5);
      BorderBuilder var2 = new BorderBuilder().empty(10).etched().empty(5);
      new TwoColumnPanel()
         .withBorder(var2)
         .withLeftInsets(var1)
         .withRightInsets(var1)
         .addRow(this.caption)
         .addRow(this.textField, this.browseButton)
         .addToParent(this.frame.getContentPane(), "Center");
      this.buttonPanel.setBorder(Empty.border(10, 10, 10, 10));
      this.frame.getContentPane().add(this.buttonPanel, "South");
      this.frame.getRootPane().setDefaultButton(this.buttonPanel.getButton(KongaButtonPanel.OK));
   }

   private static JFileChooser createFileChooser(String var0) {
      JFileChooser var1 = ApplicationFileChooserFactory.getFactory().newFileChooser(new File(var0));
      var1.setFileSelectionMode(1);
      return var1;
   }

   private class BrowseAction extends AbstractAction {
      public BrowseAction() {
         super(PackageResources.LocationFrame.Browse.LABEL, PackageResources.LocationFrame.Browse.ICON);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (AppDirLocationFrame.this.fileChooser == null) {
            AppDirLocationFrame.this.fileChooser = AppDirLocationFrame.createFileChooser(AppDirLocationFrame.this.textField.getText());
         }

         int var2 = AppDirLocationFrame.this.fileChooser.showOpenDialog(AppDirLocationFrame.this.frame.getRootPane());
         if (var2 == 0) {
            File var3 = AppDirLocationFrame.this.fileChooser.getSelectedFile();
            AppDirLocationFrame.this.textField.setText(var3 == null ? SystemUtils.USER_HOME.getAbsolutePath() : var3.getAbsolutePath());
         }
      }
   }

   private class Buttons extends KongaButtonPanel {
      public Buttons() {
         super(new KongaButtonPanel.Button[]{OK, CANCEL}, 2, -1, 0, 10);
      }

      @Override
      protected void onCancel() {
         super.onCancel();
         AppDirLocationFrame.this.frame.dispose();
      }

      @Override
      protected void onOK() {
         super.onOK();
         AppDirLocationFrame.this.directory = null;
         String var1 = AppDirLocationFrame.this.textField.getText().trim();
         if (this.isValidPath(var1)) {
            File var2 = new File(var1);
            if (this.isExistingDirectory(var2)) {
               AppDirLocationFrame.this.directory = var2;
            } else if (!this.isExistingFile(var2) && this.createDirectory(var2)) {
               AppDirLocationFrame.this.directory = var2;
            }
         }

         if (AppDirLocationFrame.this.directory != null) {
            AppDirLocationFrame.this.frame.dispose();
         }
      }

      private boolean isValidPath(String var1) {
         if (var1.length() == 0) {
            JOptionPane.showMessageDialog(
               AppDirLocationFrame.this.frame, PackageResources.LocationFrame.EMPTY_PATH, PackageResources.LocationFrame.ERROR_TITLE, 0
            );
            return false;
         } else {
            return true;
         }
      }

      private boolean isExistingFile(File var1) {
         if (var1.exists() && var1.isFile()) {
            JOptionPane.showMessageDialog(
               AppDirLocationFrame.this.frame, PackageResources.LocationFrame.getFileIsPathMessage(var1), PackageResources.LocationFrame.ERROR_TITLE, 0
            );
            return true;
         } else {
            return false;
         }
      }

      private boolean isExistingDirectory(File var1) {
         return var1.exists() && var1.isDirectory();
      }

      private boolean createDirectory(File var1) {
         if (var1.mkdirs()) {
            return true;
         }

         JOptionPane.showMessageDialog(
            AppDirLocationFrame.this.frame, PackageResources.LocationFrame.getFailedToCreatePathMessage(var1), PackageResources.LocationFrame.ERROR_TITLE, 0
         );
         return false;
      }
   }
}
