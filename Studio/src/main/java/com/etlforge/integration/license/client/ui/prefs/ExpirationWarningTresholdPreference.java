package com.etlforge.integration.license.client.ui.prefs;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.IntPreference;

public final class ExpirationWarningTresholdPreference {
   public static final IntPreference INSTANCE = create();

   public static int get() {
      return INSTANCE.get();
   }

   private static IntPreference create() {
      String[] var0 = new String[]{"License"};
      String var1 = "ExpirationWarningTresholdTreshold";
      Integer var2 = 30;
      return ApplicationConfiguration.getPreferenceFactory().newIntPreference(var0, var1, 1, Integer.MAX_VALUE, var2);
   }

   private ExpirationWarningTresholdPreference() {
   }
}
