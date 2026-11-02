package org.etlforge.integration.client.ui.interchange.component.tree;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.BooleanPreference;

public final class InvalidItemsIndicatorPreference {
   public static final BooleanPreference INSTANCE = create();

   private static BooleanPreference create() {
      String[] var0 = new String[]{"Project", "UI"};
      String var1 = "ShowValidationStatus";
      Boolean var2 = Boolean.TRUE;
      return ApplicationConfiguration.getPreferenceFactory().newBooleanPreference(var0, var1, var2);
   }

   private InvalidItemsIndicatorPreference() {
   }
}
