package org.etlforge.integration.loadsource.client.resources;

import java.text.MessageFormat;
import org.etlforge.application.ui.resources.ResourceLoader;
import org.etlforge.ui.StringResources;

public final class LoadSourceResources {
   private static ResourceLoader loader = new ResourceLoader();
   public static final StringResources STRINGS;

   public static String getString(String var0) {
      return STRINGS.getString(var0);
   }

   public static String format(String var0, Object... var1) {
      return MessageFormat.format(getString(var0), var1);
   }

   private LoadSourceResources() {
   }

   static {
      STRINGS = loader.loadStringsFromResourceBundle("org.etlforge.integration.loadsource.client.resources.Strings");
      loader = null;
   }
}
