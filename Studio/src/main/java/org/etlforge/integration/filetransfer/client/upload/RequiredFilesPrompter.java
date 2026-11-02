package org.etlforge.integration.filetransfer.client.upload;

import com.google.common.collect.Lists;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.etlforge.application.Application;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.widget.ApplicationFileChooserFactory;
import org.etlforge.ui.Empty;
import org.etlforge.ui.canvas.ComponentCanvasFactory;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.KongaScrollPane;

final class RequiredFilesPrompter extends KongaDialog {
   private static JFileChooser fileChooser;
   private File suggestedDirectory;
   private JLabel caption;
   private JLabel suggestedFilesExplanation;
   private JPanel buttonPanel;
   private List<RequiredFiles> requiredFiles;
   private List<RequiredFilesDisplayer> displayers;

   private RequiredFilesPrompter(JFrame var1, File var2, List<RequiredFiles> var3) {
      super(var1, Strings.get("FileUpload.FilePrompter.Title"), true);
      this.suggestedDirectory = var2;
      this.requiredFiles = var3;
      this.createDialog();
      this.pack();
      this.checkInitialHeight();
   }

   private void checkInitialHeight() {
      Dimension var1 = this.getSize();
      int var2 = var1.height;
      if (var2 > 600) {
         var2 = 600;
      }

      this.setSize(var1.width, var2);
   }

   private List<RequiredFiles> getInput() {
      ArrayList var1 = Lists.newArrayList();

      for (RequiredFilesDisplayer var3 : this.displayers) {
         var1.add(var3.getInput());
      }

      return var1;
   }

   @Override
   protected void onOk() {
      if (this.isInputValid()) {
         super.onOk();
      } else {
         Alert.error(
            this.getRootPane(), Strings.get("FileUpload.FilePrompter.InvalidEntries.Title"), Strings.get("FileUpload.FilePrompter.InvalidEntries.Message")
         );
      }
   }

   private boolean isInputValid() {
      boolean var1 = true;

      for (RequiredFilesDisplayer var3 : this.displayers) {
         if (!var3.validate()) {
            var1 = false;
         }
      }

      return var1;
   }

   private void createDialog() {
      this.createComponents();
      this.layoutComponents();
   }

   private void createComponents() {
      this.caption = TextStyles.DefaultBold.makeLabel(Strings.get("FileUpload.FilePrompter.Caption"));
      this.suggestedFilesExplanation = TextStyles.InfoText.makeLabel(Strings.get("FileUpload.FilePrompter.Suggestions"));
      this.displayers = Lists.newArrayList();

      for (RequiredFiles var2 : this.requiredFiles) {
         this.displayers.add(new RequiredFilesDisplayer(var2, this.suggestedDirectory));
      }

      this.buttonPanel = this.getButtonPanel(OK_CANCEL, 15);
   }

   private void layoutComponents() {
      JComponent var1 = this.getNorthComponent();
      JComponent var2 = this.getCenterComponent();
      this.buttonPanel.setBorder(Empty.border(10));
      BorderLayoutBuilder var3 = new BorderLayoutBuilder();
      var3.northToSouth(var1, var2, this.buttonPanel);
      KongaScrollPane var4 = new KongaScrollPane(var3.container());
      this.setContentPane(var4);
   }

   private JComponent getNorthComponent() {
      JPanel var1 = new JPanel(new GridBagLayout());
      GridBagConstraints var2 = getLeftConstraints();
      var1.add(this.caption, var2);
      var1.add(this.suggestedFilesExplanation, var2);
      JPanel var3 = new JPanel(new FlowLayout(3));
      var3.add(var1);
      var3.setBorder(Empty.border(5));
      return var3;
   }

   private static final GridBagConstraints getLeftConstraints() {
      GridBagConstraints var0 = new GridBagConstraints();
      var0.anchor = 18;
      var0.insets = new Insets(3, 5, 10, 10);
      var0.gridx = 0;
      return var0;
   }

   private JComponent getCenterComponent() {
      OneColumnPanel var1 = new OneColumnPanel();
      var1.setWeightX(1.0);
      var1.withInsets(5, 5, 10, 5);

      for (RequiredFilesDisplayer var3 : this.displayers) {
         var1.add(var3);
      }

      return ComponentCanvasFactory.northBorder(var1).getUi();
   }

   static JFileChooser getFileChooser(File var0) {
      if (fileChooser == null) {
         File var1 = var0 != null ? var0 : getDefaultFolder();
         fileChooser = ApplicationFileChooserFactory.getFactory().newFileChooser(var1);
      } else if (var0 != null) {
         fileChooser.setCurrentDirectory(var0);
      }

      return fileChooser;
   }

   private static File getDefaultFolder() {
      return Application.getApplicationDirectory().getLocation();
   }

   static List<RequiredFiles> getInput(List<RequiredFiles> var0, File var1) {
      JFrame var2 = UiUtils.getActiveFrame();
      RequiredFilesPrompter var3 = new RequiredFilesPrompter(var2, var1, var0);
      if (var3.isInputValid()) {
         return var3.getInput();
      }

      var3.manageLocation();
      var3.setVisible(true);
      return var3.wasOkPressed() ? var3.getInput() : null;
   }
}
