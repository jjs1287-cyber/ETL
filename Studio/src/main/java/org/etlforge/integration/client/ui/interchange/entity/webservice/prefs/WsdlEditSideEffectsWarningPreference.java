package org.etlforge.integration.client.ui.interchange.entity.webservice.prefs;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.BooleanPreference;

public final class WsdlEditSideEffectsWarningPreference {
   public static final BooleanPreference INSTANCE = create();

   public static boolean get() {
      return INSTANCE.get();
   }

   private static BooleanPreference create() {
      String[] var0 = new String[]{WebServiceCallPreferencesPage.PATH};
      String var1 = "EditWsdl.SideEffectsWarning";
      return ApplicationConfiguration.getPreferenceFactory().newBooleanPreference(var0, var1, Boolean.TRUE);
   }

   private WsdlEditSideEffectsWarningPreference() {
   }
}
