package com.etlforge.integration.server.license;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import org.etlforge.integration.common.server.license.StandardOptionsParser;
import org.etlforge.integration.server.exception.ServerConfigurationException;

public final class LicenseAgreementLocator {
   private final LicenseChecker checker;
   private final LicenseAgreements agreements;

   public LicenseAgreementLocator(LicenseChecker var1, LicenseAgreements var2) {
      Preconditions.checkNotNull(var1, "checker");
      Preconditions.checkNotNull(var2, "agreements");
      this.checker = var1;
      this.agreements = var2;
   }

   public InputStream locateLicenseAgreement(String var1, String var2) throws ServerLicenseException, ServerConfigurationException, IOException {
      LicenseInfo var3 = this.checker.checkLicenseKey(var1, var2);
      String var4 = var3.getOptions();
      StandardOptionsParser var5 = new StandardOptionsParser();
      var5.parse(var4);
      if (var5.isTrial()) {
         return this.agreements.getEvaluationLicense();
      } else {
         return var3.getExpirationDate() == null ? this.agreements.getPerpetualLicense() : this.agreements.getSubscriptionLicense();
      }
   }
}
