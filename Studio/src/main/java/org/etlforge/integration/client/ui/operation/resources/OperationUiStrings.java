package org.etlforge.integration.client.ui.operation.resources;

import java.text.MessageFormat;
import org.etlforge.application.ui.resources.ResourceLoader;
import org.etlforge.ui.StringResources;

public final class OperationUiStrings {
   public static final StringResources STRINGS = load();

   private static StringResources load() {
      ResourceLoader var0 = new ResourceLoader();
      return var0.loadStringsFromResourceBundle("org.etlforge.integration.client.ui.operation.resources.Strings");
   }

   public static String get(String var0) {
      return STRINGS.getString(var0);
   }

   public static String format(String var0, Object... var1) {
      return MessageFormat.format(get(var0), var1);
   }

   private OperationUiStrings() {
      throw new UnsupportedOperationException();
   }
}
