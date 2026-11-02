package com.etlforge.integration.license.client.ui;

import com.etlforge.integration.license.LicenseInfo;
import org.etlforge.application.ui.look.AlertIcons;
import org.etlforge.ui.layout.OneColumnPanel;

final class ExpiringLicenseMessage extends LicenseMessage {
   public ExpiringLicenseMessage(LicenseInfo var1) {
      super(var1, "License About To Expire", AlertIcons.WARNING);
      assert Utils.isExpirationDateWithinWarningTreshold(var1);
      assert !var1.hasExpired();
   }

   @Override
   protected void layoutMessage(OneColumnPanel var1) {
      var1.add(this.getMessage());
      this.layoutLicenseDetails(var1);
      this.layoutRequestNewLicenseBox(var1);
   }

   @Override
   protected String getMessage() {
      int var1 = this.getInfo().getRemainingDays();
      switch (var1) {
         case 0:
            return "The ETLForge server license will expire today.";
         case 1:
            return "The ETLForge server license will expire tomorrow.";
         default:
            return "The ETLForge server license will expire in " + var1 + " days.";
      }
   }
}
