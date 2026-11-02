package org.etlforge.integration.client.ui.entity.selector.combo;

import org.etlforge.integration.client.ui.entity.resources.EntityResources;

final class Strings {
   static String get(String var0) {
      return EntityResources.STRINGS.getString(var0);
   }

   private Strings() {
      throw new UnsupportedOperationException();
   }
}
