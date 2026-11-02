package org.etlforge.integration.project.upgrade.v_5_0_0_60;

import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.data.CC;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.data.project.IntegrationProject;

final class SalesforceOrgUpgrade {
   public static void run(IntegrationProject var0) {
      for (SalesforceConnector var3 : var0.getAllEntitiesOfClass(SalesforceConnector.class)) {
         upgrade(var3);
      }
   }

   private static void upgrade(SalesforceConnector var0) {
      synchronized (var0.getDataLock()) {
         boolean var2 = upgradeProperty(var0, "password");
         boolean var3 = upgradeProperty(var0, "security_token");
         if (var2 || var3) {
            var0.setDeployDirty(true);
         }
      }
   }

   private static boolean upgradeProperty(SalesforceConnector var0, String var1) {
      String var2 = var0.getProperty(var1);
      if (StringUtils.isEmpty(var2)) {
         return false;
      }

      String var3 = CC.hide(var2);
      var0.setProperty(var1, var3);
      return true;
   }

   private SalesforceOrgUpgrade() {
   }
}
