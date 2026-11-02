package org.etlforge.integration.activity.ui.history;

import java.awt.EventQueue;
import java.util.List;
import org.etlforge.application.ui.job.JobStatus;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.application.ui.job.UiJobMessages;
import org.etlforge.integration.activity.client.history.SourceHistoryEntry;
import org.etlforge.integration.activity.serverapi.history.GetSourceHistoryCallback;
import org.etlforge.integration.activity.serverapi.history.HistoryRequest;
import org.etlforge.integration.activity.serverapi.history.SourceHistoryProvider;
import org.etlforge.integration.activity.ui.AbstractActivityServerCallback;

final class GetSourceHistoryJob extends UiJob {
   private final SourceHistoryModel model;
   private final SourceHistoryProvider historyApi;
   private final HistoryRequest request;
   private final GetSourceHistoryJob.Callback callback;

   public GetSourceHistoryJob(SourceHistoryModel var1, SourceHistoryProvider var2, HistoryRequest var3) {
      super(PackageResources.SourceJob.getName(var1.getSubject()));
      this.model = var1;
      this.historyApi = var2;
      this.request = var3;
      this.callback = new GetSourceHistoryJob.Callback();
      this.setUncaughtExceptionHandler(this.callback);
      this.setBlockUntilDone(true);
   }

   @Override
   protected void runImpl() throws InterruptedException {
      this.setMessage(PackageResources.SourceJob.STARTED);
      this.historyApi.getSourceHistory(this.request, this.callback);
   }

   @Override
   protected void onJobCancelled() {
      this.callback.cancelled();
   }

   private final class Callback extends AbstractActivityServerCallback implements GetSourceHistoryCallback, Runnable {
      private volatile List<SourceHistoryEntry> response;

      public Callback() {
         super(GetSourceHistoryJob.this.model);
         this.setErrorTitle(PackageResources.SourceJob.ERROR_TITLE);
      }

      @Override
      public void historyWasRetrieved(List<SourceHistoryEntry> var1) {
         if (!GetSourceHistoryJob.this.isCancelled()) {
            GetSourceHistoryJob.this.setCanBeCancelled(false);
            GetSourceHistoryJob.this.setMayBeInterrupted(false);
            GetSourceHistoryJob.this.setMessage(UiJobMessages.UPDATE_UI);
            this.response = var1;
            EventQueue.invokeLater(this);
         }
      }

      @Override
      public void run() {
         GetSourceHistoryJob.this.model.getSubject().hasBeenUpdated();
         GetSourceHistoryJob.this.model.setActivityEntries(this.response);
         GetSourceHistoryJob.this.model.setTalkingToServer(false);
      }

      @Override
      public void caught(Throwable var1) {
         if (!GetSourceHistoryJob.this.isCancelled()) {
            GetSourceHistoryJob.this.setStatus(JobStatus.ERROR);
            GetSourceHistoryJob.this.setMessage(UiJobMessages.ERROR);
            super.caught(var1);
         }
      }
   }
}
