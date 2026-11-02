package org.etlforge.integration.client.ui.interchange.debug;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.BooleanPreference;

public final class LogoutDuringDebuggingWarningPreference {
   public static final BooleanPreference INSTANCE = create();

   public static boolean get() {
      return INSTANCE.get();
   }

   private static BooleanPreference create() {
      String[] var0 = new String[]{"Testing"};
      String var1 = "Logout.Warning";
      return ApplicationConfiguration.getPreferenceFactory().newBooleanPreference(var0, var1, Boolean.TRUE);
   }

   private LogoutDuringDebuggingWarningPreference() {
   }
}
