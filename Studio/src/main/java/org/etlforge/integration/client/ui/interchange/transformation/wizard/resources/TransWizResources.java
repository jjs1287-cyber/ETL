package org.etlforge.integration.client.ui.interchange.transformation.wizard.resources;

import org.etlforge.application.ui.resources.ResourceLoader;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.StringResources;

public final class TransWizResources {
   public static final StringResources STRINGS = new ResourceLoader()
      .loadStringsFromResourceBundle("org.etlforge.integration.client.ui.interchange.transformation.wizard.resources.Strings");

   public static String getString(String var0) {
      return STRINGS.getString(var0);
   }

   public static MnemonicString getMnemonicString(String var0) {
      return STRINGS.getMnemonicString(var0);
   }

   private TransWizResources() {
      throw new UnsupportedOperationException();
   }
}
