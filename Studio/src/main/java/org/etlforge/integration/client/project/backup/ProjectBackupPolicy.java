package org.etlforge.integration.client.project.backup;

import java.util.concurrent.TimeUnit;

public final class ProjectBackupPolicy {
   private static final long MILLISECONDS_IN_A_DAY = TimeUnit.MILLISECONDS.convert(1L, TimeUnit.DAYS);
   private static final ProjectBackupPolicy INSTANCE = new ProjectBackupPolicy();

   public static ProjectBackupPolicy getPolicy() {
      return INSTANCE;
   }

   private ProjectBackupPolicy() {
   }

   public boolean isEnabled() {
      return this.getReferenceBackupFrequencyInDays() > 0 || this.getMaxNumberOfIntermediateBackups() > 0;
   }

   int getMaxNumberOfIntermediateBackups() {
      return BackupPreferences.maxNumberOfIntermediateBackups().get();
   }

   int getReferenceBackupFrequencyInDays() {
      return BackupPreferences.referenceFrequencyInDays().get();
   }

   long getReferenceBackupFrequencyInMilliSeconds() {
      return this.getReferenceBackupFrequencyInDays() * MILLISECONDS_IN_A_DAY;
   }
}
