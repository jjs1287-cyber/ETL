package com.etlforge.integration.server.implementation.webservice.license;

import com.etlforge.integration.server.license.MaskedServerGuidKeyBaseGenerator;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.integration.server.ExternalApiSupport;
import org.etlforge.integration.server.ServerGuidLookup;
import org.etlforge.integration.server.db.ServerGuidDatabaseLookup;
import org.etlforge.integration.server.exception.ServerConfigurationException;

final class GetLicenseKeyGenerationInfoTask extends LicenseTask {
   public GetLicenseKeyGenerationInfoTask(String var1, String var2) {
      super("getLicenseKeyGenerationInfo", var1, var2);
   }

   @Override
   protected WsKeyValue[] runTask(ExternalApiSupport var1) throws Exception {
      ServerGuidDatabaseLookup var2 = new ServerGuidDatabaseLookup(var1.getTranDbConnection());
      MaskedServerGuidKeyBaseGenerator var3 = new MaskedServerGuidKeyBaseGenerator(var2);
      String var4 = var3.getKeyBase();
      WsKeyValue var5 = new WsKeyValue("installation-id", var4);
      WsKeyValue var6 = this.createServerGuidKeyValue(var2);
      return new WsKeyValue[]{var5, var6};
   }

   private WsKeyValue createServerGuidKeyValue(ServerGuidLookup var1) throws ServerConfigurationException {
      ServerGuid var2 = var1.lookupGuid();
      return new WsKeyValue("server-guid", var2.toString());
   }
}
