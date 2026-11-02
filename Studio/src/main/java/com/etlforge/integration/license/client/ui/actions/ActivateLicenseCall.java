package com.etlforge.integration.license.client.ui.actions;

import com.etlforge.integration.license.server.ActivateLicenseCallback;
import com.etlforge.integration.license.server.LicenseService;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.server.login.LoginCredentials;

final class ActivateLicenseCall extends AbstractLicenseServiceCall {
   public ActivateLicenseCall(LicenseService var1, ServerManager var2) {
      super(var1, var2);
   }

   public void makeCall(String var1, ActivateLicenseCallback var2) {
      LoginCredentials var3 = this.getCredentials();
      if (var3 != null && this.isLicenseAgreementAccepted(var1, var3)) {
         this.getLicenseService().activateLicense(var1, var3, var2);
      } else {
         var2.cancelled();
      }
   }

   private boolean isLicenseAgreementAccepted(String var1, LoginCredentials var2) {
      LicenseAgreementDialogQuestion var3 = new LicenseAgreementDialogQuestion();
      LicenseAgreementGate var4 = new LicenseAgreementGate(this.getLicenseService(), var3);
      LicenseAgreementAnswer var5 = var4.isAgreementAccepted(var1, var2);
      if (var5 == LicenseAgreementAnswer.DID_NOT_AGREE) {
         NonBlockingAlert.warning("The license was not activated.", "License Not Activated");
      }

      return var5 == LicenseAgreementAnswer.AGREED;
   }
}
