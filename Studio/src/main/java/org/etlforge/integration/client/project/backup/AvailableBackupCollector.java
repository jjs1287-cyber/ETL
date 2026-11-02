package org.etlforge.integration.client.project.backup;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.etlforge.integration.data.entity.id.IntegrationProjectId;

class AvailableBackupCollector {
   private final BackupDirectory backupDir;
   private final IntegrationProjectId projectId;

   public AvailableBackupCollector(BackupDirectory var1, IntegrationProjectId var2) {
      this.backupDir = var1;
      this.projectId = var2;
   }

   public AvailableBackup[] collectBackups() {
      ArrayList var1 = Lists.newArrayList();
      FileStructure var2 = new FileStructure(this.backupDir);
      this.loadBackups(var2.getPermanentBackupLocation(this.projectId), var1);
      this.loadBackups(var2.getTemporaryBackupLocation(this.projectId), var1);
      Collections.sort(var1, new AvailableBackupCollector.BackupOrder());
      return (AvailableBackup[]) var1.toArray(new AvailableBackup[var1.size()]);
   }

   private void loadBackups(File var1, List<AvailableBackup> var2) {
      if (var1.exists()) {
         BackupLog var3 = new BackupLog(var1);
         var2.addAll(var3.getAvailableBackups());
      }
   }

   private static class BackupOrder implements Comparator<AvailableBackup>, Serializable {
      private BackupOrder() {
      }

      public int compare(AvailableBackup var1, AvailableBackup var2) {
         return var2.getTimestamp().compareTo(var1.getTimestamp());
      }
   }
}
