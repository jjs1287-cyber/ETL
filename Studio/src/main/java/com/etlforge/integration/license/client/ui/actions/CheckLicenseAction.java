package com.etlforge.integration.license.client.ui.actions;

import com.etlforge.integration.license.LicenseInfo;
import com.etlforge.integration.license.client.ui.LicenseInfoDisplayer;
import com.etlforge.integration.license.server.CheckLicenseCallback;
import com.etlforge.integration.license.server.LicenseService;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.server.login.LoginCredentials;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.ui.wait.WaitService;

public final class CheckLicenseAction extends AbstractApplicationAction {
   private final ServerManager serverManager;
   private final LicenseService licenseService;
   private final WaitService waitService;

   public CheckLicenseAction(ServerManager var1, LicenseService var2, WaitService var3) {
      super("Check.License", "&Check License", ClientIcons.getIcon("License.Check.16"));
      this.serverManager = var1;
      this.licenseService = var2;
      this.waitService = var3;
      this.putValue("ShortDescription", "Check the license that is currently installed on a ETLForge server.");
   }

   @Override
   public void execute(ActionEvent var1) {
      CheckLicenseAction.CheckLicenseJob var2 = new CheckLicenseAction.CheckLicenseJob(this.serverManager, this.licenseService, this.waitService);
      var2.submit(this.waitService, 0);
   }

   private static class CheckLicenseJob extends UiJob {
      private final ServerManager serverManager;
      private final LicenseService licenseService;
      private final WaitService waitService;
      private final CheckLicenseAction.CheckLicenseJob.Callback callback = new CheckLicenseAction.CheckLicenseJob.Callback();

      public CheckLicenseJob(ServerManager var1, LicenseService var2, WaitService var3) {
         super("Check License");
         this.serverManager = var1;
         this.licenseService = var2;
         this.waitService = var3;
         this.setBlockUntilDone(true);
         this.setUncaughtExceptionHandler(this.callback);
      }

      @Override
      protected void runImpl() throws InterruptedException {
         CheckLicenseCall var1 = new CheckLicenseCall(this.licenseService, this.serverManager);
         var1.makeCall(this.callback);
      }

      private class Callback implements CheckLicenseCallback {
         private Callback() {
         }

         @Override
         public void receivedInfo(final LicenseInfo var1, final LoginCredentials var2) {
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  Callback.this.showLicenseInfo(var1, var2);
               }
            });
         }

         private void showLicenseInfo(LicenseInfo var1, LoginCredentials var2) {
            LicenseInfoDisplayer var3 = new LicenseInfoDisplayer(CheckLicenseJob.this.serverManager, var1, CheckLicenseJob.this.waitService);
            var3.setServerCredentials(var2);
            var3.display();
         }

         @Override
         public void caught(Throwable var1) {
            ErrorLog.attention(LicenseService.class, "Failed to retrieve the requested license information from the server.", var1);
         }

         @Override
         public void cancelled() {
         }
      }
   }
}
