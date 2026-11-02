package org.etlforge.integration.client.ui.script.prefs;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.BooleanPreference;
import org.etlforge.application.ui.prefs.BooleanPreferenceUi;

public final class AutoSuggestFunctionArgumentsPreference {
   public static final BooleanPreference INSTANCE = create();

   public static final boolean get() {
      return INSTANCE.get();
   }

   private static BooleanPreference create() {
      String[] var0 = new String[]{"Script", "UI"};
      String var1 = "AutoSuggestFunctionArguments";
      Boolean var2 = Boolean.TRUE;
      return ApplicationConfiguration.getPreferenceFactory().newBooleanPreference(var0, var1, var2);
   }

   private AutoSuggestFunctionArgumentsPreference() {
   }

   public static BooleanPreferenceUi createUi() {
      return new BooleanPreferenceUi(INSTANCE, "Automatically suggest function arguments when possible");
   }
}
