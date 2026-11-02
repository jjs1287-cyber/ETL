package org.etlforge.integration.activity.ui.component.filter.displayer;

import javax.swing.Icon;
import org.etlforge.integration.activity.ui.resources.ActivityResources;

final class PackageResources {
   private PackageResources() {
      throw new UnsupportedOperationException();
   }

   private static String getString(String var0) {
      return ActivityResources.STRINGS.getString(var0);
   }

   static class Launcher {
      static final String LABEL = PackageResources.getString("FilterLauncher.Label");
      static final Icon ICON = null;
   }
}
