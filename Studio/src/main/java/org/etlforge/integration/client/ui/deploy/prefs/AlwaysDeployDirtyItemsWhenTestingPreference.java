package org.etlforge.integration.client.ui.deploy.prefs;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.BooleanPreference;
import org.etlforge.application.ui.prefs.BooleanPreferenceUi;
import org.etlforge.integration.client.info.ClientInfo;

public final class AlwaysDeployDirtyItemsWhenTestingPreference {
   public static final BooleanPreference INSTANCE = create();

   public static final boolean get() {
      return INSTANCE.get();
   }

   private static BooleanPreference create() {
      String[] var0 = new String[]{"Deploy"};
      String var1 = "AlwaysDeployDirtyItemsWhenTesting";
      Boolean var2 = ClientInfo.isDataLoader();
      return ApplicationConfiguration.getPreferenceFactory().newBooleanPreference(var0, var1, var2);
   }

   private AlwaysDeployDirtyItemsWhenTestingPreference() {
   }

   public static BooleanPreferenceUi createUi() {
      return new BooleanPreferenceUi(INSTANCE, "Automatically deploy dirty items when running a test");
   }
}
