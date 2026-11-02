package org.etlforge.integration.client.ui.script.resources;

import javax.swing.Icon;
import org.etlforge.application.ui.resources.ResourceLoader;
import org.etlforge.ui.IconProvider;
import org.etlforge.ui.StringResources;

public final class ScriptResources {
   public static final StringResources STRINGS = loadStrings();
   public static final IconProvider ICONS = loadIcons();

   public static String getString(String var0) {
      return STRINGS.getString(var0);
   }

   public static Icon getIcon(String var0) {
      return ICONS.getIcon(var0);
   }

   private static StringResources loadStrings() {
      ResourceLoader var0 = new ResourceLoader();
      return var0.loadStringsFromResourceBundle("org.etlforge.integration.client.ui.script.resources.Strings");
   }

   private static IconProvider loadIcons() {
      ResourceLoader var0 = new ResourceLoader();
      return var0.loadIconsFromPropertiesFile(
         "org/etlforge/integration/client/ui/script/resources/Icons.properties", "org.etlforge.integration.client.ui.script.resources.icons"
      );
   }

   private ScriptResources() {
   }
}
