package com.etlforge.integration.server.engine.bulkload.batch;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.etlforge.integration.server.engine.bulkload.BulkLoadParams;
import com.etlforge.integration.server.engine.bulkload.ErrorLog;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import org.etlforge.integration.salesforce.bulkload.BatchFileParams;
import org.etlforge.integration.salesforce.bulkload.backend.Batch;
import org.etlforge.integration.salesforce.bulkload.backend.BatchStatus;
import org.etlforge.integration.salesforce.bulkload.backend.RecordCounts;
import org.etlforge.integration.salesforce.bulkload.backend.SectionOfFile;
import org.etlforge.util.csv.CSVParams;
import org.etlforge.util.csv.CSVParser;

final class BundleConsumer implements Callable<Void> {
   private final BlockingQueue<Bundle> queue;
   private final int label;
   private int batchCount;
   private long startOfBatch;
   private long endOfBatch;
   private final BatchProcessMonitor monitor;
   private final BundleProgressTracker progressTracker;
   private final BatchFileParams batchParams;
   private final int[] sourceColumnsIndices;
   private final int headerSize;
   private volatile ErrorLog errorLog = ErrorLog.CONSOLE;
   private volatile Logger logger = Logger.getAnonymousLogger();
   private int batchRecordCounter;
   private long batchSizeCounter;
   private final FieldTransform fieldTransform;
   private volatile boolean stopIfEmpty;
   private volatile boolean canceled;
   private final BatchSender sender;
   private Writer tempfileWriter;
   private final String sourceFile;
   private final String tempfilePath;
   private String tempfileName;

   public BundleConsumer(
      int var1, BlockingQueue<Bundle> var2, BulkLoadParams var3, BatchSender var4, BatchProcessMonitor var5, BundleProgressTracker var6, String var7
   ) {
      this.label = var1;
      this.queue = var2;
      this.batchParams = var3.getBatchFileParams();
      this.sourceColumnsIndices = var3.getSourceColumns();
      this.headerSize = var3.getTargetHeader().length() + 1;
      this.sender = var4;
      this.monitor = var5;
      this.progressTracker = var6;
      this.fieldTransform = new FieldTransform(var3);
      this.tempfileWriter = null;
      this.tempfileName = "";
      this.sourceFile = var7;
      this.tempfilePath = this.sourceFile.substring(0, this.sourceFile.lastIndexOf(File.separator) + 1);
   }

   public void stopIfEmpty() {
      this.stopIfEmpty = true;
   }

   public void setErrorLog(ErrorLog var1) {
      this.errorLog = Preconditions.checkNotNull(var1);
   }

   public void setLogger(Logger var1) {
      this.logger = Preconditions.checkNotNull(var1);
   }

   public void cancel() {
      this.canceled = true;
   }

   public Void call() throws Exception {
      while (!this.canceled) {
         Bundle var1 = this.queue.poll(1L, TimeUnit.SECONDS);
         if (var1 != null) {
            this.processBundle(var1);
         } else {
            if (!this.isDone()) {
               continue;
            }
            break;
         }
      }

      this.terminateRequest();
      this.logger.fine("Consumer #" + this.label + " returning from call. Batch count: " + this.batchCount);
      return null;
   }

   private void processBundle(Bundle var1) throws Exception {
      if (!this.canceled) {
         try {
            String var2 = this.convertBundleToString(var1);
            if (var2 == null) {
               return;
            }

            int var3 = var2.length();
            this.checkBatchLimits(var3, var1);
            this.sendBundle(var1, var2);
            this.sendToTempFile(var1, var2);
            this.updateCounts(var1, var3);
            if (this.canceled) {
               this.terminateRequest();
            }
         } catch (Exception var4) {
            this.handleBundleError(var1, var4);
         }
      }
   }

   private void sendToTempFile(Bundle var1, String var2) throws Exception {
      if (this.tempfileWriter == null) {
         this.tempfileName = "SFBULK_" + this.label + "_" + this.batchCount;
         this.tempfileWriter = new OutputStreamWriter(new FileOutputStream(this.tempfilePath + this.tempfileName));
      }

      this.tempfileWriter.write(var2);
   }

   private String convertBundleToString(Bundle var1) throws Exception {
      ArrayList var2 = Lists.newArrayList();
      StringBuilder var3 = new StringBuilder();

      for (String var5 : var1.getRecords()) {
         CSVParser.readRecord(var5, var2, CSVParams.DEFAULT);
         boolean var6 = true;
         int var7 = 0;

         for (int var11 : this.sourceColumnsIndices) {
            String var12 = (String)var2.get(var11);
            String var13 = this.fieldTransform.transform(var7, var12);
            if (var6) {
               var6 = false;
            } else {
               var3.append(',');
            }

            var3.append(var13);
            var7++;
         }

         var3.append('\n');
         if (this.canceled) {
            this.terminateRequest();
            return null;
         }
      }

      return var3.toString();
   }

   private void checkBatchLimits(int var1, Bundle var2) throws Exception {
      if (this.batchParams.recordsPerFile > 0 && this.batchRecordCounter + var2.getNumberOfRecords() > this.batchParams.recordsPerFile
         || this.batchParams.charactersPerFile > 0 && this.batchSizeCounter + var1 >= this.batchParams.charactersPerFile) {
         this.terminateRequest();
      }
   }

