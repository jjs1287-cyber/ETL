package org.etlforge.integration.client.ui.admin.prefs;

import org.etlforge.application.ui.prefs.AbstractPreferencesPage;
import org.etlforge.application.ui.prefs.BooleanPreferenceUi;

public class EnterpriseAdminPreferencesPage extends AbstractPreferencesPage {
   public static final String PATH = "Administration";

   public EnterpriseAdminPreferencesPage() {
      super(new String[]{"Administration"});
      this.addControl(new BooleanPreferenceUi(DeleteSystemQueueWarningPreference.INSTANCE, "Confirm before processing a Delete System Queue request"));
      this.addControl(new BooleanPreferenceUi(DeleteProjectsWarningPreference.INSTANCE, "Confirm before processing a Delete Project request"));
   }
}
