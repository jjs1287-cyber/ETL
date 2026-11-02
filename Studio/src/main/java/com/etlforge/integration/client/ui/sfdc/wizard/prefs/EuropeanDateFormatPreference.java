package com.etlforge.integration.client.ui.sfdc.wizard.prefs;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.BooleanPreference;

public final class EuropeanDateFormatPreference extends BooleanPreference {
   public static final EuropeanDateFormatPreference INSTANCE = new EuropeanDateFormatPreference(
      ApplicationConfiguration.getPreferenceFactory().getApplicationClass()
   );

   public EuropeanDateFormatPreference(Class<?> var1) {
      super(var1, "Salesforce Wizards", "EuropeanFormat", Boolean.FALSE);
   }

   @Override
   public boolean get() {
      return super.get();
   }
}
