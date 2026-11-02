package com.etlforge.integration.license.client.ui;

import com.etlforge.integration.license.LicenseInfo;
import com.etlforge.integration.license.LicenseMessenger;
import com.etlforge.integration.license.client.ui.prefs.ShowTrialInfoOnLoginPreference;
import java.awt.EventQueue;
import javax.swing.Action;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.ui.wait.WaitService;

public final class LicenseDialogMessenger implements LicenseMessenger {
   private final ServerManager serverManager;
   private final WaitService waitService;
   private volatile Action configureMessagePrefsAction;

   public LicenseDialogMessenger(ServerManager var1, WaitService var2) {
      this.serverManager = var1;
      this.waitService = var2;
   }

   public void setConfigureMessagePrefsAction(Action var1) {
      this.configureMessagePrefsAction = var1;
   }

   @Override
   public void showCommercialLicenseAlmostExpiredMessage(final LicenseInfo var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ExpiringLicenseMessage var1x = new ExpiringLicenseMessage(var1);
            var1x.setPreferencesAction(LicenseDialogMessenger.this.configureMessagePrefsAction);
            LicenseDialogMessenger.this.showMessage(var1x);
         }
      });
   }

   private void showMessage(final LicenseMessage var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            RequestNewLicenseAction var1x = new RequestNewLicenseAction(LicenseDialogMessenger.this.serverManager, LicenseDialogMessenger.this.waitService);
            var1.setRequestNewLicenseAction(var1x);
            LicenseDialogMessenger.this.openDialog(var1, var1x);
         }
      });
   }

   private void openDialog(LicenseMessage var1, RequestNewLicenseAction var2) {
      LicenseMessageDialog var3 = new LicenseMessageDialog(var1);
      var3.setRequestNewLicenseAction(var2);
      var3.open();
   }

   @Override
   public void showLicenseExpiredMessage(final LicenseInfo var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            LicenseExpiredMessage var1x = new LicenseExpiredMessage(var1);
            LicenseDialogMessenger.this.showMessage(var1x);
         }
      });
   }

   @Override
   public void showTrialLicenseMessage(final LicenseInfo var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            TrialLicenseMessage var1x = new TrialLicenseMessage(var1, ShowTrialInfoOnLoginPreference.INSTANCE);
            LicenseDialogMessenger.this.showMessage(var1x);
         }
      });
   }
}
