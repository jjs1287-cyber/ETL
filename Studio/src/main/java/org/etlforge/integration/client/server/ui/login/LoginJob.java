package org.etlforge.integration.client.server.ui.login;

import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.application.ui.job.JobStatus;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.login.InvalidCredentialsException;
import org.etlforge.integration.client.server.login.LoginCallback;
import org.etlforge.integration.client.server.login.LoginCredentials;
import org.etlforge.integration.client.server.login.LoginService;
import org.etlforge.integration.user.User;

final class LoginJob extends UiJob implements LoginCallback {
   private final LoginService loginService;
   private final LoginCredentials credentials;
   private final LoginCallback externalCallback;
   private final boolean showInvalidCredentialsMessage;

   public LoginJob(LoginService var1, LoginCredentials var2, LoginCallback var3, boolean var4) {
      super(PackageResources.LoginJob.getName(var2));
      this.credentials = var2;
      this.loginService = var1;
      this.externalCallback = var3;
      this.showInvalidCredentialsMessage = var4;
      this.setUncaughtExceptionHandler(this);
      this.setBlockUntilDone(true);
   }

   @Override
   protected final void runImpl() throws InterruptedException {
      this.loginService.addCallback(this);
      this.setMessage(PackageResources.LoginJob.SENDING_CREDENTIALS);
      this.loginService.login(this.credentials);
   }

   @Override
   protected void onJobCancelled() {
      this.loginService.cancelled();
      this.cancelled();
   }

   @Override
   public void loginSucceeded(ServerInfo var1, LoginCredentials var2, User var3) {
      if (!this.isCancelled()) {
         this.externalCallback.loginSucceeded(var1, var2, var3);
      }
   }

   @Override
   public void invalidCredentials(InvalidCredentialsException var1) {
      if (!this.isCancelled()) {
         this.showInvalidCredentialsMessage(var1);
         this.externalCallback.invalidCredentials(var1);
      }
   }

   private void showInvalidCredentialsMessage(InvalidCredentialsException var1) {
      if (this.showInvalidCredentialsMessage) {
         NonBlockingAlert.error(ApplicationUi.getAppWinFrame(), var1.getMessage(), "Login Failed");
      }
   }

   @Override
   public void cancelled() {
      this.externalCallback.cancelled();
   }

   @Override
   public void caught(Throwable var1) {
      if (!this.isCancelled()) {
         this.externalCallback.caught(var1);
         this.setStatus(JobStatus.ERROR);
         ErrorLog.attention(this.getClass(), PackageResources.LoginJob.ERROR, var1);
      }
   }
}
