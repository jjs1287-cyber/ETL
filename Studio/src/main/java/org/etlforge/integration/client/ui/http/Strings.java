package org.etlforge.integration.client.ui.http;

import org.etlforge.integration.client.ui.http.resources.HttpUiResources;

final class Strings {
   static String get(String var0) {
      return HttpUiResources.STRINGS.getString(var0);
   }

   private Strings() {
   }
}
