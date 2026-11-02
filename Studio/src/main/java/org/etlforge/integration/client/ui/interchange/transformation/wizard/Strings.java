package org.etlforge.integration.client.ui.interchange.transformation.wizard;

import org.etlforge.integration.client.ui.interchange.transformation.wizard.resources.TransWizResources;
import org.etlforge.ui.MnemonicString;

final class Strings {
   static String get(String var0) {
      return TransWizResources.getString(var0);
   }

   static MnemonicString mnemonic(String var0) {
      return TransWizResources.STRINGS.getMnemonicString(var0);
   }

   private Strings() {
   }
}
