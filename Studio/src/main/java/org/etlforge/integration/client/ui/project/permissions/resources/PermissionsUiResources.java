package org.etlforge.integration.client.ui.project.permissions.resources;

import org.etlforge.application.ui.resources.ResourceLoader;
import org.etlforge.ui.StringResources;

public final class PermissionsUiResources {
   public static final StringResources STRINGS = loadStrings();

   private static StringResources loadStrings() {
      ResourceLoader var0 = new ResourceLoader();
      return var0.loadStringsFromResourceBundle("org.etlforge.integration.client.ui.project.permissions.resources.Strings");
   }

   public static String getString(String var0) {
      return STRINGS.getString(var0);
   }

   private PermissionsUiResources() {
   }
}
