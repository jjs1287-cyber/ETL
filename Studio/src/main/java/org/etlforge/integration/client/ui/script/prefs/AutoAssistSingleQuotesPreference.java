package org.etlforge.integration.client.ui.script.prefs;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.BooleanPreference;

public final class AutoAssistSingleQuotesPreference {
   public static final BooleanPreference INSTANCE = create();

   public static final boolean isEnabled() {
      return INSTANCE.get();
   }

   private static BooleanPreference create() {
      String[] var0 = new String[]{"Script", "UI"};
      String var1 = "AutoAssist.SingleQuotes";
      Boolean var2 = Boolean.FALSE;
      return ApplicationConfiguration.getPreferenceFactory().newBooleanPreference(var0, var1, var2);
   }

   private AutoAssistSingleQuotesPreference() {
   }
}
