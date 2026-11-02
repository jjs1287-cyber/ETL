package org.etlforge.application.log;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.IntPreference;

public final class ApplicationLogHistoryPreference {
   public static final IntPreference INSTANCE = create();

   public static final int get() {
      return INSTANCE.get();
   }

   private static IntPreference create() {
      String[] var0 = new String[]{"General"};
      String var1 = "ApplicationLogHistoryAgeInDays";
      return ApplicationConfiguration.getPreferenceFactory().newIntPreference(var0, var1, 0, Integer.MAX_VALUE, 10);
   }

   private ApplicationLogHistoryPreference() {
   }
}
