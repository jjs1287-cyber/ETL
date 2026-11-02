package org.etlforge.integration.client.project.prefs;

import org.etlforge.application.DebugMode;
import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.BooleanPreference;
import org.etlforge.integration.client.info.ClientInfo;

public final class BlankOutPasswordsOnDeployPreference {
   public static final BooleanPreference INSTANCE = create();

   public static final boolean get() {
      return INSTANCE.get();
   }

   private static BooleanPreference create() {
      String[] var0 = new String[]{"Project"};
      String var1 = "BlankOutPasswordsOnDeploy";
      boolean var2 = !DebugMode.IS_ENABLED && !ClientInfo.isDataLoader();
      return ApplicationConfiguration.getPreferenceFactory().newBooleanPreference(var0, var1, var2);
   }

   private BlankOutPasswordsOnDeployPreference() {
   }
}
