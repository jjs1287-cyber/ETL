package com.etlforge.integration.client.ui.interchange.resources;

import java.text.MessageFormat;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.resources.ResourceLoader;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.StringResources;

public final class EnterpriseInterchangeResources {
   private static ResourceLoader loader = new ResourceLoader();
   public static final StringResources STRINGS;

   public static String getString(String var0) {
      return STRINGS.getString(var0);
   }

   public static String getFormattedMessage(String var0, Object... var1) {
      return MessageFormat.format(getString(var0), var1);
   }

   public static MnemonicString getMnemonicString(String var0) {
      return ApplicationResources.MNEMONIC_FACTORY.fromString(getString(var0));
   }

   private EnterpriseInterchangeResources() {
   }

   static {
      STRINGS = loader.loadStringsFromResourceBundle("com.etlforge.integration.client.ui.interchange.resources.Strings");
      loader = null;
   }
}
