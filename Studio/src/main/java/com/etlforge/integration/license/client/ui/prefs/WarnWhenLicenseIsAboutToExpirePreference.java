package com.etlforge.integration.license.client.ui.prefs;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.BooleanPreference;

public final class WarnWhenLicenseIsAboutToExpirePreference {
   public static final BooleanPreference INSTANCE = create();

   public static boolean get() {
      return INSTANCE.get();
   }

   private static BooleanPreference create() {
      String[] var0 = new String[]{"License"};
      String var1 = "WarnWhenLicenseIsAboutToExpire";
      Boolean var2 = Boolean.TRUE;
      return ApplicationConfiguration.getPreferenceFactory().newBooleanPreference(var0, var1, var2);
   }

   private WarnWhenLicenseIsAboutToExpirePreference() {
   }
}
