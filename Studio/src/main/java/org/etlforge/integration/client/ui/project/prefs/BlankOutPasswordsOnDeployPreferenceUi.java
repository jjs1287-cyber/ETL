package org.etlforge.integration.client.ui.project.prefs;

import org.etlforge.application.ui.prefs.BooleanPreferenceUi;
import org.etlforge.application.ui.prefs.PreferenceUi;
import org.etlforge.application.ui.prefs.TitledPreferenceUi;
import org.etlforge.integration.client.project.prefs.BlankOutPasswordsOnDeployPreference;

public final class BlankOutPasswordsOnDeployPreferenceUi extends TitledPreferenceUi {
   public BlankOutPasswordsOnDeployPreferenceUi() {
      super(
         createField(),
         "Blank Out Passwords On Deploy",
         "<html>Passwords for e.g. Sources and Targets are stored in clear text in the project files on the client.<br>For security reasons, the passwords can be erased from the client side data when the project is deployed to a server.<br>The project will still work as usual, since the passwords are used only by the server.</html>"
      );
   }

   private static PreferenceUi createField() {
      return new BooleanPreferenceUi(BlankOutPasswordsOnDeployPreference.INSTANCE, "Blank out passwords from the project data on the client side after deploy");
   }
}
