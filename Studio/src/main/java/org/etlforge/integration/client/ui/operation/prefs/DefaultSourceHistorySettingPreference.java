package org.etlforge.integration.client.ui.operation.prefs;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.BooleanPreference;
import org.etlforge.integration.client.info.ClientInfo;

public final class DefaultSourceHistorySettingPreference {
   public static final BooleanPreference INSTANCE = create();

   public static boolean checkHistory() {
      return INSTANCE.get();
   }

   private static BooleanPreference create() {
      String[] var0 = new String[]{"Operations"};
      String var1 = "SourceHistorySetting";
      Boolean var2 = !ClientInfo.isDataLoader();
      return ApplicationConfiguration.getPreferenceFactory().newBooleanPreference(var0, var1, var2);
   }

   private DefaultSourceHistorySettingPreference() {
   }
}
