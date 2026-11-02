package org.etlforge.integration.activity.ui.history;

import java.awt.EventQueue;
import java.util.List;
import org.etlforge.application.ui.job.JobStatus;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.application.ui.job.UiJobMessages;
import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.integration.activity.serverapi.history.GetOperationHistoryCallback;
import org.etlforge.integration.activity.serverapi.history.HistoryRequest;
import org.etlforge.integration.activity.serverapi.history.OperationHistoryProvider;
import org.etlforge.integration.activity.ui.AbstractActivityServerCallback;

final class GetOperationHistoryJob extends UiJob {
   private final OperationHistoryModel model;
   private final OperationHistoryProvider historyApi;
   private final HistoryRequest request;
   private final GetOperationHistoryJob.Callback callback;

   public GetOperationHistoryJob(OperationHistoryModel var1, OperationHistoryProvider var2, HistoryRequest var3) {
      super(PackageResources.OperationJob.getName(var1.getSubject()));
      this.model = var1;
      this.historyApi = var2;
      this.request = var3;
      this.callback = new GetOperationHistoryJob.Callback();
      this.setUncaughtExceptionHandler(this.callback);
      this.setBlockUntilDone(true);
   }

   @Override
   protected void runImpl() throws InterruptedException {
      this.historyApi.getOperationHistory(this.request, this.callback);
   }

   @Override
   protected void onJobCancelled() {
      this.callback.cancelled();
   }

   private final class Callback extends AbstractActivityServerCallback implements GetOperationHistoryCallback, Runnable {
      private volatile List<OperationLogEntry> response;

      public Callback() {
         super(GetOperationHistoryJob.this.model);
         this.setErrorTitle(PackageResources.OperationJob.ERROR_TITLE);
      }

      @Override
      public void historyWasRetrieved(List<OperationLogEntry> var1) {
         if (!GetOperationHistoryJob.this.isCancelled()) {
            GetOperationHistoryJob.this.setCanBeCancelled(false);
            GetOperationHistoryJob.this.setMayBeInterrupted(false);
            GetOperationHistoryJob.this.setMessage(UiJobMessages.UPDATE_UI);
            this.response = var1;
            EventQueue.invokeLater(this);
         }
      }

      @Override
      public void run() {
         GetOperationHistoryJob.this.model.getSubject().hasBeenUpdated();
         GetOperationHistoryJob.this.model.setActivityEntries(this.response);
         GetOperationHistoryJob.this.model.setTalkingToServer(false);
      }

      @Override
      public void caught(Throwable var1) {
         if (!GetOperationHistoryJob.this.isCancelled()) {
            GetOperationHistoryJob.this.setStatus(JobStatus.ERROR);
            GetOperationHistoryJob.this.setMessage(UiJobMessages.ERROR);
            super.caught(var1);
         }
      }
   }
}
