package com.etlforge.integration.license.client.ui;

import com.google.common.base.Preconditions;
import com.etlforge.integration.license.LicenseInfo;
import org.etlforge.application.ui.look.AlertIcons;
import org.etlforge.ui.layout.OneColumnPanel;

final class PerpetualLicenseMessage extends LicenseMessage {
   public PerpetualLicenseMessage(LicenseInfo var1) {
      super(var1, "Perpetual License", AlertIcons.INFO);
      Preconditions.checkArgument(var1.isPerpetual(), "info does not represent a perpetual license");
   }

   @Override
   protected void layoutMessage(OneColumnPanel var1) {
      var1.add(this.getMessage());
      this.layoutLicenseDetails(var1);
   }

   @Override
   protected String getMessage() {
      return "The ETLForge server is running on a perpetual license that never expires.";
   }
}
