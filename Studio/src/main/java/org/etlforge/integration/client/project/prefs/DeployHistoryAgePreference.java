package org.etlforge.integration.client.project.prefs;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.IntPreference;
import org.etlforge.application.prefs.PreferenceFactory;

public final class DeployHistoryAgePreference {
   public static final IntPreference INSTANCE = create();

   public static final int get() {
      return INSTANCE.get();
   }

   private static IntPreference create() {
      String[] var0 = new String[]{"Project"};
      String var1 = "DeployHistorySize";
      PreferenceFactory var2 = ApplicationConfiguration.getPreferenceFactory();
      return var2.newIntPreference(var0, var1, 1, Integer.MAX_VALUE, 100);
   }

   private DeployHistoryAgePreference() {
   }
}
