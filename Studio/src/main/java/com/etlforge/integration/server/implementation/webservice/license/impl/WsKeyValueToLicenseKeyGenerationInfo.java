package com.etlforge.integration.server.implementation.webservice.license.impl;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.etlforge.integration.license.server.LicenseKeyGenerationInfo;
import com.etlforge.integration.server.implementation.webservice.license.client.WsKeyValue;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.client.server.ServerInfo;

final class WsKeyValueToLicenseKeyGenerationInfo implements Function<WsKeyValue[], LicenseKeyGenerationInfo> {
   private final ServerInfo serverInfo;

   public WsKeyValueToLicenseKeyGenerationInfo(ServerInfo var1) {
      this.serverInfo = var1;
   }

   public LicenseKeyGenerationInfo apply(WsKeyValue[] var1) {
      return new WsKeyValueToLicenseKeyGenerationInfo.Info(this.serverInfo, var1);
   }

   private static class Info implements LicenseKeyGenerationInfo {
      private final ServerInfo serverInfo;
      private final String installationId;

      public Info(ServerInfo var1, WsKeyValue[] var2) {
         this.serverInfo = var1;
         Map var3 = this.mapify(var2);
         this.installationId = this.lookupInstallationId(var3);
      }

      @Override
      public String getServerInstallationId() {
         return this.installationId;
      }

      @Override
      public ServerInfo getServerInfo() {
         return this.serverInfo;
      }

      private Map<String, String> mapify(WsKeyValue[] var1) {
         HashMap var2 = Maps.newHashMap();

         for (WsKeyValue var6 : var1) {
            String var7 = StringUtils.lowerCase(var6.getKey());
            var2.put(var7, var6.getValue());
         }

         return var2;
      }

      private String lookupInstallationId(Map<String, String> var1) {
         String var2 = (String)var1.get("installation-id");
         return var2 != null ? var2 : "";
      }
   }
}
