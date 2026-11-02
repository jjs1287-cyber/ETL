package org.etlforge.integration.client.ui.interchange;

import org.etlforge.application.ui.prefs.AbstractPreferencesPage;
import org.etlforge.application.ui.prefs.IntPreferenceUi;
import org.etlforge.application.ui.prefs.PreferenceUi;
import org.etlforge.application.ui.prefs.TitledPreferenceUi;
import org.etlforge.integration.client.project.prefs.DeployHistoryAgePreference;
import org.etlforge.integration.client.project.prefs.ProjectHistorySizePreference;
import org.etlforge.integration.client.project.prefs.TemporaryBackupFrequencyPreference;
import org.etlforge.integration.client.project.prefs.TestHistoryAgePreference;
import org.etlforge.integration.client.ui.project.prefs.BlankOutPasswordsOnDeployPreferenceUi;
import org.etlforge.integration.data.test.PersistTestResultsProperty;

public final class GeneralProjectPreferencesPage extends AbstractPreferencesPage {
   public static final String PATH = "Project";

   public GeneralProjectPreferencesPage() {
      super(new String[]{"Project"});
      this.addControls(this.createProjectHistorySizePref(), this.createDeployHistoryAgePref());
      if (PersistTestResultsProperty.ENABLED) {
         this.addControl(this.createTestHistoryAgePref());
      }

      this.addControls(new BlankOutPasswordsOnDeployPreferenceUi(), this.createTempBackupFrquencyPref());
   }

   private PreferenceUi createProjectHistorySizePref() {
      IntPreferenceUi var1 = new IntPreferenceUi(ProjectHistorySizePreference.instance(), "History Size:", 4);
      var1.setSelectAllWhenFocused(true);
      return new TitledPreferenceUi(
         var1,
         "Project History Size",
         "<html>This value specifies the maximum number of projects that are remembered in the local<br>client project history. The projects in the history are displayed on the opening screen when<br>the client is started.</html>"
      );
   }

   private PreferenceUi createDeployHistoryAgePref() {
      IntPreferenceUi var1 = new IntPreferenceUi(DeployHistoryAgePreference.INSTANCE, "History Size (Days):", 5);
      var1.setSelectAllWhenFocused(true);
      return new TitledPreferenceUi(
         var1,
         "Deploy History Size",
         "<html>This value specifies the number of days a record of a deploy process will be stored in the project's<br>Deploy History. Older records will be deleted."
      );
   }

   private PreferenceUi createTestHistoryAgePref() {
      IntPreferenceUi var1 = new IntPreferenceUi(TestHistoryAgePreference.INSTANCE, "History Size (Days):", 5);
      var1.setSelectAllWhenFocused(true);
      return new TitledPreferenceUi(
         var1,
         "Test History Size",
         "<html>This value specifies the number of days a record of a test (e.g. Test Transformation, Test Connection) will be stored in the project's<br>Test History. Older records will be deleted."
      );
   }

   private IntPreferenceUi createTempBackupFrquencyPref() {
      IntPreferenceUi var1 = new IntPreferenceUi(TemporaryBackupFrequencyPreference.INSTANCE, "Temporary backup frequency in minutes (0 to disable):", 5);
      var1.setSelectAllWhenFocused(true);
      return var1;
   }
}
