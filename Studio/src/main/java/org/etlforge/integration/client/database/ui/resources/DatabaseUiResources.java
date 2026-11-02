package org.etlforge.integration.client.database.ui.resources;

import java.text.MessageFormat;
import javax.swing.Icon;
import org.etlforge.application.ui.resources.ResourceLoader;
import org.etlforge.ui.IconProvider;
import org.etlforge.ui.StringResources;

public final class DatabaseUiResources {
   private static ResourceLoader loader = new ResourceLoader();
   public static final StringResources STRINGS;
   public static final IconProvider ICONS;
   public static final Icon PRIMARY_KEY_ICON;

   public static String getString(String var0) {
      return STRINGS.getString(var0);
   }

   public static String formatMessage(String var0, Object... var1) {
      return MessageFormat.format(getString(var0), var1);
   }

   private DatabaseUiResources() {
   }

   static {
      STRINGS = loader.loadStringsFromResourceBundle("org.etlforge.integration.client.database.ui.resources.Strings");
      ICONS = loader.loadIconsFromPropertiesFile(
         "org/etlforge/integration/client/database/ui/resources/Icons.properties", "org.etlforge.integration.client.database.ui.resources.icons"
      );
      loader = null;
      PRIMARY_KEY_ICON = ICONS.getIcon("PrimaryKey.16");
   }
}
