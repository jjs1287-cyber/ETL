package org.etlforge.integration.activity.ui.resources;

import javax.swing.Icon;
import org.etlforge.application.ui.resources.ResourceLoader;
import org.etlforge.ui.IconProvider;
import org.etlforge.ui.StringResources;

public final class ActivityResources {
   private static ResourceLoader loader = new ResourceLoader();
   public static final StringResources STRINGS;
   public static final IconProvider ICONS;

   public static String getString(String var0) {
      return STRINGS.getString(var0);
   }

   public static Icon getIcon(String var0) {
      return ICONS.getIcon(var0);
   }

   private ActivityResources() {
      throw new UnsupportedOperationException();
   }

   static {
      STRINGS = loader.loadStringsFromResourceBundle("org.etlforge.integration.activity.ui.resources.Strings");
      ICONS = loader.loadIconsFromResourceBundle("org.etlforge.integration.activity.ui.resources.Icons");
      loader = null;
   }
}
