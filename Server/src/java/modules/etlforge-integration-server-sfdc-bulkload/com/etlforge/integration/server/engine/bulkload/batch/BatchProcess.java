package com.etlforge.integration.server.engine.bulkload.batch;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.etlforge.integration.server.engine.bulkload.BulkLoadException;
import com.etlforge.integration.server.engine.bulkload.BulkLoadParams;
import com.etlforge.integration.server.engine.bulkload.ErrorLog;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import org.etlforge.system.ArgCheck;

public final class BatchProcess {
   private final BulkLoadParams params;
   private final String jobId;
   private BatchProcessConfig config = BatchProcessConfig.DEFAULT;
   private volatile DebugRequestHeaders debugRequestHeaders;
   private BlockingQueue<Bundle>[] queues;
   private volatile BatchProcessMonitor monitor = new ConsoleMonitor();
   private volatile ErrorLog errorLog = ErrorLog.CONSOLE;
   private volatile ExecutorService executor;
   private BundleProducer producer;
   private Future<Void> producerFuture;
   private List<BundleConsumer> consumers;
   private List<Future<Void>> consumerFutures;
   private BatchProcess.CompletionMonitor completionMonitor;
   private volatile Logger logger = Logger.getAnonymousLogger();
   private boolean canceled;
   private volatile Exception scanningError;
   private final Object lock = new Object();

   public BatchProcess(BulkLoadParams var1, String var2) {
      this.params = Preconditions.checkNotNull(var1);
      this.jobId = ArgCheck.checkNotEmpty(var2);
   }

   public void setConfig(BatchProcessConfig var1) {
      this.config = Preconditions.checkNotNull(var1);
   }

   public void setDebugRequestHeaders(DebugRequestHeaders var1) {
      this.debugRequestHeaders = var1;
   }

   public void setMonitor(BatchProcessMonitor var1) {
      this.monitor = Preconditions.checkNotNull(var1);
   }

   public void setErrorLog(ErrorLog var1) {
      this.errorLog = Preconditions.checkNotNull(var1);
   }

   public void setLogger(Logger var1) {
      this.logger = Preconditions.checkNotNull(var1);
   }

   public void checkForScanningError() throws BulkLoadException {
      if (this.scanningError != null) {
         throw new BulkLoadException(104, this.scanningError);
      }
   }

   public void start() {
      synchronized (this.lock) {
         if (!this.canceled) {
            this.createQueues();
            this.createExecutor();
            this.startProducer();
            this.startCompletionMonitor();
         }
      }
   }

   private void createQueues() {
      this.queues = new BlockingQueue[this.config.numberOfThreads];

      for (int var1 = 0; var1 < this.config.numberOfThreads; var1++) {
         this.queues[var1] = new ArrayBlockingQueue<>(this.config.queueSize);
      }
   }

   private void createExecutor() {
      this.executor = Executors.newFixedThreadPool(this.config.numberOfThreads + 1 + 1);
   }

   private void startProducer() {
      int var1 = this.params.getBatchFileParams().recordsPerFile > 0
         ? Math.min(this.config.recordsPerBundle, this.params.getBatchFileParams().recordsPerFile)
         : this.config.recordsPerBundle;
      this.logger.fine("Effective bundle size: " + var1);
      this.producer = new BundleProducer(this.params, this.queues, var1, new BatchProcess.ProducerMonitor(this.monitor));
      this.producer.setRecordsToValidate(this.config.recordsToValidate);
      this.producer.setJobToRunWhenDone(new Runnable() {
         @Override
         public void run() {
            BatchProcess.this.tellConsumersToStopIfQueueIsEmpty();
         }
      });
      this.producer.setLogger(this.logger);
      this.producerFuture = this.executor.submit(this.producer);
   }

   private void tellConsumersToStopIfQueueIsEmpty() {
      if (!this.canceled) {
         synchronized (this.lock) {
            for (BundleConsumer var3 : this.consumers) {
               var3.stopIfEmpty();
            }
         }
      }
   }

   private void startCompletionMonitor() {
      this.completionMonitor = new BatchProcess.CompletionMonitor(this.producerFuture);
      this.executor.submit(this.completionMonitor);
   }

   public void cancel() {
      new Thread(new Runnable() {
         @Override
         public void run() {
            BatchProcess.this.cancel(1L, TimeUnit.HOURS);
         }
      }).start();
   }

   public boolean cancel(long var1, TimeUnit var3) {
      synchronized (this.lock) {
         if (this.canceled) {
            return this.awaitTermination(var1, var3);
         }

         this.canceled = true;
      }

      return this.stopThreads(var1, var3);
   }

