package com.etlforge.integration.server.license;

import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.common.server.ServerEdition;
import org.etlforge.integration.common.server.ServerEditionLookup;
import org.etlforge.integration.common.server.license.StandardOptionsParser;

public final class LicenseBasedServerEditionLookup implements ServerEditionLookup {
   private final LicenseKeyStore licenseKeyStore;
   private final LicenseChecker licenseChecker;
   private final String nodeName;

   public LicenseBasedServerEditionLookup(LicenseKeyStore var1, LicenseChecker var2, String var3) {
      this.licenseKeyStore = var1;
      this.licenseChecker = var2;
      this.nodeName = var3;
   }

   @Override
   public ServerEdition getServerEdition() throws Exception {
      LicenseInfo var1 = this.checkLicense();
      StandardOptionsParser var2 = this.parseOptions(var1);
      return var2.getServerEdition();
   }

   private LicenseInfo checkLicense() throws ServerLicenseException {
      String var1 = this.licenseKeyStore.readLicenseKey();
      if (StringUtils.isEmpty(var1)) {
         throw new ServerLicenseException("The ETLForge server does not have a license key. Please contact ETLForge support.");
      } else {
         return this.licenseChecker.checkLicenseKey(var1, this.nodeName);
      }
   }

   private StandardOptionsParser parseOptions(LicenseInfo var1) {
      String var2 = var1.getOptions();
      StandardOptionsParser var3 = new StandardOptionsParser();
      var3.parse(var2);
      return var3;
   }
}
