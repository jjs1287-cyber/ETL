package org.etlforge.integration.client.project.backup;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.IntPreference;

final class BackupPreferences {
   static final String[] PATH = new String[]{"Project", "Backup Policy"};
   private static final IntPreference referenceFrequencyPrefs = ApplicationConfiguration.getPreferenceFactory()
      .newIntPreference(PATH, "referenceFrequency", 0, 1000, 7);
   private static final IntPreference maxIntermediate = ApplicationConfiguration.getPreferenceFactory().newIntPreference(PATH, "maxIntermediate", 0, 1000, 10);

   static IntPreference referenceFrequencyInDays() {
      return referenceFrequencyPrefs;
   }

   static IntPreference maxNumberOfIntermediateBackups() {
      return maxIntermediate;
   }

   private BackupPreferences() {
   }
}
