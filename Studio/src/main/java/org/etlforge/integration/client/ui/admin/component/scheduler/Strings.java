package org.etlforge.integration.client.ui.admin.component.scheduler;

import org.etlforge.integration.client.ui.admin.resources.AdminViewResources;

final class Strings {
   static String get(String var0) {
      return AdminViewResources.STRINGS.getString(var0);
   }

   private Strings() {
      throw new UnsupportedOperationException();
   }
}
