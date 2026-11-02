package org.etlforge.integration.client.server.ui.user;

import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.user.Role;

public final class RoleUiUtils {
   public static String getDisplayName(Role var0) {
      String var1 = var0.getName();
      if (StringUtils.isBlank(var1)) {
         var1 = "[name missing]";
      }

      return var1;
   }

   private RoleUiUtils() {
   }
}
