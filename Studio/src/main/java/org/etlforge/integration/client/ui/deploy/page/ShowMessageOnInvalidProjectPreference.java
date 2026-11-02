package org.etlforge.integration.client.ui.deploy.page;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.BooleanPreference;

public final class ShowMessageOnInvalidProjectPreference {
   public static final BooleanPreference INSTANCE = create();

   public static boolean get() {
      return INSTANCE.get();
   }

   private static BooleanPreference create() {
      String[] var0 = new String[]{"Deploy"};
      String var1 = "ShowInvalidProjectMessage";
      Boolean var2 = Boolean.TRUE;
      return ApplicationConfiguration.getPreferenceFactory().newBooleanPreference(var0, var1, var2);
   }

   private ShowMessageOnInvalidProjectPreference() {
   }
}
