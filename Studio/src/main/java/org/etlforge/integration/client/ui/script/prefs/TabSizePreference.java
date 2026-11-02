package org.etlforge.integration.client.ui.script.prefs;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.IntPreference;
import org.etlforge.application.prefs.PreferenceFactory;

public final class TabSizePreference {
   private static final IntPreference INSTANCE = createPref();

   public static int get() {
      return INSTANCE.get();
   }

   public static IntPreference instance() {
      return INSTANCE;
   }

   private static IntPreference createPref() {
      String[] var0 = new String[]{"Script", "UI"};
      String var1 = "TabSize";
      PreferenceFactory var2 = ApplicationConfiguration.getPreferenceFactory();
      return var2.newIntPreference(var0, var1, 1, 100, 4);
   }

   private TabSizePreference() {
   }
}
