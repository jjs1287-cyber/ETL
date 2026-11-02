package org.etlforge.integration.client.ui.interchange.component.tree;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.BooleanPreference;

public final class DeployDirtyIndicatorPreference {
   public static final BooleanPreference INSTANCE = create();

   private static BooleanPreference create() {
      String[] var0 = new String[]{"Project", "UI"};
      String var1 = "DecorateDeployDirtyItems";
      Boolean var2 = Boolean.FALSE;
      return ApplicationConfiguration.getPreferenceFactory().newBooleanPreference(var0, var1, var2);
   }

   private DeployDirtyIndicatorPreference() {
   }
}
