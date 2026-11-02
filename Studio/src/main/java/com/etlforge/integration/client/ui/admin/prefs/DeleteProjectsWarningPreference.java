package com.etlforge.integration.client.ui.admin.prefs;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.BooleanPreference;

public final class DeleteProjectsWarningPreference {
   public static final BooleanPreference INSTANCE = create();

   public static boolean get() {
      return INSTANCE.get();
   }

   private static BooleanPreference create() {
      String[] var0 = new String[]{"Administration", "ProjectConsole"};
      String var1 = "WarnBeforeDeleteProjects";
      Boolean var2 = Boolean.TRUE;
      return ApplicationConfiguration.getPreferenceFactory().newBooleanPreference(var0, var1, var2);
   }

   private DeleteProjectsWarningPreference() {
   }
}
