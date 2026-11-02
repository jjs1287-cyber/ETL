package org.etlforge.integration.client.ui.project.permissions;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.BooleanPreference;

public final class AskBeforeOverridingReadVetoPreference {
   public static final BooleanPreference INSTANCE = create();

   private static BooleanPreference create() {
      String[] var0 = new String[]{"Project", "Permissions"};
      String var1 = "OverrideReadVeto";
      Boolean var2 = Boolean.TRUE;
      return ApplicationConfiguration.getPreferenceFactory().newBooleanPreference(var0, var1, var2);
   }

   private AskBeforeOverridingReadVetoPreference() {
   }
}
