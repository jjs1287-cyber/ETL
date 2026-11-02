package com.etlforge.integration.client.ui.sfdc.wizard.prefs;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.BooleanPreference;

public final class WriteFilesAsUTF8Preference extends BooleanPreference {
   public static final WriteFilesAsUTF8Preference INSTANCE = new WriteFilesAsUTF8Preference(
      ApplicationConfiguration.getPreferenceFactory().getApplicationClass()
   );

   public WriteFilesAsUTF8Preference(Class<?> var1) {
      super(var1, "Salesforce Wizards", "WriteFilesAsUTF8", Boolean.FALSE);
   }

   @Override
   public boolean get() {
      return super.get();
   }
}
