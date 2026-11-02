package org.etlforge.integration.client.project.prefs;

import java.util.concurrent.TimeUnit;
import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.IntPreference;

public final class TemporaryBackupFrequencyPreference {
   public static final IntPreference INSTANCE = create();

   public static long inMillis() {
      return TimeUnit.MINUTES.toMillis(INSTANCE.get());
   }

   private static IntPreference create() {
      String[] var0 = new String[]{"Project"};
      String var1 = "TemporaryBackupFrequency";
      return ApplicationConfiguration.getPreferenceFactory().newIntPreference(var0, var1, 0, Integer.MAX_VALUE, 20);
   }

   private TemporaryBackupFrequencyPreference() {
   }
}
