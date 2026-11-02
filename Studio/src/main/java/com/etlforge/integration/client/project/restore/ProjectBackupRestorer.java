package com.etlforge.integration.client.project.restore;

import com.google.common.base.Preconditions;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.etlforge.application.appdir.ApplicationTempFolder;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.integration.client.project.backup.AvailableBackup;

public class ProjectBackupRestorer {
   private final AvailableBackup backup;
   private final File destination;

   public ProjectBackupRestorer(AvailableBackup var1, File var2) {
      Preconditions.checkNotNull(var1, "backup");
      Preconditions.checkNotNull(var2, "destination");
      this.backup = var1;
      this.destination = var2;
   }

   public ProjectLocation restore() throws RestoreProjectBackupException {
      if (!this.destination.isDirectory() && !this.destination.mkdirs()) {
         throw new RestoreProjectBackupException("Could not create the destination folder \"" + this.destination.getAbsolutePath() + "\"");
      }

      File var1 = this.backupCurrentContents();

      try {
         ProjectLocation var2 = this.copyFiles();
         this.deleteFailBack(var1);
         return var2;
      } catch (IOException var4) {
         if (var1 != null) {
            this.restoreFailBack(var1);
         }

         String var3 = "Failed to replace the project data from backup.";
         if (var1 != null) {
            var3 = var3 + " The original content of the backup destination was saved to \"" + var1.getAbsolutePath() + "\"";
         }

         throw new RestoreProjectBackupException(var3, var4);
      }
   }

   private ProjectLocation copyFiles() throws IOException {
      FileUtils.cleanDirectory(this.destination);
      File var1 = this.backup.getLocation().getLocation();
      FileUtils.copyDirectory(var1, this.destination);
      return new ProjectLocation(this.backup.getLocation().getProjectName(), this.destination);
   }

   private void deleteFailBack(File var1) {
      if (var1 != null) {
         try {
            FileUtils.deleteDirectory(var1);
         } catch (Exception var3) {
            ErrorLog.log(ProjectBackupRestorer.class, "Failed to remove failback directory.", var3);
         }
      }
   }

   private File backupCurrentContents() {
      if (!org.etlforge.io.FileUtils.isFolderEmpty(this.destination)) {
         try {
            File var1 = ApplicationTempFolder.newFolder("BackupTarget");
            FileUtils.copyDirectory(this.destination, var1);
            return var1;
         } catch (IOException var2) {
            ErrorLog.log(ProjectBackupRestorer.class, "An error occurred when trying to create a failback copy of the contents to be overwritten.", var2);
         }
      }

      return null;
   }

   private void restoreFailBack(File var1) {
      if (var1 != null) {
         try {
            FileUtils.cleanDirectory(this.destination);
         } catch (IOException var4) {
            ErrorLog.log(ProjectBackupRestorer.class, "An error occurred while trying to restore overwritten data.", var4);
         }

         try {
            FileUtils.copyDirectory(var1, this.destination);
         } catch (IOException var3) {
            ErrorLog.log(ProjectBackupRestorer.class, "An error occurred while trying to restore overwritten data.", var3);
         }
      }
   }
}
