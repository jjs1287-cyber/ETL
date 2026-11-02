package com.etlforge.integration.license.client.ui;

import com.google.common.base.Preconditions;
import com.etlforge.integration.license.LicenseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import org.etlforge.application.prefs.BooleanPreference;
import org.etlforge.application.ui.look.AlertIcons;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.widget.button.KongaCheckBox;

final class TrialLicenseMessage extends LicenseMessage {
   private final BooleanPreference showMessagePref;
   private final JCheckBox doNotShowAgainCheck;

   public TrialLicenseMessage(LicenseInfo var1) {
      this(var1, null);
   }

   public TrialLicenseMessage(LicenseInfo var1, BooleanPreference var2) {
      super(var1, "Trial License", AlertIcons.INFO);
      Preconditions.checkArgument(var1.isTrial(), "info does not represent a trial license");
      this.showMessagePref = var2;
      this.doNotShowAgainCheck = var2 != null ? this.createDoNotShowAgainCheck() : null;
   }

   private final JCheckBox createDoNotShowAgainCheck() {
      assert this.showMessagePref != null;
      KongaCheckBox var1 = new KongaCheckBox(ApplicationResources.STRINGS.getString("DoNotShowAgain"));
      var1.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            boolean var2 = ((JCheckBox)var1.getSource()).isSelected();
            TrialLicenseMessage.this.showMessagePref.set(!var2);
         }
      });
      return var1;
   }

   @Override
   protected void layoutMessage(OneColumnPanel var1) {
      var1.add(this.getMessage());
      this.layoutLicenseDetails(var1);
      this.layoutRequestNewLicenseBox(var1);
      this.layoutDoNotShowAgain(var1);
   }

   @Override
   protected String getMessage() {
      int var1 = this.getInfo().getRemainingDays();
      switch (var1) {
         case 0:
            return "The ETLForge server trial license will expire today.";
         case 1:
            return "The ETLForge server trial license will expire tomorrow.";
         default:
            return "The ETLForge server trial license will expire in " + var1 + " days.";
      }
   }

   private void layoutDoNotShowAgain(OneColumnPanel var1) {
      if (this.doNotShowAgainCheck != null) {
         var1.add(this.doNotShowAgainCheck);
      }
   }
}