   public boolean stopThreads(long var1, TimeUnit var3) {
      synchronized (this.lock) {
         if (this.producer == null) {
            return true;
         }

         if (this.executor == null) {
            return true;
         }

         this.cancelProducer();
         this.cancelConsumers();
      }

      this.executor.shutdown();
      return this.awaitTermination(var1, var3);
   }

   private void cancelProducer() {
      this.producer.cancel();
      this.producerFuture.cancel(true);
   }

   private void cancelConsumers() {
      if (this.consumers != null) {
         for (BundleConsumer var2 : this.consumers) {
            var2.cancel();
         }
      }
   }

   private boolean awaitTermination(long var1, TimeUnit var3) {
      if (this.executor == null) {
         return true;
      }

      try {
         boolean var4 = this.executor.awaitTermination(var1, var3);
         if (var4) {
            this.executor = null;
         }

         return var4;
      } catch (InterruptedException var5) {
         var5.printStackTrace();
         Thread.currentThread().interrupt();
         return false;
      }
   }

   private class CompletionMonitor implements Callable<Void> {
      private final Future<Void> producer;
      private volatile List<Future<Void>> consumers;
      private final CountDownLatch consumersAvailableLatch;

      public CompletionMonitor(Future<Void> var2) {
         this.producer = var2;
         this.consumersAvailableLatch = new CountDownLatch(1);
      }

      public void setConsumers(List<Future<Void>> var1) {
         this.consumers = var1;
         this.consumersAvailableLatch.countDown();
      }

      public Void call() throws Exception {
         try {
            if (this.waitFor(this.producer)) {
               this.consumersAvailableLatch.await();
            }

            if (this.consumers != null) {
               for (Future var2 : this.consumers) {
                  this.waitFor(var2);
               }
            }
         } catch (Exception var6) {
            var6.printStackTrace();
            throw var6;
         } finally {
            BatchProcess.this.monitor.handleMessage(BatchProcessMessage.LAST_BATCH_SENT);
         }

         return null;
      }

      private boolean waitFor(Future<?> var1) {
         try {
            var1.get();
            return true;
         } catch (InterruptedException var3) {
            Thread.currentThread().interrupt();
         } catch (Exception var4) {
            BatchProcess.this.logger.fine("Error from background task: " + var4.getMessage());
         }

         return false;
      }
   }

   private class ProducerMonitor implements BatchProcessMonitor {
      private final BatchProcessMonitor externalMonitor;

      public ProducerMonitor(BatchProcessMonitor var2) {
         this.externalMonitor = var2;
      }

      @Override
      public void handleMessage(BatchProcessMessage var1) {
         this.externalMonitor.handleMessage(var1);
         if (var1 instanceof ScanningDoneMessage) {
            this.startConsumers(((ScanningDoneMessage)var1).getResult());
         } else if (var1 instanceof ScanningErrorMessage) {
            BatchProcess.this.scanningError = ((ScanningErrorMessage)var1).getError();
         }
      }

      private void startConsumers(ScanningResult var1) {
         synchronized (BatchProcess.this.lock) {
            if (!BatchProcess.this.canceled) {
               long var3 = var1.getTotalNumberOfRecords();
               BundleProgressTracker var5 = new BundleProgressTracker(this.externalMonitor, var3);
               BatchProcess.this.consumers = Lists.newArrayList();
               BatchProcess.this.consumerFutures = Lists.newArrayList();

               for (int var6 = 0; var6 < BatchProcess.this.queues.length; var6++) {
                  BundleConsumer var7 = this.createConsumer(var6, var5);
                  BatchProcess.this.consumers.add(var7);
                  BatchProcess.this.consumerFutures.add(BatchProcess.this.executor.submit(var7));
               }

               BatchProcess.this.completionMonitor.setConsumers(BatchProcess.this.consumerFutures);
            }
         }
      }

      private BundleConsumer createConsumer(int var1, BundleProgressTracker var2) {
         BatchSender var3 = this.createSender();
         BundleConsumer var4 = new BundleConsumer(
            var1,
            BatchProcess.this.queues[var1],
            BatchProcess.this.params,
            var3,
            this.externalMonitor,
            var2,
            BatchProcess.this.params.getSourceFile().getPath()
         );
         var4.setErrorLog(BatchProcess.this.errorLog);
         var4.setLogger(BatchProcess.this.logger);
         return var4;
      }

      private BatchSender createSender() {
         HttpSender var1 = new HttpSender(
            BatchProcess.this.params.getEndpoint(), BatchProcess.this.jobId, BatchProcess.this.params.getTargetHeader(), BatchProcess.this.params.getCompress()
         );
         var1.setProxySettings(BatchProcess.this.params.getProxySettings());
         var1.setDebugHeaders(BatchProcess.this.debugRequestHeaders);
         return var1;
      }
   }
}
