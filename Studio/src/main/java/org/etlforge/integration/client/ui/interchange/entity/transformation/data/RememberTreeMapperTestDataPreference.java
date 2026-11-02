package org.etlforge.integration.client.ui.interchange.entity.transformation.data;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.BooleanPreference;

public final class RememberTreeMapperTestDataPreference {
   public static final BooleanPreference INSTANCE = create();

   public static boolean get() {
      return INSTANCE.get();
   }

   private static BooleanPreference create() {
      String[] var0 = new String[]{"Transformations", "RememberTestData"};
      String var1 = "RememberTestData";
      return ApplicationConfiguration.getPreferenceFactory().newBooleanPreference(var0, var1, Boolean.TRUE);
   }

   private RememberTreeMapperTestDataPreference() {
   }
}
