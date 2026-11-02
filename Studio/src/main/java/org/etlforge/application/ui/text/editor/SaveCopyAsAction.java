package org.etlforge.application.ui.text.editor;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.concurrent.Callable;
import javax.swing.AbstractAction;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.ui.util.file.DefaultFileChooser;
import org.etlforge.ui.util.file.FileChooser;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.util.preference.KongaPreferences;

public final class SaveCopyAsAction extends AbstractAction {
   private static final String DIRECTORY_KEY = "directory";
   private File sourceFile;
   private WaitService waitService = WaitService.NONE;
   private KongaPreferences directoryPreference;
   private FileChooser fc;
   private String suggestedTargetName;

   public SaveCopyAsAction() {
      this(null);
   }

   public SaveCopyAsAction(File var1) {
      super("Save a Copy As...", CommonIcons.SAVE_16);
      this.sourceFile = var1;
      this.setEnabled(var1 != null);
   }

   public void setSourceFile(File var1) {
      this.sourceFile = var1;
      this.setEnabled(var1 != null);
   }

   public void setDirectoryPreference(KongaPreferences var1) {
      this.directoryPreference = var1;
   }

   public void setSuggestedTargetName(String var1) {
      this.suggestedTargetName = var1;
   }

   public void setWaitService(WaitService var1) {
      this.waitService = var1;
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            SaveCopyAsAction.this.selectTargetAndCopy();
         }
      });
   }

   private void selectTargetAndCopy() {
      if (this.fc == null) {
         this.fc = this.createFileChooser();
      }

      this.suggestTargetFile();
      File var1 = this.fc.getFileToSave(null, null, "Save a Copy As");
      if (var1 != null) {
         this.updatePreferences(var1);
         this.copyFile(var1);
      }
   }

   private DefaultFileChooser createFileChooser() {
      DefaultFileChooser var1 = new DefaultFileChooser();
      var1.setOverwriteConfirmer(new DefaultFileChooser.DefaultConfirmer());
      if (this.directoryPreference != null) {
         File var2 = this.directoryPreference.getFile("directory", null);
         if (var2 != null) {
            var1.setCurrentDirectory(var2);
         }
      }

      return var1;
   }

   private void suggestTargetFile() {
      if (StringUtils.isNotBlank(this.suggestedTargetName)) {
         File var1 = this.fc.getCurrentDirectory();
         File var2 = new File(var1, this.suggestedTargetName);
         this.fc.setSelectedFile(var2);
      }
   }

   private void updatePreferences(File var1) {
      if (this.directoryPreference != null) {
         this.directoryPreference.putFile("directory", var1.getParentFile());
      }
   }

   private void copyFile(final File var1) {
      Callable var2 = new Callable<Void>() {
         public Void call() throws Exception {
            FileUtils.copyFile(SaveCopyAsAction.this.sourceFile, var1);
            return null;
         }
      };
      KongaSwingWorker.runCallableWhileWaiting(var2, this.waitService);
   }
}
