package org.etlforge.integration.client.ui.interchange.location.resources;

import java.text.MessageFormat;
import org.etlforge.application.ui.resources.ResourceLoader;
import org.etlforge.ui.StringResources;

public final class LocationResources {
   public static final StringResources STRINGS = loadStrings();

   public static String getString(String var0) {
      return STRINGS.getString(var0);
   }

   public static String format(String var0, Object... var1) {
      return MessageFormat.format(getString(var0), var1);
   }

   private static StringResources loadStrings() {
      ResourceLoader var0 = new ResourceLoader();
      return var0.loadStringsFromResourceBundle("org.etlforge.integration.client.ui.interchange.location.resources.Strings");
   }

   private LocationResources() {
   }
}
