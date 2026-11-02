package org.etlforge.integration.client.project.backup;

import java.util.SortedSet;
import org.etlforge.system.ArgCheck;

class KeepLatestBackupPruningPolicy implements BackupPruningPolicy {
   private final int limit;

   public KeepLatestBackupPruningPolicy(int var1) {
      this.limit = ArgCheck.checkNotNegative(var1, "limit");
   }

   @Override
   public boolean allowNewBackup() {
      return this.limit > 0;
   }

   @Override
   public void prune(SortedSet<BackupLog.Entry> var1) {
      while (this.aboveLimit(var1)) {
         this.deleteOldestBackup(var1);
      }
   }

   private boolean aboveLimit(SortedSet<BackupLog.Entry> var1) {
      return var1.size() > this.limit;
   }

   private void deleteOldestBackup(SortedSet<BackupLog.Entry> var1) {
      if (!var1.isEmpty()) {
         BackupLog.Entry var2 = (BackupLog.Entry)var1.last();
         var2.delete();
         var1.remove(var2);
      }
   }
}
