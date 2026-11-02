package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.resources;

import java.text.MessageFormat;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.resources.ResourceLoader;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.StringResources;

public final class TreeStrings {
   private static final StringResources STRINGS = load();

   public static String get(String var0) {
      return STRINGS.getString(var0);
   }

   public static String format(String var0, Object... var1) {
      return MessageFormat.format(get(var0), var1);
   }

   public static MnemonicString mnemonic(String var0) {
      return ApplicationResources.MNEMONIC_FACTORY.fromString(get(var0));
   }

   private static StringResources load() {
      ResourceLoader var0 = new ResourceLoader();
      return var0.loadStringsFromResourceBundle("org.etlforge.integration.client.ui.interchange.entity.transformation.tree.resources.Strings");
   }

   private TreeStrings() {
   }
}
