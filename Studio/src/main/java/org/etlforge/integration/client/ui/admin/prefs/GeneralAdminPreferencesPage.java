package org.etlforge.integration.client.ui.admin.prefs;

import org.etlforge.application.ui.prefs.AbstractPreferencesPage;
import org.etlforge.application.ui.prefs.BooleanPreferenceUi;

public class GeneralAdminPreferencesPage extends AbstractPreferencesPage {
   public static final String PATH = "Administration";

   public GeneralAdminPreferencesPage() {
      super(new String[]{"Administration"});
      this.addControl(new BooleanPreferenceUi(DeleteSystemQueueWarningPreference.INSTANCE, "Confirm before processing a Delete System Queue request"));
      this.addControl(
         new BooleanPreferenceUi(
            DownloadProjectInfoAutomaticallyPreference.INSTANCE, "Download project information automatically when the Project Console is opened"
         )
      );
      this.addControl(
         new BooleanPreferenceUi(
            DownloadUsersAndGroupsAutomaticallyPreference.INSTANCE, "Download users && groups automatically when the User Console is opened"
         )
      );
      this.addControl(
         new BooleanPreferenceUi(
            UploadUsersAndGroupsSuccessMessagePreference.INSTANCE, "Show a success message when users and groups have been uploaded to a server"
         )
      );
   }
}
