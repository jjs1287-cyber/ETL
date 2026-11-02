package org.jitterbit.application.ui;

import org.jitterbit.application.config.ApplicationConfiguration;
import org.jitterbit.application.prefs.BooleanPreference;

public final class ShowGettingStartedPagePreference {
   public static final BooleanPreference INSTANCE = create();

   public static boolean showPage() {
      return INSTANCE.get();
   }

   private static BooleanPreference create() {
      String[] var0 = new String[]{"General"};
      String var1 = "ShowGettingstarted";
      Boolean var2 = Boolean.TRUE;
      return ApplicationConfiguration.getPreferenceFactory().newBooleanPreference(var0, var1, var2);
   }

   private ShowGettingStartedPagePreference() {
   }
}
