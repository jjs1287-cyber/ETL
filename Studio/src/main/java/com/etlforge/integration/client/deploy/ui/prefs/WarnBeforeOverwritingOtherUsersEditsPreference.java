package com.etlforge.integration.client.deploy.ui.prefs;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.BooleanPreference;

public final class WarnBeforeOverwritingOtherUsersEditsPreference {
   public static final BooleanPreference INSTANCE = create();

   public static boolean get() {
      return INSTANCE.get();
   }

   private static BooleanPreference create() {
      String[] var0 = new String[]{"Deploy"};
      String var1 = "WarnBeforeOverwritingOtherUsersEdits";
      Boolean var2 = Boolean.TRUE;
      return ApplicationConfiguration.getPreferenceFactory().newBooleanPreference(var0, var1, var2);
   }

   private WarnBeforeOverwritingOtherUsersEditsPreference() {
   }
}
