package org.etlforge.integration.client.ui.interchange.component.launcher;

import java.text.MessageFormat;
import org.etlforge.integration.client.ui.interchange.resources.InterchangeResources;
import org.etlforge.ui.MnemonicString;

final class Strings {
   static String get(String var0) {
      return InterchangeResources.STRINGS.getString(var0);
   }

   static MnemonicString mnemonicString(String var0) {
      return InterchangeResources.STRINGS.getMnemonicString(var0);
   }

   static String format(String var0, Object... var1) {
      return MessageFormat.format(get(var0), var1);
   }

   private Strings() {
   }
}
