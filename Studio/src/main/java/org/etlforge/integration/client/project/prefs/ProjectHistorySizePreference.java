package org.etlforge.integration.client.project.prefs;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.IntPreference;
import org.etlforge.application.prefs.PreferenceFactory;

public final class ProjectHistorySizePreference {
   private static final IntPreference INSTANCE = createPref();

   public static int get() {
      return INSTANCE.get();
   }

   public static void set(int var0) {
      INSTANCE.set(var0);
   }

   public static IntPreference instance() {
      return INSTANCE;
   }

   private static IntPreference createPref() {
      String[] var0 = new String[]{"Project"};
      String var1 = "HistorySize";
      PreferenceFactory var2 = ApplicationConfiguration.getPreferenceFactory();
      return var2.newIntPreference(var0, var1, 1, 1000, 50);
   }

   private ProjectHistorySizePreference() {
   }
}
