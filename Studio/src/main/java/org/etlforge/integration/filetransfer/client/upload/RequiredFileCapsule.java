package org.etlforge.integration.filetransfer.client.upload;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.text.KongaTextField;

final class RequiredFileCapsule implements ActionListener, UiProvider {
   private static final Icon ERROR_ICON = CommonIcons.ERROR_16;
   private static final Icon EMPTY_ICON = Empty.iconOfSameSize(ERROR_ICON);
   private final File missingFile;
   private JTextField filePathField;
   private JButton browseButton;
   private final JComponent displayer;
   private JLabel statusIndicator;
   private String lastError;

   public RequiredFileCapsule(File var1, File var2) {
      this.missingFile = var1;
      this.createComponents();
      this.checkSuggestedFile(var2);
      this.displayer = this.createLayout();
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.displayer);
   }

   @Override
   public void requestFocus() {
      this.filePathField.requestFocusInWindow();
   }

   private boolean checkValid() {
      this.lastError = null;
      String var1 = this.filePathField.getText().trim();
      if (var1.length() == 0) {
         this.lastError = Strings.get("FileUpload.FilePrompter.Error.EmptyName");
         return false;
      } else {
         File var2 = new File(var1);
         if (!var2.exists()) {
            this.lastError = Strings.get("FileUpload.FilePrompter.Error.FileDoesNotExist");
            return false;
         } else if (var2.isDirectory()) {
            this.lastError = Strings.get("FileUpload.FilePrompter.Error.PathIsDirectory");
            return false;
         } else if (!var2.getName().equalsIgnoreCase(this.missingFile.getName())) {
            this.lastError = Strings.get("FileUpload.FilePrompter.Error.NameMisMatch");
            return false;
         } else {
            return true;
         }
      }
   }

   public boolean validate() {
      boolean var1 = this.checkValid();
      if (var1) {
         this.statusIndicator.setIcon(EMPTY_ICON);
         this.statusIndicator.setToolTipText(null);
      } else {
         this.statusIndicator.setIcon(ERROR_ICON);
         this.statusIndicator.setToolTipText(this.lastError);
      }

      return var1;
   }

   public File getSpecifiedFile() {
      return new File(this.filePathField.getText());
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      File var2 = new File(this.filePathField.getText());
      File var3 = this.getDirectory(var2);
      this.selectFile(var3);
   }

   private File getDirectory(File var1) {
      File var2 = null;
      if (var1.exists() && var1.isDirectory()) {
         var2 = var1;
      } else {
         var1 = var1.getParentFile();
         if (var1 != null && var1.exists() && var1.isDirectory()) {
            var2 = var1;
         }
      }

      if (var2 == null) {
         var2 = this.missingFile.getParentFile();
      }

      return var2;
   }

   private void selectFile(File var1) {
      JFileChooser var2 = RequiredFilesPrompter.getFileChooser(var1);
      if (var2.showOpenDialog(null) == 0) {
         File var3 = var2.getSelectedFile();
         if (var3 != null) {
            this.filePathField.setText(var3.getAbsolutePath());
         }
      }
   }

   private void createComponents() {
      this.statusIndicator = new JLabel(EMPTY_ICON);
      this.filePathField = new KongaTextField(30);
      this.browseButton = new KongaButton(ApplicationResources.STRINGS.getString("Browse..."));
      this.browseButton.addActionListener(this);
   }

   private void checkSuggestedFile(File var1) {
      File var2 = new File(var1, this.missingFile.getName());
      if (var2.exists()) {
         this.filePathField.setText(var2.getAbsolutePath());
      }
   }

   private JComponent createLayout() {
      TwoColumnPanel var1 = new TwoColumnPanel();
      var1.withAnchor(TwoColumnPanel.SOUTHWEST);
      var1.withLeftInsets(0, 0, 5, 10);
      var1.withRightInsets(0, 0, 5, 0);
      var1.withLeftFill(TwoColumnPanel.HORIZONTAL);
      JLabel var2 = new JLabel(this.missingFile.getName() + ":");
      var1.addRow(var2, this.statusIndicator);
      var1.withLeftWeights(1.0, 0.0);
      var1.addRow(this.filePathField, this.browseButton);
      return BorderLayoutBuilder.fillWith(var1);
   }
}
