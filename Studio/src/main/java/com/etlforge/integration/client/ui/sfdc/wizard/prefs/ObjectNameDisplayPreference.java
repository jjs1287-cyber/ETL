package com.etlforge.integration.client.ui.sfdc.wizard.prefs;

import com.etlforge.integration.salesforce.ui.NameDisplayMode;
import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.EnumPreference;

public final class ObjectNameDisplayPreference extends EnumPreference<NameDisplayMode> {
   public static final ObjectNameDisplayPreference INSTANCE = new ObjectNameDisplayPreference(
      ApplicationConfiguration.getPreferenceFactory().getApplicationClass()
   );

   public ObjectNameDisplayPreference(Class<?> var1) {
      super(var1, "Salesforce Wizards", "ObjectNameDisplay", NameDisplayMode.LABEL, NameDisplayMode.class);
   }
}
