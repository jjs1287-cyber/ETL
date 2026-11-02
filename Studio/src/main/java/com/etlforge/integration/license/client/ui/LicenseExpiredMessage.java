package com.etlforge.integration.license.client.ui;

import com.google.common.base.Preconditions;
import com.etlforge.integration.license.LicenseInfo;
import org.etlforge.application.ui.look.AlertIcons;
import org.etlforge.ui.layout.OneColumnPanel;

final class LicenseExpiredMessage extends LicenseMessage {
   public LicenseExpiredMessage(LicenseInfo var1) {
      super(var1, "License Expired", AlertIcons.ERROR);
      Preconditions.checkArgument(var1.hasExpired(), "info does not represent an expired license");
   }

   @Override
   protected String getMessage() {
      return "The license on the ETLForge server has expired. The server does not accept new connections.";
   }

   @Override
   protected void layoutMessage(OneColumnPanel var1) {
      var1.add(new CaptionArea(this.getMessage()));
      this.layoutLicenseDetails(var1);
      this.layoutRequestNewLicenseBox(var1);
   }
}
