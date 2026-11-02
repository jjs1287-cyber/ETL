package org.etlforge.integration.client.ui.interchange;

import com.google.common.base.Supplier;
import java.io.File;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.ask.Answer;
import org.etlforge.application.ui.ask.Ask;
import org.etlforge.application.ui.widget.ApplicationFileChooser;
import org.etlforge.integration.client.info.ProductNames;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.project.UpgradeStrategy;
import org.etlforge.integration.client.ui.jitterpack.JitterPackerUi;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.util.file.FileChooser;
import org.etlforge.util.version.Version;

public final class InteractiveUpgradeStrategy implements UpgradeStrategy {
   private final ProjectManager projectManager;

   public InteractiveUpgradeStrategy(ProjectManager var1) {
      this.projectManager = var1;
   }

   @Override
   public UpgradeStrategy.Instruction getInstruction(String var1) {
      return this.isPermissionGranted(var1) ? this.askAboutBackup() : UpgradeStrategy.Instruction.CANCEL;
   }

   private boolean isPermissionGranted(String var1) {
      String var2 = "The project \""
         + var1
         + "\" was saved in an earlier version of the "
         + ProductNames.CLIENT
         + ". In order to open it with the current version, some of the project data must be updated.\n\nDo you want to update the project?";
      Answer var3 = Ask.yesOrNo(var2, "Upgrade project?");
      return var3.isYes();
   }

   private UpgradeStrategy.Instruction askAboutBackup() {
      String var1 = "Do you want to create a Jitterpak backup of the project before upgrading it?";
      Answer var2 = Ask.yesNoOrCancel(var1, "Create backup?");
      if (var2.isYes()) {
         return UpgradeStrategy.Instruction.BACKUP_AND_UPGRADE;
      } else {
         return var2.isNo() ? UpgradeStrategy.Instruction.UPGRADE : UpgradeStrategy.Instruction.CANCEL;
      }
   }

   @Override
   public boolean doBackup(String var1, File var2, Version var3) {
      File var4 = this.getTargetFile(var1, var3);
      if (var4 == null) {
         return false;
      }

      try {
         this.projectManager.createJitterPack(var1, var2, var4, true);
         return true;
      } catch (Exception var6) {
         ErrorLog.attention(this.getClass(), "Failed to create the project backup.", var6);
         return false;
      }
   }

   private File getTargetFile(String var1, Version var2) {
      return UiThreadSupplier.call(new InteractiveUpgradeStrategy.TargetFileSupplier(var1, var2));
   }

   private static class TargetFileSupplier implements Supplier<File> {
      private final String projectName;
      private final Version versionSavedIn;

      public TargetFileSupplier(String var1, Version var2) {
         this.projectName = var1;
         this.versionSavedIn = var2;
      }

      public File get() {
         System.out.println(Thread.currentThread().getName());
         FileChooser var1 = ApplicationFileChooser.getInstance(null);
         File var2 = this.getSuggestedTargetFile(var1.getCurrentDirectory());
         return var1.getFileToSave(null, JitterPackerUi.FILE_FILTER, null, "Backup to file", var2);
      }

      private File getSuggestedTargetFile(File var1) {
         String var2 = this.projectName + " - ";
         if (this.versionSavedIn != null) {
            var2 = var2 + this.versionSavedIn.getPatchLevelString();
         } else {
            var2 = var2 + "backup";
         }

         var2 = var2 + ".jpk";
         return new File(var1, var2);
      }
   }
}
