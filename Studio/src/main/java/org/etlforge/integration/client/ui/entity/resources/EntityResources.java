package org.etlforge.integration.client.ui.entity.resources;

import java.text.MessageFormat;
import org.etlforge.application.ui.resources.ResourceLoader;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.StringResources;

public final class EntityResources {
   public static final StringResources STRINGS = loadStrings();

   private static StringResources loadStrings() {
      ResourceLoader var0 = new ResourceLoader();
      return var0.loadStringsFromResourceBundle("org.etlforge.integration.client.ui.entity.resources.Strings");
   }

   public static String getString(String var0) {
      return STRINGS.getString(var0);
   }

   public static String format(String var0, Object... var1) {
      return MessageFormat.format(getString(var0), var1);
   }

   public static MnemonicString getMnemonicString(String var0) {
      return STRINGS.getMnemonicString(var0);
   }

   private EntityResources() {
   }
}
