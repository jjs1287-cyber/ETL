package org.etlforge.integration.activity.ui.log;

import java.awt.EventQueue;
import java.util.List;
import org.etlforge.application.ui.job.JobStatus;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.application.ui.job.UiJobMessages;
import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.integration.activity.serverapi.log.GetOperationLogCallback;
import org.etlforge.integration.activity.serverapi.log.OperationLogProvider;
import org.etlforge.integration.activity.serverapi.log.OperationLogRequest;
import org.etlforge.integration.activity.ui.AbstractActivityServerCallback;

final class GetLogJob extends UiJob {
   private final OperationLogModel model;
   private final OperationLogProvider logApi;
   private final OperationLogRequest request;
   private final GetLogJob.Callback callback;

   public GetLogJob(OperationLogModel var1, OperationLogProvider var2, OperationLogRequest var3) {
      super(PackageResources.LogJob.getName(var1.getSubject()));
      this.logApi = var2;
      this.model = var1;
      this.request = var3;
      this.callback = new GetLogJob.Callback();
      this.setUncaughtExceptionHandler(this.callback);
      this.setBlockUntilDone(true);
   }

   @Override
   protected void runImpl() throws InterruptedException {
      this.setMessage(PackageResources.LogJob.STARTED);
      this.logApi.getOperationLog(this.request, this.callback);
   }

   @Override
   protected void onJobCancelled() {
      this.callback.cancelled();
   }

   private final class Callback extends AbstractActivityServerCallback implements GetOperationLogCallback, Runnable {
      private volatile List<OperationLogEntry> response;

      public Callback() {
         super(GetLogJob.this.model);
         this.setErrorTitle(PackageResources.LogJob.ERROR_TITLE);
      }

      @Override
      public void run() {
         GetLogJob.this.model.getSubject().hasBeenUpdated();
         GetLogJob.this.model.setActivityEntries(this.response);
         GetLogJob.this.model.setTalkingToServer(false);
      }

      @Override
      public void logWasRetrieved(List<OperationLogEntry> var1) {
         if (!GetLogJob.this.isCancelled()) {
            GetLogJob.this.setMessage(UiJobMessages.UPDATE_UI);
            GetLogJob.this.setCanBeCancelled(false);
            GetLogJob.this.setMayBeInterrupted(false);
            if (!GetLogJob.this.isCancelled()) {
               this.response = var1;
               EventQueue.invokeLater(this);
            }
         }
      }

      @Override
      public void caught(Throwable var1) {
         if (!GetLogJob.this.isCancelled()) {
            GetLogJob.this.setStatus(JobStatus.ERROR);
            GetLogJob.this.setMessage(UiJobMessages.ERROR);
            super.caught(var1);
         }
      }
   }
}
