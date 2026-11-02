package com.etlforge.integration.license.client.ui.prefs;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.IntPreference;

public final class ExpirationWarningFrequencyPreference {
   public static final IntPreference INSTANCE = create();

   public static int get() {
      return INSTANCE.get();
   }

   private static IntPreference create() {
      String[] var0 = new String[]{"License"};
      String var1 = "ExpirationWarningFrequency";
      Integer var2 = 1;
      return ApplicationConfiguration.getPreferenceFactory().newIntPreference(var0, var1, 1, Integer.MAX_VALUE, var2);
   }

   private ExpirationWarningFrequencyPreference() {
   }
}
