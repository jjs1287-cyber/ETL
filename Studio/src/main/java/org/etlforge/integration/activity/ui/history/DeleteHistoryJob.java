package org.etlforge.integration.activity.ui.history;

import org.etlforge.application.ui.job.UiJob;
import org.etlforge.integration.activity.serverapi.history.DeleteSourceHistoryCallback;
import org.etlforge.integration.activity.serverapi.history.SourceHistoryProvider;
import org.etlforge.integration.activity.ui.AbstractActivityServerCallback;

final class DeleteHistoryJob extends UiJob {
   private final SourceHistoryProvider historyApi;
   private final SourceHistoryModel model;
   private final SourceHistoryController controller;
   private final DeleteHistoryJob.Callback callback;

   public DeleteHistoryJob(SourceHistoryProvider var1, SourceHistoryModel var2, SourceHistoryController var3) {
      super("Deleting source history");
      this.historyApi = var1;
      this.model = var2;
      this.controller = var3;
      this.callback = new DeleteHistoryJob.Callback();
      this.setUncaughtExceptionHandler(this.callback);
      this.setBlockUntilDone(true);
   }

   @Override
   protected void runImpl() throws InterruptedException {
      this.historyApi.deleteSourceHistoryEntries(this.model.getEntriesToDelete(), this.callback);
   }

   private class Callback extends AbstractActivityServerCallback implements DeleteSourceHistoryCallback {
      public Callback() {
         super(DeleteHistoryJob.this.model);
      }

      @Override
      public void entriesWereDeleted(long[] var1) {
         if (!DeleteHistoryJob.this.isCancelled()) {
            DeleteHistoryJob.this.model.setTalkingToServer(false);
            DeleteHistoryJob.this.controller.downloadHistory();
         }
      }
   }
}
