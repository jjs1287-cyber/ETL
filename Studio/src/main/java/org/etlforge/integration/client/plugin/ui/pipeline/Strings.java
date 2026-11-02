package org.etlforge.integration.client.plugin.ui.pipeline;

import java.text.MessageFormat;
import org.etlforge.integration.client.plugin.ui.resources.PluginUiResources;

final class Strings {
   static String get(String var0) {
      return PluginUiResources.STRINGS.getString(var0);
   }

   static String format(String var0, Object... var1) {
      return MessageFormat.format(get(var0), var1);
   }

   private Strings() {
      throw new UnsupportedOperationException();
   }
}
