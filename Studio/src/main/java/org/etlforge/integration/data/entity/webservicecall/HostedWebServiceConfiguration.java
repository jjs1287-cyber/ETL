package org.etlforge.integration.data.entity.webservicecall;

import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.id.OperationId;

public final class HostedWebServiceConfiguration {
   public static final String URL_MARKER = "http://[server name]";

   public static String getWebServiceUrl(Operation var0) {
      return "http://[server name]/etlforge_ws/?id=" + var0.getID();
   }

   public static String getWebServiceUrl(String var0, String var1, OperationId var2) {
      String var3 = "http://";
      if (StringUtils.isEmpty(var0)) {
         var3 = var3 + "[server name]";
      } else {
         var3 = var3 + var0;
      }

      if (StringUtils.isNotEmpty(var1)) {
         var3 = var3 + ":" + var1;
      }

      return var3 + "/etlforge_ws/?id=" + var2;
   }

   private HostedWebServiceConfiguration() {
   }
}
