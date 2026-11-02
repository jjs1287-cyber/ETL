package org.etlforge.integration.client.server.ui.user.profile;

import java.awt.EventQueue;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.job.JobStatus;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.integration.client.server.AbstractServerCallback;
import org.etlforge.integration.client.server.IntegrationServer;
import org.etlforge.integration.client.server.login.LoginConfiguration;
import org.etlforge.integration.client.server.useradmin.SetUserInfoCallback;
import org.etlforge.integration.client.server.useradmin.UserAdmin;
import org.etlforge.integration.user.User;

final class UploadUserProfileJob extends UiJob {
   private final LoginConfiguration loginConfig;
   private final UserProfileDialog dialog;
   private final User user;
   private final SetUserInfoCallback callback;

   public UploadUserProfileJob(LoginConfiguration var1, UserProfileDialog var2, User var3) {
      super(Strings.get("UserProfile.Upload.Name"));
      this.loginConfig = var1;
      this.dialog = var2;
      this.user = var3;
      this.callback = new UploadUserProfileJob.CallbackImpl();
      this.setUncaughtExceptionHandler(this.callback);
      this.setBlockUntilDone(true);
   }

   @Override
   protected void runImpl() throws InterruptedException {
      this.setMessage(Strings.get("UserProfile.Upload.Started"));
      Thread.sleep(250L);
      IntegrationServer var1 = IntegrationServer.getInstance();
      UserAdmin var2 = var1.getServerCall(UserAdmin.class);
      var2.setUserInfo(this.user, this.callback);
   }

   @Override
   protected void onJobCancelled() {
      this.callback.cancelled();
   }

   private final class CallbackImpl extends AbstractServerCallback implements SetUserInfoCallback {
      private CallbackImpl() {
      }

      @Override
      public void userInfoSet(User var1) {
         if (!UploadUserProfileJob.this.isCancelled()) {
            UploadUserProfileJob.this.loginConfig.updateUserProfile(var1);
            UploadUserProfileJob.this.dialog.dispose();
            this.showSuccessMessage();
         }
      }

      private void showSuccessMessage() {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               Alert.info(Strings.get("UserProfile.Upload.Success.Message"), Strings.get("UserProfile.Upload.Success.Title"));
            }
         });
      }

      @Override
      public void caught(Throwable var1) {
         if (!UploadUserProfileJob.this.isCancelled()) {
            super.caught(var1);
            UploadUserProfileJob.this.setStatus(JobStatus.ERROR);
            UploadUserProfileJob.this.dialog.onUploadFailed();
         }
      }
   }
}
