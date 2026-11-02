package org.etlforge.integration.client.server.ui.user.profile;

import java.awt.EventQueue;
import org.etlforge.application.ui.job.JobStatus;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.integration.client.server.AbstractServerCallback;
import org.etlforge.integration.client.server.IntegrationServer;
import org.etlforge.integration.client.server.useradmin.GetUserInfoCallback;
import org.etlforge.integration.client.server.useradmin.UserAdmin;
import org.etlforge.integration.user.User;

final class DownloadUserProfileJob extends UiJob {
   private final UserProfileDialog dialog;
   private final GetUserInfoCallback callback;

   public DownloadUserProfileJob(UserProfileDialog var1) {
      super(Strings.get("UserProfile.Download.Name"));
      this.dialog = var1;
      this.callback = new DownloadUserProfileJob.CallbackImpl();
      this.setUncaughtExceptionHandler(this.callback);
      this.setBlockUntilDone(true);
   }

   @Override
   protected void runImpl() throws InterruptedException {
      this.setMessage(Strings.get("UserProfile.Download.Started"));
      Thread.sleep(250L);
      IntegrationServer var1 = IntegrationServer.getInstance();
      UserAdmin var2 = var1.getServerCall(UserAdmin.class);
      var2.getUserInfo(this.callback);
   }

   @Override
   protected void onJobCancelled() {
      this.callback.cancelled();
   }

   private final class CallbackImpl extends AbstractServerCallback implements GetUserInfoCallback {
      private CallbackImpl() {
      }

      @Override
      public void gotUserInfo(final User var1) {
         if (!DownloadUserProfileJob.this.isCancelled()) {
            DownloadUserProfileJob.this.setCanBeCancelled(false);
            DownloadUserProfileJob.this.setMayBeInterrupted(false);
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  DownloadUserProfileJob.this.dialog.onUserInfoDownloaded(var1);
               }
            });
         }
      }

      @Override
      public void caught(Throwable var1) {
         if (!DownloadUserProfileJob.this.isCancelled()) {
            super.caught(var1);
            DownloadUserProfileJob.this.setStatus(JobStatus.ERROR);
            DownloadUserProfileJob.this.dialog.dispose();
         }
      }
   }
}
