package org.etlforge.application.ui.search.string;

import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.ui.MnemonicString;

final class Strings {
   static String get(String var0) {
      return ApplicationResources.STRINGS.getString(var0);
   }

   static MnemonicString mnemonic(String var0) {
      return ApplicationResources.STRINGS.getMnemonicString(var0);
   }

   private Strings() {
      throw new UnsupportedOperationException();
   }
}
