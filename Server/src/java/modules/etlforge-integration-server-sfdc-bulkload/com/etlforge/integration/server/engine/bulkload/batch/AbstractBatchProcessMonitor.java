package com.etlforge.integration.server.engine.bulkload.batch;

import com.google.common.base.Preconditions;
import java.util.concurrent.CountDownLatch;

public abstract class AbstractBatchProcessMonitor implements BatchProcessMonitor {
   private volatile long numberOfRecordsSent;
   private final CountDownLatch completionLatch = new CountDownLatch(1);
   private final Object lock = new Object();
   private Runnable cancelRequestHandler;

   public final void waitUntilDone() throws InterruptedException {
      this.completionLatch.await();
   }

   public final long getNumberOfRecordsSent() {
      return this.numberOfRecordsSent;
   }

   @Override
   public final void handleMessage(BatchProcessMessage var1) {
      Preconditions.checkNotNull(var1, "m");
      synchronized (this.lock) {
         this.checkIfCancelRequested(var1);
         this.doCustomProcessing(var1);
         this.doDefaultProcessing(var1);
         this.checkStop(var1);
      }
   }

   private void checkIfCancelRequested(BatchProcessMessage var1) {
      if (var1 == BatchProcessMessage.REQUEST_CANCEL) {
         Runnable var2 = null;
         synchronized (this.lock) {
            var2 = this.cancelRequestHandler;
            this.cancelRequestHandler = null;
         }

         if (var2 != null) {
            var2.run();
         }
      }
   }

   protected void doCustomProcessing(BatchProcessMessage var1) {
   }

   private void doDefaultProcessing(BatchProcessMessage var1) {
      if (var1 instanceof BatchSentMessage) {
         this.handleBatchSent((BatchSentMessage)var1);
      }
   }

   private void handleBatchSent(BatchSentMessage var1) {
      this.numberOfRecordsSent = this.numberOfRecordsSent + var1.getNumberOfRecords();
   }

   private void checkStop(BatchProcessMessage var1) {
      if (var1 == BatchProcessMessage.LAST_BATCH_SENT) {
         this.completionLatch.countDown();
      }
   }

   public void setCancelRequestHandler(Runnable var1) {
      synchronized (this.lock) {
         this.cancelRequestHandler = var1;
      }
   }
}
