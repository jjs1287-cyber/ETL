package org.etlforge.application.ui.window.toolbar;

import org.etlforge.application.config.ApplicationConfiguration;

public enum ToolbarMode {
   EXTENDED,
   MINIMAL;

   public static ToolbarMode getConfiguredMode() {
      return ApplicationConfiguration.getEnum("ToolbarMode", ToolbarMode.class, EXTENDED);
   }
}
