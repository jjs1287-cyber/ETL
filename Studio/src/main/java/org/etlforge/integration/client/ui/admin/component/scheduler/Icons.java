package org.etlforge.integration.client.ui.admin.component.scheduler;

import javax.swing.Icon;
import org.etlforge.integration.client.ui.admin.resources.AdminViewResources;

final class Icons {
   static Icon get(String var0) {
      return AdminViewResources.ICONS.getIcon(var0);
   }

   private Icons() {
      throw new UnsupportedOperationException();
   }
}
