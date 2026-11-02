package org.etlforge.integration.client.ui.interchange;

import org.etlforge.application.config.ApplicationConfiguration;

public final class ShowTrashCanInProjectTreeProperty {
   public static final boolean ENABLED = getValue();

   private static boolean getValue() {
      return ApplicationConfiguration.getBoolean("Interchange.ShowTrashCanInProjectTree", false);
   }

   private ShowTrashCanInProjectTreeProperty() {
   }
}
