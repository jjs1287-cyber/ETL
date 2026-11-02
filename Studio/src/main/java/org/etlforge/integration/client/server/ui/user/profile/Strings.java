package org.etlforge.integration.client.server.ui.user.profile;

import java.text.MessageFormat;
import org.etlforge.integration.client.server.ui.resources.ServerUiResources;

final class Strings {
   static String get(String var0) {
      return ServerUiResources.STRINGS.getString(var0);
   }

   static String format(String var0, Object... var1) {
      return MessageFormat.format(get(var0), var1);
   }

   private Strings() {
      throw new UnsupportedOperationException();
   }
}
