package com.etlforge.integration.license.client.ui.actions;

import com.etlforge.integration.license.server.CheckLicenseCallback;
import com.etlforge.integration.license.server.LicenseService;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.server.login.LoginCredentials;

public final class CheckLicenseCall extends AbstractLicenseServiceCall {
   public CheckLicenseCall(LicenseService var1, ServerManager var2) {
      super(var1, var2);
   }

   public void makeCall(CheckLicenseCallback var1) {
      LoginCredentials var2 = this.getCredentials();
      if (var2 != null) {
         this.getLicenseService().checkLicense(var2, var1);
      } else {
         var1.cancelled();
      }
   }
}
