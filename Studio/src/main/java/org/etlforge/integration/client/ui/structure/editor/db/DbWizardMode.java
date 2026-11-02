package org.etlforge.integration.client.ui.structure.editor.db;

import org.etlforge.application.config.ApplicationConfiguration;

enum DbWizardMode {
   NORMAL,
   MINIMAL;

   public static DbWizardMode readFromAppConfig() {
      return ApplicationConfiguration.getEnum("DbWizardMode", DbWizardMode.class, NORMAL);
   }
}
