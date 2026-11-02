package org.etlforge.integration.client.ui.interchange.entity.webservice.prefs;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.BooleanPreference;

public final class ShowWebServiceStructuresPreference {
   public static final BooleanPreference INSTANCE = create();

   public static boolean showStructures() {
      return INSTANCE.get();
   }

   private static BooleanPreference create() {
      String[] var0 = new String[]{WebServiceCallPreferencesPage.PATH};
      String var1 = "ShowStructures";
      return ApplicationConfiguration.getPreferenceFactory().newBooleanPreference(var0, var1, Boolean.TRUE);
   }

   private ShowWebServiceStructuresPreference() {
   }
}
