package org.etlforge.integration.client.ui.operation.prefs;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.BooleanPreference;

public final class ShowActivitySelectorPreference {
   public static final BooleanPreference INSTANCE = create();

   public static final boolean shouldSelectorBeShown() {
      return INSTANCE.get();
   }

   private static BooleanPreference create() {
      String[] var0 = new String[]{"Operations"};
      String var1 = "ShowActivitySelector";
      Boolean var2 = Boolean.TRUE;
      return ApplicationConfiguration.getPreferenceFactory().newBooleanPreference(var0, var1, var2);
   }

   private ShowActivitySelectorPreference() {
   }
}
