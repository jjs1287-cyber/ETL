package org.etlforge.integration.client.ui.entity.testhistory.resources;

import java.text.MessageFormat;
import org.etlforge.application.ui.resources.ResourceLoader;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.StringResources;

public final class TestHistoryResources {
   public static final StringResources STRINGS = new ResourceLoader()
      .loadStringsFromResourceBundle("org.etlforge.integration.client.ui.entity.testhistory.resources.Strings");

   public static String getString(String var0) {
      return STRINGS.getString(var0);
   }

   public static String format(String var0, Object... var1) {
      return MessageFormat.format(getString(var0), var1);
   }

   public static MnemonicString getMnemonicString(String var0) {
      return STRINGS.getMnemonicString(var0);
   }

   private TestHistoryResources() {
   }
}
