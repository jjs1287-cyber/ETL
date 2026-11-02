package com.etlforge.integration.server.engine.bulkload;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.etlforge.integration.server.engine.bulkload.batch.BatchProcess;
import com.etlforge.integration.server.engine.bulkload.batch.BatchProcessConfig;
import com.etlforge.integration.server.engine.bulkload.batch.DefaultCancelRequestHandler;
import com.etlforge.integration.server.engine.bulkload.batch.MessagePersistor;
import com.etlforge.integration.server.engine.bulkload.batch.MessageQueueMonitor;
import com.etlforge.integration.server.engine.bulkload.rest.JobCloser;
import com.etlforge.integration.server.engine.bulkload.rest.JobCreator;
import com.etlforge.integration.server.engine.bulkload.rest.Poster;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import org.apache.http.client.HttpClient;
import org.etlforge.integration.salesforce.bulkload.BulkAction;
import org.etlforge.system.Disposable;

public final class BulkLoadProcess implements Disposable {
   private final BulkLoadParams params;
   private final Optional<JobResultProcessThread> jobResultProcess;
   private BatchProcessConfig batchProcessConfig = BatchProcessConfig.DEFAULT;
   private String jobId;
   private BatchProcess batchProcess;
   private BulkLoadMonitor monitor = BulkLoadMonitor.NONE;
   private MessageQueueMonitor messageQueueMonitor;
   private MessagePersistor messagePersistor = MessagePersistor.NONE;
   private ErrorLog errorLog = ErrorLog.CONSOLE;
   private Logger logger;
   private volatile boolean canceled;
   private final Object batchProcessCreationLock = new Object();
   private final CountDownLatch completionLatch = new CountDownLatch(1);
   private final HttpClient client;

   public BulkLoadProcess(BulkLoadParams var1, HttpClient var2) {
      this.params = var1;
      this.jobResultProcess = Optional.absent();
      this.client = var2;
   }

   public BulkLoadProcess(BulkLoadParams var1, JobResultProcessThread var2, HttpClient var3) {
      this.params = var1;
      this.jobResultProcess = Optional.of(var2);
      this.client = var3;
   }

   public void setMessagePersistor(MessagePersistor var1) {
      Preconditions.checkNotNull(var1, "persistor");
      this.messagePersistor = var1;
   }

   public void setMonitor(BulkLoadMonitor var1) {
      Preconditions.checkNotNull(var1, "monitor");
      this.monitor = var1;
   }

   public void setBatchProcessConfig(BatchProcessConfig var1) {
      Preconditions.checkNotNull(var1, "cfg");
      this.batchProcessConfig = var1;
   }

   public void setErrorLog(ErrorLog var1) {
      Preconditions.checkNotNull(var1, "errorLog");
      this.errorLog = var1;
   }

   public void setLogger(Logger var1) {
      Preconditions.checkNotNull(var1, "logger");
      this.logger = var1;
   }

   public void run() throws BulkLoadException, InterruptedException {
      Preconditions.checkState(this.completionLatch.getCount() == 1L, "This process has already run.");
      this.createJob();

      try {
         if (this.canceled) {
            return;
         }

         if (this.isQuery()) {
            this.sendBulkQuery();
            this.closeJob();
            TimeUnit.MILLISECONDS.sleep(50L);
            this.startJobResultThread();
         } else {
            this.startJobResultThread();
            this.sendFile();
         }
      } finally {
         try {
            if (!this.isQuery()) {
               this.closeJob();
            }
         } finally {
            this.doPostJobWork();
         }
      }
   }

   private void createJob() throws BulkLoadException {
      try {
         this.monitor.preCreateJob();
         JobCreator var1 = new JobCreator(this.params.getEndpoint(), this.params.getCreateJobParams(), this.client);
         var1.setLogger(this.logger);
         this.jobId = var1.createJob();
         this.monitor.postJobCreated(this.getJobId());
      } catch (PersistorException var2) {
         throw new BulkLoadException(101, var2);
      }
   }

   private boolean isQuery() {
      return this.params.getCreateJobParams().action == BulkAction.QUERY;
   }

   private void startJobResultThread() {
      if (this.jobResultProcess.isPresent()) {
         this.jobResultProcess.get().start();
      }
   }

   private void sendBulkQuery() throws BulkLoadException {
      String var1 = this.params.getEndpoint().getSendBatchUrl(this.getJobId());
      Poster var2 = new Poster(var1, this.params.getEndpoint().getSessionId());

      try {
         var2.post(this.params.getQueryString(), "text/csv", this.client);
      } catch (Exception var4) {
         throw new BulkLoadException(201, var4);
      }
   }

