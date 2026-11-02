package com.etlforge.integration.server.engine.bulkload.batch;

import com.google.common.base.Preconditions;
import com.etlforge.integration.server.engine.bulkload.BulkLoadParams;
import com.etlforge.integration.server.engine.bulkload.BulkLoadUtils;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.logging.Logger;
import org.etlforge.lang.exception.NotImplementedYetException;
import org.etlforge.util.csv.CSVParser;
import org.etlforge.util.csv.FileContents;

final class BundleProducer implements Callable<Void> {
   private static final byte ef = -17;
   private static final byte bb = -69;
   private static final byte bf = -65;
   private final BulkLoadParams params;
   private final int[] sourceColumnIndices;
   private final int maxSourceColumnNumber;
   private final BatchProcessMonitor monitor;
   private FileContents in;
   private CSVParser parser;
   private final int sections;
   private final int bundleSize;
   private final long startAtRow;
   private final BlockingQueue<Bundle>[] queues;
   private SectionBlock[] block_array;
   private volatile boolean rerun;
   private volatile boolean canceled;
   private volatile int recordsToValidate = 0;
   private final FieldTransform fieldTransform;
   private volatile Logger logger = Logger.getAnonymousLogger();
   private volatile Runnable jobToRunWhenDone = new Runnable() {
      @Override
      public void run() {
      }
   };
   private static final byte CR = 13;
   private static final byte LN = 10;

   public BundleProducer(BulkLoadParams var1, BlockingQueue<Bundle>[] var2, int var3, BatchProcessMonitor var4) {
      this.params = var1;
      this.queues = var2;
      this.monitor = var4;
      this.sections = var2.length;
      this.fieldTransform = new FieldTransform(var1);
      this.bundleSize = var3;
      this.sourceColumnIndices = var1.getSourceColumns();
      this.maxSourceColumnNumber = 1 + this.sourceColumnIndices[this.sourceColumnIndices.length - 1];
      this.startAtRow = var1.getStartAtRow();
   }

   public void setLogger(Logger var1) {
      this.logger = Preconditions.checkNotNull(var1);
   }

   public void cancel() {
      this.canceled = true;
   }

   public void setReRun(boolean var1) {
      this.rerun = var1;
   }

   public void setRecordsToValidate(int var1) {
      this.recordsToValidate = var1;
   }

   public void setJobToRunWhenDone(Runnable var1) {
      this.jobToRunWhenDone = var1;
   }

   public Void call() throws Exception {
      try {
         this.openFile();
         this.scan();
         int var1 = this.createBundles();
         this.jobToRunWhenDone.run();
         this.logger.fine("Producer returning from call. Number of bundles handed over to queue: " + var1);
         return null;
      } finally {
         this.close();
      }
   }

   private void openFile() {
      this.in = new FileContents(this.params.getSourceFile(), this.params.getEncoding());
      this.parser = new CSVParser(this.in);
   }

   private void scan() throws Exception {
      long var1 = this.params.getSourceFile().length();
      if (var1 == 0L) {
         throw new Exception("The input file is empty.");
      }

      ScanningProgress var3 = this.publishScanningStarted(var1);
      long var4 = this.getStartPosition(var1);
      int var6 = 0;
      long[] var7 = null;
      if (this.rerun) {
         throw new NotImplementedYetException();
      }

      long var8 = (var1 - var4) / this.sections;
      long var10 = var8 + var4;
      var7 = new long[this.sections + 1];
      var7[0] = var4;
      int var12 = 0;
      this.logger.fine("input file size=" + var1 + ", start " + var4);
      Date var13 = new Date();

      long var16;
      for (long var14 = var4; !this.canceled && (var16 = this.parser.readRecord(var14, true)) >= 0L; var6++) {
         List var18 = this.parser.getValuesForLastRecord();
         var16 = this.consumeLeadingLineBreaks(var16);
         if (BulkLoadUtils.isBlankRecord(var18)) {
            var6--;
         } else {
            if (var6 < this.recordsToValidate) {
               this.validateRecord(var18, var14, var16);
            }

            boolean var19 = this.params.getBatchFileParams().recordsPerFile <= 0 ? var6 == 0 : var6 % this.params.getBatchFileParams().recordsPerFile == 0;
            if (var19 && var16 > var10) {
               this.logger.fine("section " + var12 + ", break at record=" + var6 + ", pos=" + var14);
               var7[++var12] = var14;
               var10 += var8;
            }

            Date var20 = new Date();
            if (var20.after(var13)) {
               var3.scanningUpdate(var14);
               var13.setTime(var20.getTime() + 1000L);
            }
         }

         var14 = var16;
      }

      if (var7[var12] < var1) {
         var7[++var12] = var1;
         this.logger.fine("section " + var12 + ", break at record=" + var6 + ", pos=" + var1);
      }

      assert var12 <= this.sections;

      while (var12 < this.sections) {
         var7[++var12] = var1;
         this.logger.fine("section " + var12 + ", break at record=" + var6 + ", pos=" + var1);
      }

      this.block_array = new SectionBlock[this.sections];

      for (int var23 = 0; var23 < this.sections; var23++) {
         this.block_array[var23] = new SectionBlock(new Interval("Section_" + var23, var7[var23], var7[var23 + 1]));
      }

      ScanningDoneMessage var22 = new ScanningDoneMessage(new ScanningResult(var6, var1, var7));
      this.monitor.handleMessage(var22);
   }

