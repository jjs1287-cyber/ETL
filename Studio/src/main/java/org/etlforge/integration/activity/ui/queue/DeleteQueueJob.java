package org.etlforge.integration.activity.ui.queue;

import org.etlforge.application.ui.job.UiJob;
import org.etlforge.integration.activity.serverapi.queue.DeleteQueueEntriesCallback;
import org.etlforge.integration.activity.serverapi.queue.OperationQueueProvider;
import org.etlforge.integration.activity.ui.AbstractActivityServerCallback;

class DeleteQueueJob extends UiJob {
   private final OperationQueueProvider queueApi;
   private final OperationQueueModel model;
   private final OperationQueueController controller;
   private final DeleteQueueJob.Callback callback;

   public DeleteQueueJob(OperationQueueProvider var1, OperationQueueModel var2, OperationQueueController var3) {
      super("Deleting operations from the queue");
      this.queueApi = var1;
      this.model = var2;
      this.controller = var3;
      this.callback = new DeleteQueueJob.Callback();
      this.setUncaughtExceptionHandler(this.callback);
      this.setBlockUntilDone(true);
   }

   @Override
   protected void runImpl() throws InterruptedException {
      this.queueApi.deleteQueueEntries(this.model.getEntriesToDelete(), this.callback);
   }

   private class Callback extends AbstractActivityServerCallback implements DeleteQueueEntriesCallback {
      public Callback() {
         super(DeleteQueueJob.this.model);
      }

      @Override
      public void entriesWereDeleted(long[] var1) {
         if (!DeleteQueueJob.this.isCancelled()) {
            DeleteQueueJob.this.model.setTalkingToServer(false);
            DeleteQueueJob.this.controller.downloadQueue();
         }
      }
   }
}
