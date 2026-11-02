package org.etlforge.integration.client.ui.interchange.entity.webservice.wizard;

import java.text.MessageFormat;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.integration.client.ui.interchange.entity.webservice.resources.WebServiceResources;
import org.etlforge.ui.MnemonicString;

final class Strings {
   static String get(String var0) {
      return WebServiceResources.STRINGS.getString(var0);
   }

   static MnemonicString mnemonicString(String var0) {
      return ApplicationResources.MNEMONIC_FACTORY.fromString(get(var0));
   }

   static String format(String var0, Object... var1) {
      return MessageFormat.format(get(var0), var1);
   }

   private Strings() {
   }
}
