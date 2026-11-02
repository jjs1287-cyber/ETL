package org.etlforge.integration.client.ui.project.trashcan;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.BooleanPreference;
import org.etlforge.application.prefs.PreferenceFactory;
import org.etlforge.integration.client.info.ClientInfo;

public final class ReviveDependenciesWarningPreference {
   private static final BooleanPreference pref = createPref();

   public static boolean get() {
      return pref.get();
   }

   public static void set(boolean var0) {
      pref.set(var0);
   }

   public static BooleanPreference instance() {
      return pref;
   }

   private static BooleanPreference createPref() {
      PreferenceFactory var0 = ApplicationConfiguration.getPreferenceFactory();
      Boolean var1 = !ClientInfo.isDataLoader();
      return var0.newBooleanPreference(new String[]{"Project"}, "ReviveDependenciesWarning", var1);
   }

   private ReviveDependenciesWarningPreference() {
   }
}
