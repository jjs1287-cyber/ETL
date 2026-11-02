package com.etlforge.integration.server.engine.bulkload;

import com.etlforge.integration.server.engine.bulkload.db.OperationProgressTab;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.etlforge.integration.salesforce.bulkload.backend.RecordCounts;
import org.etlforge.system.Disposable;

final class JobResultProcessThread implements Disposable {
   private final ExecutorService thread = Executors.newSingleThreadExecutor();
   private final BulkLoadResultProcess jobResultProcess;
   private final OperationProgressTab progressTab;
   private Date progressStartDate;
   private Future<?> future;
   private volatile boolean publishProgressMessages;
   private volatile boolean canceled;

   public JobResultProcessThread(BulkLoadResultProcess var1, OperationProgressTab var2) {
      this.jobResultProcess = var1;
      this.progressTab = var2;
   }

   public void start() {
      this.future = this.thread.submit(new Callable<Void>() {
         public Void call() throws Exception {
            while (JobResultProcessThread.this.shouldContinue()) {
               RecordCounts var1 = JobResultProcessThread.this.jobResultProcess.ResultProcess();
               JobResultProcessThread.this.publishProgressUpdate(var1);
               if (!JobResultProcessThread.this.shouldStop()) {
                  TimeUnit.SECONDS.sleep(2L);
                  continue;
               }
               break;
            }

            JobResultProcessThread.this.jobResultProcess.waitForFuture();
            return null;
         }
      });
   }

   private boolean shouldContinue() {
      return !this.shouldStop();
   }

   private boolean shouldStop() {
      return this.canceled || this.jobResultProcess.isDone();
   }

   private void publishProgressUpdate(RecordCounts var1) {
      if (this.publishProgressMessages) {
         if (var1.sent != 0) {
            if (this.progressStartDate == null) {
               this.progressStartDate = new Date();
            }

            RecordCountsProgressMessage var2 = new RecordCountsProgressMessage(var1, this.progressStartDate);
            this.persistProgressUpdate(var2);
         }
      }
   }

   private void persistProgressUpdate(RecordCountsProgressMessage var1) {
      try {
         this.progressTab.insertProgressMessage(var1);
      } catch (PersistorException var3) {
         var3.printStackTrace();
      }
   }

   public void startPublishProgressMessages() {
      this.publishProgressMessages = true;
   }

   public void waitFor() throws BulkLoadException, InterruptedException {
      try {
         this.future.get();
      } catch (ExecutionException var3) {
         Throwable var2 = var3.getCause();
         throw new BulkLoadException(105, var2);
      }
   }

   public boolean cancel(long var1, TimeUnit var3) throws InterruptedException {
      this.canceled = true;
      this.future.cancel(true);
      this.thread.shutdown();
      return this.thread.awaitTermination(var1, var3);
   }

   @Override
   public void dispose() {
      try {
         this.progressTab.dispose();
      } finally {
         this.jobResultProcess.dispose();
      }
   }
}
