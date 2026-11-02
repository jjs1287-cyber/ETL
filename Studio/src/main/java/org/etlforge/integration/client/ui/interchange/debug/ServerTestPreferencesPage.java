package org.etlforge.integration.client.ui.interchange.debug;

import org.etlforge.application.ui.prefs.AbstractPreferencesPage;
import org.etlforge.application.ui.prefs.BooleanPreferenceUi;

public final class ServerTestPreferencesPage extends AbstractPreferencesPage {
   public static final String PATH = "Testing";

   public ServerTestPreferencesPage() {
      super(new String[]{"Testing"});
      this.addControl(
         new BooleanPreferenceUi(LogoutDuringDebuggingWarningPreference.INSTANCE, "Show a warning if logging out from a server while running a test")
      );
   }
}
