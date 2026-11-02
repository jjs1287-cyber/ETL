package org.etlforge.integration.client.project.backup;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.io.FileUtils;

class TemporaryBackupCreator {
   private final File location;
   private final Logger logger;
   private final BackupLog backupLog;
   private final Object lock;

   public TemporaryBackupCreator(File var1, Logger var2) {
      this.location = var1;
      this.backupLog = this.createLog(var1);
      this.logger = var2;
      this.lock = new Object();
   }

   private BackupLog createLog(File var1) {
      BackupLog var2 = new BackupLog(var1);
      var2.setPruningPolicy(BackupType.TEMPORARY, new KeepLatestBackupPruningPolicy(3));
      var2.setPruningPolicy(BackupType.REFERENCE, BackupPruningPolicy.PROHIBITED);
      var2.setPruningPolicy(BackupType.INTERMEDIATE, BackupPruningPolicy.PROHIBITED);
      if (!var2.exists()) {
         this.deleteOldBackups(var1);
      }

      return var2;
   }

   private void deleteOldBackups(File var1) {
      try {
         FileUtils.cleanFolderStructure(var1, false);
      } catch (IOException var3) {
         var3.printStackTrace();
      }
   }

   public File createBackup(IntegrationProject var1) {
      synchronized (this.lock) {
         File var10000;
         try {
            this.log(var1);
            BackupFolder var3 = new BackupFolder(this.location, var1.getName(), var1.getLocationOnDisk(), true);
            var3.setMultiThread(BackupFolder.MULTITHREAD);
            File var4 = var3.createBackup();
            this.backupLog.addBackup(var3, BackupType.TEMPORARY);
            var10000 = var4;
         } catch (IOException var6) {
            ErrorLog.log(TemporaryBackupCreator.class, "Failed to create a local backup of the project", var6);
            return null;
         }

         return var10000;
      }
   }

   private void log(IntegrationProject var1) {
      this.logger.info("Creating a working backup of the project \"" + var1.getName() + "\"");
   }
}
