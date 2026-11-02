package com.etlforge.integration.client.ui.interchange.project.prefs;

import com.etlforge.integration.client.ui.interchange.project.share.ShowDependenciesWarningPreference;
import org.etlforge.application.ui.prefs.AbstractPreferencesPage;
import org.etlforge.application.ui.prefs.BooleanPreferenceUi;

public class ProjectRestorePreferencesPage extends AbstractPreferencesPage {
   public static String[] path() {
      return new String[]{"Project", "Import & Restore"};
   }

   public ProjectRestorePreferencesPage() {
      super(path());
      this.addControl(
         new BooleanPreferenceUi(WarnBeforeRestoringBackupPreference.INSTANCE, "Ask for confirmation before restoring a complete project from backup")
      );
      this.addControl(
         new BooleanPreferenceUi(
            ShowDependenciesWarningPreference.INSTANCE, "Ask for confirmation when importing/restoring an item that has required dependencies"
         )
      );
   }
}
