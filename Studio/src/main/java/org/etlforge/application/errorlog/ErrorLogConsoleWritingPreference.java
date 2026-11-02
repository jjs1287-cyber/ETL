package org.etlforge.application.errorlog;

import org.etlforge.application.DebugMode;
import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.BooleanPreference;

public final class ErrorLogConsoleWritingPreference {
   public static final BooleanPreference INSTANCE = create();

   private static BooleanPreference create() {
      String[] var0 = new String[]{"General", "ErrorLog"};
      String var1 = "WriteToConsole";
      Boolean var2 = DebugMode.IS_ENABLED;
      return ApplicationConfiguration.getPreferenceFactory().newBooleanPreference(var0, var1, var2);
   }

   private ErrorLogConsoleWritingPreference() {
   }
}