   private void terminateRequest() throws Exception {
      if (this.sender.isOpen()) {
         int var1 = 0;
         boolean var2 = false;
         boolean var3 = false;
         BatchResponse var4 = null;

         Exception var5;
         do {
            try {
               var5 = null;
               var1++;
               var3 = this.canceled || var1 >= 10;
               if (var1 <= 1 || this.handleRetry(true)) {
                  var4 = this.sender.terminateRequest();
                  var2 = var4 != null && !var4.isClientSideError();
               }
            } catch (Exception var7) {
               var2 = false;
               var5 = var7;
               this.sender.forceClose();
            }
         } while (!var2 && !var3);

         if (var2 && var1 > 1) {
            System.out.println("TerminateRequest Recovered, On Try Count #" + var1);
         }

         if (var5 != null) {
            this.handleTerminateFailure(var5);
         } else {
            if (var4 == null) {
               this.handleTerminateFailure(new Exception("Unhandled error"));
            }

            this.processBatchResponse(var4);
         }

         this.batchCount++;
         this.closeAndDeleteTempFile();
      }
   }

   private void closeAndDeleteTempFile() throws Exception {
      this.closeTempFile();
      this.deleteTempFile();
   }

   private void closeTempFile() throws IOException {
      if (this.tempfileWriter != null) {
         this.tempfileWriter.close();
         this.tempfileWriter = null;
      }
   }

   private void deleteTempFile() throws Exception {
      File var1 = new File(this.tempfilePath + this.tempfileName);
      var1.delete();
   }

   private void processBatchResponse(BatchResponse var1) {
      String var2 = var1.getBatchId(this.errorLog);
      if (var2 != null) {
         SectionOfFile var3 = new SectionOfFile(this.startOfBatch, this.endOfBatch);
         RecordCounts var4 = RecordCounts.notProcessed(this.batchRecordCounter);
         Batch var5 = new Batch(var2, BatchStatus.QUEUED, var3, var4);
         this.monitor.handleMessage(new BatchSentMessage(var5));
      } else {
         this.handleFailureResponse(var1);
      }
   }

   private void handleTerminateFailure(Exception var1) {
      String var2 = "An error occurred closing connection during processing of a section of the source file: ";
      this.errorLog.writeError(var2, var1);
      this.logger.info("[Consumer #" + this.label + "] " + var2 + var1.getMessage());
      this.cancel();
      this.monitor.handleMessage(BatchProcessMessage.REQUEST_CANCEL);
   }

   private void handleFailureResponse(BatchResponse var1) {
      if (var1.isClientSideError()) {
         this.cancel();
         this.monitor.handleMessage(BatchProcessMessage.REQUEST_CANCEL);
      }
   }

   private void sendBundle(Bundle var1, String var2) throws Exception {
      if (!this.sender.isOpen()) {
         this.prepareForNewBatch(var1);
      }

      int var3 = 0;
      boolean var4 = false;
      boolean var5 = false;

      Exception var6;
      do {
         try {
            var6 = null;
            var3++;
            var5 = var3 >= 10;
            if (var3 <= 1 || this.handleRetry(false)) {
               this.sender.sendBundle(var2, this.label, this.batchCount);
               var4 = true;
            }
         } catch (Exception var8) {
            var6 = var8;
            var4 = false;
            this.sender.forceClose();
         }
      } while (!var4 && !var5);

      if (!var4) {
         throw var6;
      }

      if (var3 > 1) {
         System.out.println("SendBundle Recovered, On Try Count #" + var3);
      }

      this.progressTracker.bundleWasSent(var1.getNumberOfRecords());
   }

   private boolean handleRetry(boolean var1) {
      try {
         this.closeTempFile();
         Thread.sleep(5000L);
         String var2 = Files.toString(new File(this.tempfilePath + this.tempfileName), Charsets.UTF_8);
         this.sender.sendBundle(var2, this.label, this.batchCount);
         if (!var1) {
            this.tempfileWriter = new OutputStreamWriter(new FileOutputStream(this.tempfilePath + this.tempfileName, true));
         }

         return true;
      } catch (Exception var4) {
         String var3 = var1 ? "HandleTerminateRetry " : "HandleMiddleRetry ";
         this.errorLog.writeError(var3 + "Failed ", var4);
         this.sender.forceClose();
         return false;
      }
   }

   private void prepareForNewBatch(Bundle var1) {
      this.batchSizeCounter = this.headerSize;
      this.batchRecordCounter = 0;
      this.startOfBatch = var1.getStartPos();
   }

   private void updateCounts(Bundle var1, int var2) {
      this.batchRecordCounter = this.batchRecordCounter + var1.getNumberOfRecords();
      this.batchSizeCounter += var2 + 1;
      this.endOfBatch = var1.getNextPos();
   }

   private boolean isDone() {
      return this.stopIfEmpty && this.queue.isEmpty();
   }

   private void handleBundleError(Bundle var1, Exception var2) {
      String var3 = "An error occurred when processing a section of the source file, starting at position "
         + var1.getStartPos()
         + ", ending at position "
         + var1.getNextPos()
         + ":";
      this.errorLog.writeError(var3, var2);
      this.logger.info("[Consumer #" + this.label + "] " + var3 + var2.getMessage());
   }
}
