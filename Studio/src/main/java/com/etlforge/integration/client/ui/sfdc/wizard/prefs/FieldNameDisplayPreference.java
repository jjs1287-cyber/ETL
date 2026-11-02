package com.etlforge.integration.client.ui.sfdc.wizard.prefs;

import com.etlforge.integration.salesforce.ui.NameDisplayMode;
import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.EnumPreference;

public final class FieldNameDisplayPreference extends EnumPreference<NameDisplayMode> {
   public static final FieldNameDisplayPreference INSTANCE = new FieldNameDisplayPreference(
      ApplicationConfiguration.getPreferenceFactory().getApplicationClass()
   );

   public FieldNameDisplayPreference(Class<?> var1) {
      super(var1, "Salesforce Wizards", "FieldNameDisplay", NameDisplayMode.API_NAME, NameDisplayMode.class);
   }
}
