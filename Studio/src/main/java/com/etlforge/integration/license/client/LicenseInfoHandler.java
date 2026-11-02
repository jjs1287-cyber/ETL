package com.etlforge.integration.license.client;

import com.google.common.base.Preconditions;
import com.etlforge.integration.license.ExpiringLicenseMessagePolicy;
import com.etlforge.integration.license.LicenseInfo;
import com.etlforge.integration.license.LicenseMessenger;
import java.util.Map;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.ServerLicenseException;
import org.etlforge.integration.client.server.login.LoginKeyValuesHandler;

public final class LicenseInfoHandler implements LoginKeyValuesHandler {
   private final LicenseMessenger messenger;
   private ExpiringLicenseMessagePolicy aboutToExpirePolicy;

   public LicenseInfoHandler(LicenseMessenger var1) {
      this.messenger = Preconditions.checkNotNull(var1, "messenger");
      this.aboutToExpirePolicy = ExpiringLicenseMessagePolicy.NEVER_SHOW_MESSAGE;
   }

   @Override
   public void handle(ServerInfo var1, Map<String, String> var2) throws IntegrationServerException {
      LicenseInfo var3 = new LicenseInfo(var1, var2);
      this.displayLicenseInfo(var3);
   }

   private void displayLicenseInfo(LicenseInfo var1) throws ServerLicenseException {
      if (!var1.isPerpetual()) {
         if (var1.hasExpired()) {
            this.messenger.showLicenseExpiredMessage(var1);
            throw new ServerLicenseException("The trial period has expired.");
         }

         if (var1.isTrial()) {
            if (this.aboutToExpirePolicy.shouldMessageBeShown(var1)) {
               this.messenger.showTrialLicenseMessage(var1);
            }
         } else if (this.aboutToExpirePolicy.shouldMessageBeShown(var1)) {
            this.messenger.showCommercialLicenseAlmostExpiredMessage(var1);
         }
      }
   }

   public void setExpiringLicenseMessagePolicy(ExpiringLicenseMessagePolicy var1) {
      this.aboutToExpirePolicy = var1;
   }
}
