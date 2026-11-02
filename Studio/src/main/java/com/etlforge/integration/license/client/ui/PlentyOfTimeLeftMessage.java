package com.etlforge.integration.license.client.ui;

import com.etlforge.integration.license.LicenseInfo;
import org.etlforge.application.ui.look.AlertIcons;
import org.etlforge.ui.layout.OneColumnPanel;

final class PlentyOfTimeLeftMessage extends LicenseMessage {
   public PlentyOfTimeLeftMessage(LicenseInfo var1) {
      super(var1, "License Info", AlertIcons.INFO);
      assert !Utils.isExpirationDateWithinWarningTreshold(var1);
      assert !var1.hasExpired();
   }

   @Override
   protected String getMessage() {
      return "You still have " + this.getInfo().getRemainingDays() + " days left on the license.";
   }

   @Override
   protected void layoutMessage(OneColumnPanel var1) {
      var1.add(this.getMessage());
      this.layoutLicenseDetails(var1);
   }
}
