package com.etlforge.integration.license.client.ui.actions;

import com.etlforge.integration.license.server.GetLicenseKeyGenerationInfoCallback;
import com.etlforge.integration.license.server.LicenseKeyGenerationInfo;
import com.etlforge.integration.license.server.LicenseService;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.server.login.InvalidCredentialsException;
import org.etlforge.integration.client.server.login.LoginCredentials;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.ui.wait.WaitService;

public final class DownloadLicenseKeyGenerationInfoAction extends AbstractApplicationAction {
   private final ServerManager serverManager;
   private final LicenseService licenseService;
   private LoginCredentials serverCredentials;
   private final WaitService waitService;

   public DownloadLicenseKeyGenerationInfoAction(ServerManager var1, LicenseService var2, WaitService var3) {
      super("Get.License.Key.Generation.Info", "&Request New License", ClientIcons.getIcon("License.Request.16"));
      this.serverManager = var1;
      this.licenseService = var2;
      this.waitService = var3;
      this.putValue("ShortDescription", "Download the installation ID that is needed for creating a license key for your server.");
   }

   public void setServerCredentials(LoginCredentials var1) {
      this.serverCredentials = var1;
   }

   @Override
   public void execute(ActionEvent var1) {
      DownloadLicenseKeyGenerationInfoAction.TestJob var2 = new DownloadLicenseKeyGenerationInfoAction.TestJob(
         this.serverManager, this.licenseService, this.serverCredentials
      );
      var2.submit(this.waitService, 0);
   }

   private static class TestJob extends UiJob {
      private final ServerManager serverManager;
      private final LicenseService licenseService;
      private final LoginCredentials serverCredentials;
      private final DownloadLicenseKeyGenerationInfoAction.TestJob.Callback callback = new DownloadLicenseKeyGenerationInfoAction.TestJob.Callback();

      public TestJob(ServerManager var1, LicenseService var2, LoginCredentials var3) {
         super("Get License Key Info");
         this.serverManager = var1;
         this.licenseService = var2;
         this.serverCredentials = var3;
         this.setBlockUntilDone(true);
         this.setUncaughtExceptionHandler(this.callback);
      }

      @Override
      protected void runImpl() throws InterruptedException {
         DownloadLicenseKeyGenerationInfoCall var1 = new DownloadLicenseKeyGenerationInfoCall(this.licenseService, this.serverManager);
         var1.setServerCredentials(this.serverCredentials);
         var1.makeCall(this.callback);
      }

      private static class Callback implements GetLicenseKeyGenerationInfoCallback {
         private Callback() {
         }

         @Override
         public void licenseKeyGenerationInfoReceived(final LicenseKeyGenerationInfo var1) {
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  InstallationIdViewer var1x = new InstallationIdViewer(var1.getServerInstallationId());
                  NonBlockingAlert.message(null, var1x, "License Request Instructions", Alert.Type.INFORMATION);
               }
            });
         }

         @Override
         public void invalidCredentials(InvalidCredentialsException var1) {
            NonBlockingAlert.error(ApplicationUi.getAppWinFrame(), var1.getMessage(), "Invalid credentials");
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
