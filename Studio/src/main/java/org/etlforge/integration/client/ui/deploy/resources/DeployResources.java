package org.etlforge.integration.client.ui.deploy.resources;

import org.etlforge.application.ui.resources.ResourceLoader;
import org.etlforge.ui.IconProvider;
import org.etlforge.ui.StringResources;

public final class DeployResources {
   private static ResourceLoader loader = new ResourceLoader();
   public static final StringResources STRINGS;
   public static final IconProvider ICONS;

   public static String getString(String var0) {
      return STRINGS.getString(var0);
   }

   private DeployResources() {
   }

   static {
      STRINGS = loader.loadStringsFromResourceBundle("org.etlforge.integration.client.ui.deploy.resources.Strings");
      ICONS = loader.loadIconsFromPropertiesFile(
         "org/etlforge/integration/client/ui/deploy/resources/Icons.properties", "org.etlforge.integration.client.ui.deploy.resources.icons"
      );
      loader = null;
   }
}
