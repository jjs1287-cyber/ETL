package org.etlforge.integration.activity.ui.queue;

import java.awt.EventQueue;
import java.util.List;
import org.etlforge.application.ui.job.JobStatus;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.application.ui.job.UiJobMessages;
import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.integration.activity.serverapi.queue.GetOperationQueueCallback;
import org.etlforge.integration.activity.serverapi.queue.OperationQueueProvider;
import org.etlforge.integration.activity.serverapi.queue.OperationQueueRequest;
import org.etlforge.integration.activity.ui.AbstractActivityServerCallback;

final class GetQueueJob extends UiJob {
   private final OperationQueueProvider queueApi;
   private final OperationQueueModel model;
   private final OperationQueueRequest request;
   private final GetQueueJob.Callback callback;

   public GetQueueJob(OperationQueueProvider var1, OperationQueueModel var2, OperationQueueRequest var3) {
      super(PackageResources.Job.getName(var2.getSubject()));
      this.queueApi = var1;
      this.model = var2;
      this.request = var3;
      this.callback = new GetQueueJob.Callback();
      this.setUncaughtExceptionHandler(this.callback);
      this.setBlockUntilDone(true);
   }

   @Override
   protected void runImpl() throws InterruptedException {
      this.setMessage(PackageResources.Job.STARTED);
      this.queueApi.getOperationQueue(this.request, this.callback);
   }

   @Override
   protected void onJobCancelled() {
      this.callback.cancelled();
   }

   private final class Callback extends AbstractActivityServerCallback implements GetOperationQueueCallback, Runnable {
      private volatile List<OperationLogEntry> response;

      public Callback() {
         super(GetQueueJob.this.model);
         this.setErrorTitle(PackageResources.Job.ERROR_TITLE);
      }

      @Override
      public void queueWasRetrieved(List<OperationLogEntry> var1) {
         if (!GetQueueJob.this.isCancelled()) {
            GetQueueJob.this.setCanBeCancelled(false);
            GetQueueJob.this.setMayBeInterrupted(false);
            GetQueueJob.this.setMessage(UiJobMessages.UPDATE_UI);
            this.response = var1;
            EventQueue.invokeLater(this);
         }
      }

      @Override
      public void run() {
         GetQueueJob.this.model.getSubject().hasBeenUpdated();
         GetQueueJob.this.model.setActivityEntries(this.response);
         GetQueueJob.this.model.setTalkingToServer(false);
      }

      @Override
      public void caught(Throwable var1) {
         if (!GetQueueJob.this.isCancelled()) {
            GetQueueJob.this.setStatus(JobStatus.ERROR);
            GetQueueJob.this.setMessage(UiJobMessages.ERROR);
            super.caught(var1);
         }
      }
   }
}
