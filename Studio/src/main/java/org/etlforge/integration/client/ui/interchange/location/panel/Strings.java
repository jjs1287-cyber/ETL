package org.etlforge.integration.client.ui.interchange.location.panel;

import java.text.MessageFormat;
import org.etlforge.integration.client.ui.interchange.location.resources.LocationResources;

final class Strings {
   static String get(String var0) {
      return LocationResources.getString(var0);
   }

   static String format(String var0, Object... var1) {
      return MessageFormat.format(get(var0), var1);
   }

   private Strings() {
   }
}
