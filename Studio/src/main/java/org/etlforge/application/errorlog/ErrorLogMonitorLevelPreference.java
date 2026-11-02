package org.etlforge.application.errorlog;

import org.etlforge.application.DebugMode;
import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.EnumPreference;

public final class ErrorLogMonitorLevelPreference {
   public static final EnumPreference<ErrorLevel> INSTANCE = create();

   private static EnumPreference<ErrorLevel> create() {
      String[] var0 = new String[]{"General", "ErrorLog"};
      String var1 = "MonitorLevel";
      ErrorLevel var2 = ErrorLevel.ATTENTION;
      return ApplicationConfiguration.getPreferenceFactory().newEnumPreference(var0, var1, var2, ErrorLevel.class);
   }

   public static ErrorLevel getPreferredLevel() {
      ErrorLevel var0 = INSTANCE.get();
      return var0 == ErrorLevel.INTERNAL && !DebugMode.IS_ENABLED ? ErrorLevel.LOG : var0;
   }

   private ErrorLogMonitorLevelPreference() {
   }
}
