package org.etlforge.integration.client.ui.connector.mscrm.prefs;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.EnumPreference;
import org.etlforge.integration.client.ui.connector.mscrm.function.wizard.NameOptions;

public final class NameOptionPreference {
   public static EnumPreference<NameOptions> INSTANCE;

   private NameOptionPreference() {
   }

   static {
      String[] var0 = new String[]{"MSCRM Wizards"};
      String var1 = "NameOption";
      NameOptions var2 = NameOptions.LOGICAL_NAME;
      INSTANCE = ApplicationConfiguration.getPreferenceFactory().newEnumPreference(var0, var1, var2, NameOptions.class);
   }
}
