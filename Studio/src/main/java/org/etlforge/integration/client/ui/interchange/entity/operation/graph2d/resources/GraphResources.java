package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.resources;

import java.text.MessageFormat;
import javax.swing.Icon;
import org.etlforge.application.ui.resources.ResourceLoader;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.ui.IconProvider;
import org.etlforge.ui.StringResources;

public final class GraphResources {
   private static ResourceLoader loader = new ResourceLoader();
   public static final StringResources STRINGS;
   public static final IconProvider ICONS;
   public static final Icon SUCCESS_16 = ClientIcons.getIcon("Operation.Success.16");
   public static final Icon SUCCESS_24 = ClientIcons.getIcon("Operation.Success.24");
   public static final Icon FAILURE_16 = ClientIcons.getIcon("Operation.Error.16");
   public static final Icon FAILURE_24 = ClientIcons.getIcon("Operation.Error.24");

   public static String getString(String var0) {
      return STRINGS.getString(var0);
   }

   public static String format(String var0, Object... var1) {
      return MessageFormat.format(getString(var0), var1);
   }

   private GraphResources() {
   }

   static {
      STRINGS = loader.loadStringsFromResourceBundle("org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.resources.Strings");
      ICONS = loader.loadIconsFromPropertiesFile(
         "org/etlforge/integration/client/ui/interchange/entity/operation/graph2d/resources/icons.properties",
         "org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.resources.icons"
      );
      loader = null;
   }
}
