package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.prefs;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.BooleanPreference;

public final class DrawAttributeMappingsPreference {
   public static final BooleanPreference INSTANCE = create();

   public static boolean value() {
      return INSTANCE.get();
   }

   private static BooleanPreference create() {
      String[] var0 = new String[]{"Transformations"};
      String var1 = "MapAreaDisplayOption.ATTRIBUTES";
      Boolean var2 = Boolean.TRUE;
      return ApplicationConfiguration.getPreferenceFactory().newBooleanPreference(var0, var1, var2);
   }

   private DrawAttributeMappingsPreference() {
   }
}
