package org.etlforge.integration.client.ui.interchange.component.menu.prefs;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.BooleanPreference;

public final class IncludeTransformationMenuPreference {
   public static final BooleanPreference INSTANCE = create();

   public static boolean get() {
      return INSTANCE.get();
   }

   private static BooleanPreference create() {
      String[] var0 = new String[]{"Transformations"};
      String var1 = "IncludeMenu";
      return ApplicationConfiguration.getPreferenceFactory().newBooleanPreference(var0, var1, Boolean.FALSE);
   }

   private IncludeTransformationMenuPreference() {
   }
}
