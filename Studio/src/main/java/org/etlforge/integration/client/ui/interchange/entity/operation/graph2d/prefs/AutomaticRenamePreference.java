package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.prefs;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.BooleanPreference;

public final class AutomaticRenamePreference {
   public static final BooleanPreference INSTANCE = create();

   private static BooleanPreference create() {
      String[] var0 = OperationGraphPreferencesPage.PATH();
      String var1 = "AutomaticRename";
      return ApplicationConfiguration.getPreferenceFactory().newBooleanPreference(var0, var1, Boolean.TRUE);
   }

   private AutomaticRenamePreference() {
   }
}
