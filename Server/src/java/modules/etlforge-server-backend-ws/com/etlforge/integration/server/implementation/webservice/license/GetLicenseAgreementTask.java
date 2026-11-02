package com.etlforge.integration.server.implementation.webservice.license;

import org.etlforge.integration.server.ExternalApiSupport;

final class GetLicenseAgreementTask extends LicenseTask {
   private final String licenseKey;

   public GetLicenseAgreementTask(String var1, String var2, String var3) {
      super("getLicenseAgreement", var1, var2);
      this.licenseKey = var3;
   }

   @Override
   protected WsKeyValue[] runTask(ExternalApiSupport var1) throws Exception {
      AgreementLocatorWsImpl var2 = new AgreementLocatorWsImpl(var1.getTranDbConnection());
      return var2.locateAndZipLicense(this.licenseKey);
   }
}
