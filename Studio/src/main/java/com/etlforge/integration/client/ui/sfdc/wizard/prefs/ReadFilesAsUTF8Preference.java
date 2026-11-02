package com.etlforge.integration.client.ui.sfdc.wizard.prefs;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.BooleanPreference;

public final class ReadFilesAsUTF8Preference extends BooleanPreference {
   public static final ReadFilesAsUTF8Preference INSTANCE = new ReadFilesAsUTF8Preference(ApplicationConfiguration.getPreferenceFactory().getApplicationClass());

   public ReadFilesAsUTF8Preference(Class<?> var1) {
      super(var1, "Salesforce Wizards", "ReadFilesAsUTF8", Boolean.FALSE);
   }

   @Override
   public boolean get() {
      return super.get();
   }
}
