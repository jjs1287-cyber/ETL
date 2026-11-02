package org.etlforge.integration.client.ui.admin.prefs;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.BooleanPreference;

public final class UploadUsersAndGroupsSuccessMessagePreference {
   public static final BooleanPreference INSTANCE = create();

   private static BooleanPreference create() {
      String[] var0 = new String[]{"Administration", "UserConsole"};
      String var1 = "UploadUsersAndGroupsSuccessMessage";
      Boolean var2 = Boolean.TRUE;
      return ApplicationConfiguration.getPreferenceFactory().newBooleanPreference(var0, var1, var2);
   }

   private UploadUsersAndGroupsSuccessMessagePreference() {
   }
}
