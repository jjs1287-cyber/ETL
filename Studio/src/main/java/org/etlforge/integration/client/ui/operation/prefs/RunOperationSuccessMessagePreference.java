package org.etlforge.integration.client.ui.operation.prefs;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.BooleanPreference;

public final class RunOperationSuccessMessagePreference {
   public static final BooleanPreference INSTANCE = create();

   private static BooleanPreference create() {
      String[] var0 = new String[]{"Operations"};
      String var1 = "RunOperation.SuccessMessage";
      return ApplicationConfiguration.getPreferenceFactory().newBooleanPreference(var0, var1, Boolean.FALSE);
   }

   private RunOperationSuccessMessagePreference() {
   }
}
