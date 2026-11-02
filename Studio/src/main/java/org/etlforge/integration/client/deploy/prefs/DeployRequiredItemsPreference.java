package org.etlforge.integration.client.deploy.prefs;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.EnumPreference;
import org.etlforge.integration.client.deploy.RequiredItemsMode;

public final class DeployRequiredItemsPreference {
   public static final EnumPreference<RequiredItemsMode> INSTANCE = create();

   public static RequiredItemsMode get() {
      return INSTANCE.get();
   }

   private static EnumPreference<RequiredItemsMode> create() {
      String[] var0 = new String[]{"Deploy"};
      String var1 = "RequiredItemsMode";
      RequiredItemsMode var2 = RequiredItemsMode.ALWAYS_DEPLOY;
      return ApplicationConfiguration.getPreferenceFactory().newEnumPreference(var0, var1, var2, RequiredItemsMode.class);
   }

   private DeployRequiredItemsPreference() {
   }
}
