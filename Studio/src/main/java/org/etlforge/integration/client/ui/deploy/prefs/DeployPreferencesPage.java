package org.etlforge.integration.client.ui.deploy.prefs;

import org.etlforge.application.ui.prefs.AbstractPreferencesPage;
import org.etlforge.integration.client.ui.project.prefs.BlankOutPasswordsOnDeployPreferenceUi;

public final class DeployPreferencesPage extends AbstractPreferencesPage {
   public DeployPreferencesPage() {
      super(new String[]{"Deploy"});
      this.addControls(new BlankOutPasswordsOnDeployPreferenceUi(), new RequiredItemsPreferenceUi(), AlwaysDeployDirtyItemsWhenTestingPreference.createUi());
   }
}
