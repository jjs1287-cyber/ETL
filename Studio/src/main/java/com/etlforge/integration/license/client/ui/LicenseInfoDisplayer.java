package com.etlforge.integration.license.client.ui;

import com.google.common.base.Preconditions;
import com.etlforge.integration.license.LicenseInfo;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.ui.Alert;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.server.login.LoginCredentials;
import org.etlforge.ui.wait.WaitService;

public final class LicenseInfoDisplayer {
   private final LicenseInfo info;
   private final RequestNewLicenseAction newLicenseRequest;

   public LicenseInfoDisplayer(ServerManager var1, LicenseInfo var2, WaitService var3) {
      Preconditions.checkNotNull(var2, "info");
      this.info = var2;
      this.newLicenseRequest = new RequestNewLicenseAction(var1, var3);
   }

   public void setServerCredentials(LoginCredentials var1) {
      this.newLicenseRequest.setServerCredentials(var1);
   }

   public void display() {
      LicenseMessage var1 = this.createUi();
      if (var1 != null) {
         this.openDialog(var1);
      }
   }

   public LicenseMessage createUi() {
      LicenseMessage var1 = null;
      boolean var2 = true;
      if (this.info.hasExpired()) {
         var1 = new LicenseExpiredMessage(this.info);
      } else if (this.info.isTrial()) {
         var1 = new TrialLicenseMessage(this.info);
      } else if (StringUtils.isNotEmpty(this.info.getExpirationDate())) {
         boolean var3 = Utils.isExpirationDateWithinWarningTreshold(this.info);
         if (var3) {
            var1 = new ExpiringLicenseMessage(this.info);
         } else {
            var1 = new PlentyOfTimeLeftMessage(this.info);
            var2 = false;
         }
      } else {
         if (!this.info.isPerpetual()) {
            Alert.warning(
               "The server returned information that confused the client. Please contact ETLForge support for questions about your license.",
               "No License Info Available"
            );
            return null;
         }

         var1 = new PerpetualLicenseMessage(this.info);
         var2 = false;
      }

      if (var2) {
         var1.setRequestNewLicenseAction(this.newLicenseRequest);
      }

      return var1;
   }

   private void openDialog(LicenseMessage var1) {
      LicenseMessageDialog var2 = new LicenseMessageDialog(var1);
      var2.setRequestNewLicenseAction(this.newLicenseRequest);
      var2.setPreferenceKey(LicenseInfoDisplayer.class);
      var2.open();
   }
}
