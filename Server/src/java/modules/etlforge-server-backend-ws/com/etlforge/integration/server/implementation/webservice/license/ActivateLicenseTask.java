package com.etlforge.integration.server.implementation.webservice.license;

import org.etlforge.integration.server.ExternalApiSupport;

final class ActivateLicenseTask extends LicenseTask {
   private final String compressedLicenseKey;

   public ActivateLicenseTask(String var1, String var2, String var3) {
      super("activateLicense", var1, var2);
      this.compressedLicenseKey = var3;
   }

   @Override
   protected WsKeyValue[] runTask(ExternalApiSupport var1) throws Exception {
      LicenseActivatorWsImpl var2 = new LicenseActivatorWsImpl(var1.getTranDbConnection());
      var2.activate(this.compressedLicenseKey);
      return new WsKeyValue[]{var2.getServerGuid(), var2.getError()};
   }
}
