package org.etlforge.integration.filetransfer.client.upload;

import com.google.common.collect.Lists;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.integration.filetransfer.client.ServerFile;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.OneColumnPanel;

final class RequiredFilesDisplayer implements UiProvider {
   private final RequiredFiles dependency;
   private final File suggestedDirectory;
   private final JComponent displayer;
   private final List<RequiredFileCapsule> fileCapsules;

   public RequiredFilesDisplayer(RequiredFiles var1, File var2) {
      this.dependency = var1;
      this.suggestedDirectory = var2;
      this.fileCapsules = this.createFileCapsules();
      this.displayer = this.createLayout();
   }

   public RequiredFiles getInput() {
      ArrayList var1 = Lists.newArrayList();

      for (RequiredFileCapsule var3 : this.fileCapsules) {
         var1.add(var3.getSpecifiedFile());
      }

      return new RequiredFiles(this.dependency.getParentFile(), var1);
   }

   public boolean validate() {
      boolean var1 = true;

      for (RequiredFileCapsule var3 : this.fileCapsules) {
         if (!var3.validate()) {
            var1 = false;
         }
      }

      return var1;
   }

   public ServerFile getParentFile() {
      return this.dependency.getParentFile();
   }

   public List<File> getSpecifiedFiles() {
      ArrayList var1 = Lists.newArrayList();

      for (RequiredFileCapsule var3 : this.fileCapsules) {
         var1.add(var3.getSpecifiedFile());
      }

      return var1;
   }

   private List<RequiredFileCapsule> createFileCapsules() {
      ArrayList var1 = Lists.newArrayList();

      for (File var3 : this.dependency.getRequiredFiles()) {
         var1.add(new RequiredFileCapsule(var3, this.suggestedDirectory));
      }

      return var1;
   }

   private JComponent createLayout() {
      String var1 = this.getTitle();
      OneColumnPanel var2 = new OneColumnPanel().withInsets(5, 5, 5, 5).withBorder(StyleSheet.getDefaultTitleBorder(var1));
      var2.setWeightX(1.0);

      for (RequiredFileCapsule var4 : this.fileCapsules) {
         var2.add(var4);
      }

      return BorderLayoutBuilder.fillWith(var2);
   }

   private String getTitle() {
      return Strings.format("FileUpload.FilePrompter.ParentFileLabel", new File(this.dependency.getParentFile().getFilePath()));
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.displayer);
   }

   @Override
   public void requestFocus() {
      if (!this.fileCapsules.isEmpty()) {
         this.fileCapsules.get(0).requestFocus();
      }
   }
}
