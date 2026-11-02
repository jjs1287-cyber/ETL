package org.etlforge.integration.client.server.prefs;

import java.util.concurrent.TimeUnit;
import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.IntPreference;

public final class ServerCallTimeoutPreference {
   public static final IntPreference INSTANCE = create();

   public static int inMinutes() {
      return INSTANCE.get();
   }

   private static IntPreference create() {
      String[] var0 = new String[]{"Server"};
      String var1 = "Timeout";
      return ApplicationConfiguration.getPreferenceFactory().newIntPreference(var0, var1, 1, getMaxValue(), 20);
   }

   private static int getMaxValue() {
      return (int)(2147483647L / TimeUnit.MINUTES.toMillis(1L));
   }

   private ServerCallTimeoutPreference() {
   }
}
