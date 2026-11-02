package com.etlforge.integration.client.deploy.ui.prefs;

import org.etlforge.application.ui.prefs.AbstractPreferencesPage;
import org.etlforge.application.ui.prefs.BooleanPreferenceUi;
import org.etlforge.integration.client.ui.deploy.prefs.AlwaysDeployDirtyItemsWhenTestingPreference;
import org.etlforge.integration.client.ui.deploy.prefs.RequiredItemsPreferenceUi;
import org.etlforge.integration.client.ui.project.prefs.BlankOutPasswordsOnDeployPreferenceUi;

public class DeployPreferencesPage extends AbstractPreferencesPage {
   public static final String PATH = "Deploy";

   public DeployPreferencesPage() {
      super(new String[]{"Deploy"});
      this.addControls(
         new BlankOutPasswordsOnDeployPreferenceUi(),
         new RequiredItemsPreferenceUi(),
         AlwaysDeployDirtyItemsWhenTestingPreference.createUi(),
         this.createShowWarningOnOverwritePrefUi(),
         this.createShowWarningForSharedItemsPrefUi()
      );
   }

   private BooleanPreferenceUi createShowWarningOnOverwritePrefUi() {
      return new BooleanPreferenceUi(
         WarnBeforeOverwritingOtherUsersEditsPreference.INSTANCE, "Show a warning if deploy will overwrite another users changes on the server"
      );
   }

   private BooleanPreferenceUi createShowWarningForSharedItemsPrefUi() {
      return new BooleanPreferenceUi(
         WarnWhenDeployingSharedItemsPreference.INSTANCE, "Show a warning when deploying items that are shared between multiple projects"
      );
   }
}
