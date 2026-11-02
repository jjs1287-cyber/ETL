package org.etlforge.integration.client.ui.interchange.core.action;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.BooleanPreference;
import org.etlforge.application.prefs.PreferenceFactory;

public final class ConfirmDeletePreference {
   private static final BooleanPreference INSTANCE = createPref();

   public static boolean get() {
      return INSTANCE.get();
   }

   public static void set(boolean var0) {
      INSTANCE.set(var0);
   }

   public static BooleanPreference instance() {
      return INSTANCE;
   }

   private static BooleanPreference createPref() {
      PreferenceFactory var0 = ApplicationConfiguration.getPreferenceFactory();
      return var0.newBooleanPreference(new String[]{"Project"}, "ConfirmDelete", Boolean.TRUE);
   }

   private ConfirmDeletePreference() {
   }
}
