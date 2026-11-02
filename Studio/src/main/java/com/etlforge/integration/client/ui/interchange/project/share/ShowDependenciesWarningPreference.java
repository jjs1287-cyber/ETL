package com.etlforge.integration.client.ui.interchange.project.share;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.BooleanPreference;

public final class ShowDependenciesWarningPreference {
   public static final BooleanPreference INSTANCE = create();

   public static boolean get() {
      return INSTANCE.get();
   }

   private static BooleanPreference create() {
      String[] var0 = new String[]{"Project", "Import"};
      String var1 = "ShowDependenciesWarning";
      return ApplicationConfiguration.getPreferenceFactory().newBooleanPreference(var0, var1, Boolean.TRUE);
   }

   private ShowDependenciesWarningPreference() {
   }
}
