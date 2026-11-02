package com.etlforge.integration.license.client.ui.actions;

import com.etlforge.integration.license.server.ActivateLicenseCallback;
import com.etlforge.integration.license.server.LicenseService;
import java.awt.event.ActionEvent;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.BusyWorker;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.server.login.InvalidCredentialsException;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.ui.Size;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.ui.widget.text.KongaTextArea;

public final class ActivateLicenseAction extends AbstractApplicationAction {
   private final ServerManager serverManager;
   private final LicenseService licenseService;
   private final WaitService waitService;

   public ActivateLicenseAction(ServerManager var1, LicenseService var2, WaitService var3) {
      super("Activate.License", "&Activate License", ClientIcons.LICENSE_16);
      this.serverManager = var1;
      this.licenseService = var2;
      this.waitService = var3;
      this.putValue("ShortDescription", "Upload and activate a license key that you have received from ETLForge.");
   }

   @Override
   public void execute(ActionEvent var1) {
      BusyWorker var2 = new BusyWorker(this.waitService, new Runnable() {
         @Override
         public void run() {
            ActivateLicenseAction.this.promptUserForLicenseKey();
         }
      });
      var2.run();
   }

   private void promptUserForLicenseKey() {
      LicenseKeyEntryUi var1 = new LicenseKeyEntryUi();

      try {
         WizardStyleDialog var2 = this.createDialog(var1);
         var2.setVisible(true);
         if (var2.wasOkPressed()) {
            String var3 = var1.getLicenseKey();
            if (StringUtils.isNotEmpty(var3)) {
               this.uploadLicenseKey(var3);
            }
         }
      } finally {
         var1.dispose();
      }
   }

   private WizardStyleDialog createDialog(LicenseKeyEntryUi var1) {
      WizardStyleDialog var2 = new WizardStyleDialog(UiUtils.getActiveWindow(), "Upload License Key", var1, KongaDialog.CONTINUE_CANCEL, true);
      var2.setContinueButtonAsDefault();
      var2.setLocationRelativeTo(null);
      return var2;
   }

   private void uploadLicenseKey(String var1) {
      ActivateLicenseAction.ActivationJob var2 = new ActivateLicenseAction.ActivationJob(this.serverManager, this.licenseService, var1);
      var2.submit(this.waitService, 0);
   }

   private static class ActivationJob extends UiJob {
      private final ServerManager serverManager;
      private final LicenseService licenseService;
      private final String licenseKey;
      private final ActivateLicenseAction.ActivationJob.Callback callback = new ActivateLicenseAction.ActivationJob.Callback();

      public ActivationJob(ServerManager var1, LicenseService var2, String var3) {
         super("Upload License Key To Server");
         this.serverManager = var1;
         this.licenseService = var2;
         this.licenseKey = var3;
         this.setBlockUntilDone(true);
         this.setUncaughtExceptionHandler(this.callback);
      }

      @Override
      protected void runImpl() throws InterruptedException {
         ActivateLicenseCall var1 = new ActivateLicenseCall(this.licenseService, this.serverManager);
         var1.makeCall(this.licenseKey, this.callback);
      }

      private static class Callback implements ActivateLicenseCallback {
         private Callback() {
         }

         @Override
         public void licenseWasActivated(ServerInfo var1) {
            NonBlockingAlert.info("The license has been activated.", "License Activated");
         }

         @Override
         public void activationError(String var1) {
            KongaTextArea var2 = KongaTextArea.viewer(var1, Size.rows(10), Size.columns(60));
            NonBlockingAlert.message(ApplicationUi.getAppWinFrame(), var2, "License Activation Error", Alert.Type.ERROR);
         }

         @Override
         public void invalidCredentials(InvalidCredentialsException var1) {
            NonBlockingAlert.error(ApplicationUi.getAppWinFrame(), var1.getMessage() + "\n\nThe license was not activated.", "Invalid credentials");
         }

         @Override
         public void caught(Throwable var1) {
            ErrorLog.attention(LicenseService.class, null, var1);
         }

         @Override
         public void cancelled() {
         }
      }
   }
}