   private ScanningProgress publishScanningStarted(long var1) {
      ScanningProgress var3 = new ScanningProgress(this.monitor);
      var3.scanningStarted(var1);
      return var3;
   }

   private void validateRecord(List<String> var1, long var2, long var4) throws Exception {
      try {
         if (var1.size() < this.maxSourceColumnNumber) {
            throw this.validationError(
               String.format("Found a record with %d fields, but expected %d fields.", var1.size(), this.maxSourceColumnNumber), var2, var4
            );
         }

         int var6 = 0;

         for (int var10 : this.sourceColumnIndices) {
            try {
               this.fieldTransform.setStrict(true);
               this.fieldTransform.transform(var6, (String)var1.get(var10));
               this.fieldTransform.setStrict(false);
            } catch (Exception var12) {
               throw this.validationError("Failed to transform a value in the source file: " + var12.getMessage(), var2, var4);
            }

            var6++;
         }
      } catch (Exception var13) {
         throw var13;
      }
   }

   private Exception validationError(String var1, long var2, long var4) {
      String var6 = String.format("%s\nThe error was detected in the following record:\n\"%s\"", var1, this.parser.read(var2, var4).trim());
      Exception var7 = new Exception(var6);
      this.monitor.handleMessage(new ScanningErrorMessage(var7));
      return var7;
   }

   private long getStartPosition(long var1) throws IOException {
      long var5 = 0L;
      if (var1 >= 3L && this.in.at(0L) == -17 && this.in.at(1L) == -69 && this.in.at(2L) == -65) {
         var5 = 3L;
      }

      if (this.params.getSourceFileHasHeader()) {
         var5 = this.parser.readRecord(var5);
         var5 = this.consumeLeadingLineBreaks(var5);
      }

      for (int var7 = 1; var5 < var1 && var7 < this.startAtRow; var7++) {
         var5 = this.parser.readRecord(var5);
         var5 = this.consumeLeadingLineBreaks(var5);
      }

      return var5 < var1 ? var5 : var1;
   }

   private long consumeLeadingLineBreaks(long var1) throws IOException {
      byte var3;
      while ((var3 = this.in.at(++var1)) == 13 || var3 == 10) {
      }

      return var1;
   }

   private Integer createBundles() throws Exception {
      Bundle[] var1 = new Bundle[this.sections];
      int var2 = 0;
      boolean var3 = false;

      while (!var3 && !this.canceled) {
         for (int var4 = 0; var4 < this.sections && !this.canceled; var4++) {
            SectionBlock var5 = this.block_array[var4];
            Bundle var6 = var1[var4];
            boolean var7 = false;
            if (var6 == null && var5.sectionEnd > var5.currentPos) {
               var6 = this.readBundle(var5.currentPos, var5.sectionEnd);
               if (var6 != null) {
                  var7 = true;
               }
            }

            if (this.canceled) {
               break;
            }

            if (var6 != null && this.queues[var4].offer(var6)) {
               if (var7) {
                  var2++;
               }

               var5.currentPos = var6.getNextPos();
               var5.updateCurrentPosition();
               var6 = null;
            }

            var1[var4] = var6;
         }

         int var8 = 0;

         while (var8 < this.sections && !this.canceled && this.block_array[var8].done() && var1[var8] == null) {
            var8++;
         }

         var3 = var8 == this.sections;
      }

      return var2;
   }

   private Bundle readBundle(long var1, long var3) throws Exception {
      Bundle var5 = new Bundle(var1);

      for (int var8 = 0; var8 < this.bundleSize && !this.canceled; var8++) {
         long var6 = this.parser.readRecord(var1);
         if (var6 < 0L || var6 > var3) {
            break;
         }

         String var9 = this.in.getStr(var1, (int)(var6 - var1));
         boolean var10 = BulkLoadUtils.isBlank(var9);
         if (!var10) {
            var5.addRecord(var9);
         }

         var6 = this.consumeLeadingLineBreaks(var6);
         var1 = var6;
      }

      var5.setNextPos(var1);
      return var5;
   }

   private void close() {
      try {
         this.in.close();
      } catch (Exception var2) {
         var2.printStackTrace();
      }
   }
}
