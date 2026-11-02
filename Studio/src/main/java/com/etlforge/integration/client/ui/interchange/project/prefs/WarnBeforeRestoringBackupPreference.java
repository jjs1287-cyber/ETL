package com.etlforge.integration.client.ui.interchange.project.prefs;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.BooleanPreference;
import org.etlforge.application.prefs.PreferenceFactory;

public final class WarnBeforeRestoringBackupPreference {
   public static final BooleanPreference INSTANCE = createPref();

   public static boolean get() {
      return INSTANCE.get();
   }

   private static BooleanPreference createPref() {
      PreferenceFactory var0 = ApplicationConfiguration.getPreferenceFactory();
      return var0.newBooleanPreference(new String[]{"Project"}, "ConfirmDelete", Boolean.TRUE);
   }

   private WarnBeforeRestoringBackupPreference() {
   }
}
