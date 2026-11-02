package org.etlforge.application.ui.widget;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.BooleanPreference;
import org.etlforge.application.prefs.PreferenceFactory;

                  
public final class SplitPaneAnimationPreference {
   public static final BooleanPreference INSTANCE = createPref();

   private static BooleanPreference createPref() {
      String[] var0 = new String[]{"General"};
      String var1 = "SplitPaneAnimation";
      PreferenceFactory var2 = ApplicationConfiguration.getPreferenceFactory();
      return var2.newBooleanPreference(var0, var1, Boolean.TRUE);
   }

   private SplitPaneAnimationPreference() {
   }
}