   private void sendFile() throws BulkLoadException, InterruptedException {
      MessageQueueMonitor var1;
      BatchProcess var2;
      synchronized (this.batchProcessCreationLock) {
         if (this.canceled) {
            return;
         }

         this.logger.info("Preparing the batch sending. URL = " + this.params.getEndpoint().getSendBatchUrl(this.getJobId()));
         this.batchProcess = new BatchProcess(this.params, this.getJobId());
         this.batchProcess.setConfig(this.batchProcessConfig);
         this.batchProcess.setErrorLog(this.errorLog);
         this.batchProcess.setLogger(this.logger);
         this.messageQueueMonitor = createBatchMonitor(this.batchProcess);
         this.batchProcess.setMonitor(this.messageQueueMonitor);
         this.batchProcess.start();
         var2 = this.batchProcess;
         var1 = this.messageQueueMonitor;
      }

      try {
         var1.consumeMessages(this.messagePersistor);
      } catch (PersistorException var5) {
         this.handlePersistError(var1, var5);
      }

      boolean var7 = var2.stopThreads(5L, TimeUnit.SECONDS);
      if (!var7) {
      }

      var2.checkForScanningError();
   }

   private static MessageQueueMonitor createBatchMonitor(BatchProcess var0) {
      LinkedBlockingQueue var1 = new LinkedBlockingQueue(1000000);
      MessageQueueMonitor var2 = new MessageQueueMonitor(var1);
      var2.setCancelRequestHandler(new DefaultCancelRequestHandler(var0));
      return var2;
   }

   private void handlePersistError(MessageQueueMonitor var1, PersistorException var2) throws BulkLoadException {
      boolean var3 = this.batchProcess.cancel(5L, TimeUnit.MINUTES);
      if (!var3) {
      }

      if (var1.getNumberOfRecordsSent() == 0L) {
         throw new BulkLoadException(101, "An error occurred when recording the process of the bulk load.", var2);
      }

      this.errorLog.writeError("Failed to store a information about the bulk load process in the backend's data store.", var2);
   }

   private void closeJob() {
      try {
         this.monitor.preCloseJob();
         JobCloser var1 = new JobCloser(this.params.getEndpoint(), this.getJobId(), this.client);
         var1.setLogger(this.logger);
         var1.closeJob();
         this.monitor.postCloseJob();
      } catch (BulkLoadException var2) {
         var2.printStackTrace();
      } catch (PersistorException var3) {
         var3.printStackTrace();
      }
   }

   private void doPostJobWork() throws BulkLoadException {
      try {
         this.stopMonitor();
         this.waitForJobProcessThread();
      } finally {
         this.completionLatch.countDown();
      }
   }

   private void stopMonitor() {
      try {
         this.monitor.done();
      } catch (PersistorException var2) {
         this.errorLog.writeError("Could not record the bulk load end time.", var2);
      }
   }

   private void waitForJobProcessThread() throws BulkLoadException {
      if (this.jobResultProcess.isPresent()) {
         JobResultProcessThread var1 = this.jobResultProcess.get();
         var1.startPublishProgressMessages();

         try {
            var1.waitFor();
         } catch (InterruptedException var3) {
            Thread.currentThread().interrupt();
         }
      }
   }

   public boolean cancel(long var1, TimeUnit var3) {
      if (this.canceled) {
         return true;
      }

      this.canceled = true;
      if (!this.cancelJobResultProcess(var1, var3)) {
         return false;
      }

      BatchProcess var4;
      synchronized (this.batchProcessCreationLock) {
         var4 = this.batchProcess;
      }

      if (var4 != null) {
         boolean var8 = this.cancelProcessAndWait(var4, var1, var3);
         if (!var8) {
            return false;
         }
      }

      return this.cancelJobResultProcess(var1, var3);
   }

   private boolean cancelJobResultProcess(long var1, TimeUnit var3) {
      if (!this.jobResultProcess.isPresent()) {
         return true;
      }

      try {
         return this.jobResultProcess.get().cancel(var1, var3);
      } catch (InterruptedException var5) {
         Thread.currentThread().interrupt();
         return false;
      }
   }

   private boolean cancelProcessAndWait(BatchProcess var1, long var2, TimeUnit var4) {
      if (var1.cancel(var2, var4)) {
         try {
            return this.completionLatch.await(var2, var4);
         } catch (InterruptedException var6) {
            Thread.currentThread().interrupt();
         }
      }

      return false;
   }

   public void cancel() {
      if (!this.canceled) {
         this.canceled = true;
         synchronized (this.batchProcessCreationLock) {
            if (this.batchProcess != null) {
               this.batchProcess.cancel();
            }
         }
      }
   }

   public long getNumberOfRecordsSent() {
      synchronized (this.batchProcessCreationLock) {
         return this.messageQueueMonitor == null ? 0L : this.messageQueueMonitor.getNumberOfRecordsSent();
      }
   }

   @Override
   public void dispose() {
      if (this.jobResultProcess.isPresent()) {
         this.jobResultProcess.get().dispose();
      }
   }

   public String getJobId() {
      return this.jobId;
   }
}
