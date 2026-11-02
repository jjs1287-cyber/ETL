package org.etlforge.integration.client.project.backup;

import java.io.File;
import org.etlforge.integration.data.entity.id.IntegrationProjectId;

class FileStructure {
   private final BackupDirectory backupDir;

   public FileStructure(BackupDirectory var1) {
      this.backupDir = var1;
   }

   public File getPermanentBackupLocation(IntegrationProjectId var1) {
      return new File(this.backupDir.getPermanentBackupDirectory(), var1.toString());
   }

   public File getTemporaryBackupLocation(IntegrationProjectId var1) {
      return new File(this.backupDir.getWorkingDirectory(), var1.toString());
   }
}
