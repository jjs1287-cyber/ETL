package org.etlforge.application.ui.widget;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.BooleanPreference;
import org.etlforge.application.prefs.PreferenceFactory;

public final class AnimationPreference {
   public static final BooleanPreference INSTANCE = createPref();

   public static boolean get() {
      return INSTANCE.get();
   }

   private static BooleanPreference createPref() {
      String[] var0 = new String[]{"General", "UI"};
      String var1 = "UseAnimation";
      PreferenceFactory var2 = ApplicationConfiguration.getPreferenceFactory();
      return var2.newBooleanPreference(var0, var1, Boolean.TRUE);
   }

   private AnimationPreference() {
   }
}
