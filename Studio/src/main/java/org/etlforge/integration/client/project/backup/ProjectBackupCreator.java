package org.etlforge.integration.client.project.backup;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.data.project.IntegrationProject;

class ProjectBackupCreator {
   private final File location;
   private final BackupLog backupLog;
   private final Logger logger;

   public ProjectBackupCreator(File var1, Logger var2) {
      this.location = var1;
      if (!var1.exists()) {
         var1.mkdirs();
      }

      this.backupLog = this.createLog(var1);
      this.logger = var2;
   }

   private BackupLog createLog(File var1) {
      BackupLog var2 = new BackupLog(var1);
      var2.setPruningPolicy(BackupType.REFERENCE, BackupPruningPolicy.UNLIMITED);
      var2.setPruningPolicy(BackupType.INTERMEDIATE, new KeepLatestBackupPruningPolicy(ProjectBackupPolicy.getPolicy().getMaxNumberOfIntermediateBackups()));
      var2.setPruningPolicy(BackupType.TEMPORARY, BackupPruningPolicy.PROHIBITED);
      return var2;
   }

   public synchronized BackupResult createBackup(IntegrationProject var1) {
      try {
         BackupFolder var2 = new BackupFolder(this.location, var1.getName(), var1.getLocationOnDisk());
         var2.setMultiThread(BackupFolder.MULTITHREAD);
         BackupType var3 = this.getNextBackupType(var2);
         if (var3 == null) {
            return BackupResult.createForNoBackupNeeded();
         }

         File var4 = var2.createBackup();
         this.log(var1, var3, var4);
         this.backupLog.addBackup(var2, var3);
         return BackupResult.createForBackupAttempt(var4);
      } catch (IOException var5) {
         this.logFailure(var5);
         return null;
      }
   }

   private BackupType getNextBackupType(BackupFolder var1) {
      if (this.isNewReferenceBackup(var1.getTimestamp())) {
         return BackupType.REFERENCE;
      } else {
         return this.isNewIntermediateBackup() ? BackupType.INTERMEDIATE : null;
      }
   }

   private boolean isNewReferenceBackup(long var1) {
      if (ProjectBackupPolicy.getPolicy().getReferenceBackupFrequencyInDays() == 0) {
         return false;
      }

      Date var3 = this.backupLog.getDateOfLastBackupOfType(BackupType.REFERENCE);
      if (var3 == null) {
         return true;
      }

      long var4 = var1 - var3.getTime();
      long var6 = ProjectBackupPolicy.getPolicy().getReferenceBackupFrequencyInMilliSeconds();
      return var4 >= var6;
   }

   private boolean isNewIntermediateBackup() {
      return ProjectBackupPolicy.getPolicy().getMaxNumberOfIntermediateBackups() > 0;
   }

   private void log(IntegrationProject var1, BackupType var2, File var3) {
      this.logger.info("Creating a backup of type " + var2 + " of the project \"" + var1.getName() + "\". Backup location: " + var3.getAbsolutePath());
   }

   private void logFailure(Throwable var1) {
      ErrorLog.log(ProjectBackupCreator.class, "Failed to create a local backup of the project", var1);
   }
}
