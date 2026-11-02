package com.etlforge.integration.license.client.ui.actions;

import com.etlforge.integration.license.server.GetLicenseKeyGenerationInfoCallback;
import com.etlforge.integration.license.server.LicenseService;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.server.login.LoginCredentials;

final class DownloadLicenseKeyGenerationInfoCall extends AbstractLicenseServiceCall {
   public DownloadLicenseKeyGenerationInfoCall(LicenseService var1, ServerManager var2) {
      super(var1, var2);
   }

   public void makeCall(GetLicenseKeyGenerationInfoCallback var1) {
      LoginCredentials var2 = this.getCredentials();
      if (var2 != null) {
         this.getLicenseService().getLicenseKeyGenerationInfo(var2, var1);
      } else {
         var1.cancelled();
      }
   }
}
