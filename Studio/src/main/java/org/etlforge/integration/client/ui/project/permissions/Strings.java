package org.etlforge.integration.client.ui.project.permissions;

import java.text.MessageFormat;
import org.etlforge.integration.client.ui.project.permissions.resources.PermissionsUiResources;

final class Strings {
   static String get(String var0) {
      return PermissionsUiResources.STRINGS.getString(var0);
   }

   static String format(String var0, Object... var1) {
      return MessageFormat.format(get(var0), var1);
   }

   private Strings() {
      throw new UnsupportedOperationException();
   }
}
