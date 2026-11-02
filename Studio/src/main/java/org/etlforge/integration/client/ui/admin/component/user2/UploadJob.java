package org.etlforge.integration.client.ui.admin.component.user2;

import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.job.JobStatus;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.application.ui.job.UiJobMessages;
import org.etlforge.integration.client.server.useradmin.SetUsersAndGroupsCallback;
import org.etlforge.integration.client.server.useradmin.UserAdmin;
import org.etlforge.system.callback.CallbackResult;
import org.etlforge.system.callback.CallbackResultWrapper;
import org.etlforge.ui.util.UiUtils;

class UploadJob extends UiJob {
   private final UserAdmin serverApi;
   private final UserConsoleModel model;
   private final UploadJob.Callback internalCallback;
   private final CallbackResult successCallback;

   public UploadJob(UserAdmin var1, UserConsoleModel var2, CallbackResult var3) {
      super(Strings.get("User.UploadJob.Name"));
      this.serverApi = var1;
      this.model = var2;
      this.internalCallback = new UploadJob.Callback();
      this.successCallback = new CallbackResultWrapper(var3);
      this.setBlockUntilDone(true);
      this.setUncaughtExceptionHandler(this.internalCallback);
      this.setUploadFlag(true);
   }

   @Override
   protected void runImpl() throws InterruptedException {
      this.setMessage(Strings.get("User.UploadJob.Started"));
      this.serverApi.setUsersAndGroups(this.model.getUsers(), this.model.getGroups(), this.model.getIdOfServerOfOrigin(), this.internalCallback);
   }

   @Override
   protected void onJobCancelled() {
      this.internalCallback.cancelled();
   }

   private void setUploadFlag(final boolean var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            UploadJob.this.model.setUploadingInfo(var1);
         }
      });
   }

   private class Callback implements SetUsersAndGroupsCallback {
      private Callback() {
      }

      @Override
      public void doneSettingUsersAndGroups() {
         UploadJob.this.setCanBeCancelled(false);
         UploadJob.this.setMayBeInterrupted(false);
         UiUtils.runOnEventThread(new Runnable() {
            @Override
            public void run() {
               UploadJob.this.model.setDirty(false);
               UploadJob.this.setUploadFlag(false);
               UploadJob.this.successCallback.succeeded(null);
            }
         });
      }

      @Override
      public void cancelled() {
         UploadJob.this.setUploadFlag(false);
      }

      @Override
      public void caught(Throwable var1) {
         UploadJob.this.setStatus(JobStatus.ERROR);
         UploadJob.this.setMessage(UiJobMessages.ERROR);
         ErrorLog.attention(UploadJob.class, Strings.get("User.UploadJob.Error.Message"), var1);
         UploadJob.this.setUploadFlag(false);
      }
   }
}
