package org.etlforge.integration.client.ui.structure.editor.resources;

import java.text.MessageFormat;
import org.etlforge.application.ui.resources.ResourceLoader;
import org.etlforge.ui.IconProvider;
import org.etlforge.ui.StringResources;

public final class StructureEditorResources {
   private static ResourceLoader loader = new ResourceLoader();
   public static final StringResources STRINGS;
   public static final IconProvider ICONS;

   public static String getString(String var0) {
      return STRINGS.getString(var0);
   }

   public static String formatString(String var0, Object... var1) {
      return MessageFormat.format(getString(var0), var1);
   }

   private StructureEditorResources() {
   }

   static {
      STRINGS = loader.loadStringsFromResourceBundle("org.etlforge.integration.client.ui.structure.editor.resources.Strings");
      ICONS = loader.loadIconsFromPropertiesFile(
         "org/etlforge/integration/client/ui/structure/editor/resources/Icons.properties",
         "org.etlforge.integration.client.ui.structure.editor.resources.icons"
      );
      loader = null;
   }
}
