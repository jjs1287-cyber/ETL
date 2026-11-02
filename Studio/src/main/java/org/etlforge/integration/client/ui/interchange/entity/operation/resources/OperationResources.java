package org.etlforge.integration.client.ui.interchange.entity.operation.resources;

import org.etlforge.application.ui.resources.ResourceLoader;
import org.etlforge.ui.StringResources;

public final class OperationResources {
   public static final StringResources STRINGS = loadStrings();

   private static StringResources loadStrings() {
      ResourceLoader var0 = new ResourceLoader();
      String var1 = "org.etlforge.integration.client.ui.interchange.entity.operation.resources.Strings";
      return var0.loadStringsFromResourceBundle(var1);
   }

   public static String getString(String var0) {
      return STRINGS.getString(var0);
   }

   private OperationResources() {
   }
}
