package org.etlforge.integration.client.ui.de.resources;

import org.etlforge.application.ui.resources.ResourceLoader;
import org.etlforge.ui.StringResources;

public final class DataElementResources {
   public static final StringResources STRINGS = loadStrings();

   public static String getString(String var0) {
      return STRINGS.getString(var0);
   }

   private static StringResources loadStrings() {
      ResourceLoader var0 = new ResourceLoader();
      return var0.loadStringsFromResourceBundle("org.etlforge.integration.client.ui.de.resources.Strings");
   }

   private DataElementResources() {
   }
}
