package org.etlforge.integration.client.project.backup;

import java.util.SortedSet;

interface BackupPruningPolicy {
   BackupPruningPolicy UNLIMITED = new BackupPruningPolicy() {
      @Override
      public void prune(SortedSet<BackupLog.Entry> var1) {
      }

      @Override
      public boolean allowNewBackup() {
         return true;
      }
   };
   BackupPruningPolicy PROHIBITED = new BackupPruningPolicy() {
      @Override
      public void prune(SortedSet<BackupLog.Entry> var1) {
         for (BackupLog.Entry var3 : var1) {
            var3.delete();
         }

         var1.clear();
      }

      @Override
      public boolean allowNewBackup() {
         return false;
      }
   };

   void prune(SortedSet<BackupLog.Entry> var1);

   boolean allowNewBackup();
}
