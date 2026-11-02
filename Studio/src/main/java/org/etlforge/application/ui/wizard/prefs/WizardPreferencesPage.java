package org.etlforge.application.ui.wizard.prefs;

import org.etlforge.application.ui.prefs.AbstractPreferencesPage;
import org.etlforge.application.ui.prefs.BooleanPreferenceUi;

public class WizardPreferencesPage extends AbstractPreferencesPage {
   static final String PATH = "Wizards";

   public WizardPreferencesPage() {
      super(new String[]{"Wizards"});
      this.addControl(new BooleanPreferenceUi(ConfirmWizardCancelledPreference.instance(), "Confirm before processing a cancel request"));
   }
}
