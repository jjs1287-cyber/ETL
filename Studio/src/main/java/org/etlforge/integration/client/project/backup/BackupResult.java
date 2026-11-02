package org.etlforge.integration.client.project.backup;

import java.io.File;

public final class BackupResult {
   private final File folder;
   private final boolean noBackupNeeded;

   private BackupResult(File var1, boolean var2) {
      this.folder = var1;
      this.noBackupNeeded = var2;
   }

   static BackupResult createForNoBackupNeeded() {
      return new BackupResult(null, true);
   }

   static BackupResult createForBackupAttempt(File var0) {
      return new BackupResult(var0, false);
   }

   public boolean noBackupNeeded() {
      return this.noBackupNeeded;
   }

   public File getBackupFolder() {
      return this.folder;
   }
}
