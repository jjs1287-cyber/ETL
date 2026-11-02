package com.etlforge.integration.server.engine.bulkload.batch;

import com.google.common.base.Preconditions;
import org.etlforge.integration.server.progress.QuantityBasedProgressMessage;
import org.etlforge.system.ArgCheck;

final class BundleProgressTracker {
   private final BatchProcessMonitor monitor;
   private final long totalNumberOfRecords;
   private long numberOfRecordsSent;
   private long timeOfFirstUpdate;
   private long timeOfLastUpdate;
   private volatile long progressUpdateInterval = 1000L;
   private final Object lock = new Object();

   public BundleProgressTracker(BatchProcessMonitor var1, long var2) {
      this.monitor = Preconditions.checkNotNull(var1);
      this.totalNumberOfRecords = ArgCheck.checkNotNegative(var2);
   }

   public void setProgressUpdateInterval(long var1) {
      this.progressUpdateInterval = ArgCheck.checkNotNegative(var1);
   }

   public void bundleWasSent(int var1) {
      ArgCheck.checkNotNegative(var1);
      NewProgressMessage var2 = null;
      synchronized (this.lock) {
         this.numberOfRecordsSent += var1;
         if (this.isTimeForNewProgressUpdate()) {
            var2 = this.createProgressMessage();
            this.timeOfLastUpdate = System.currentTimeMillis();
         }
      }

      if (var2 != null) {
         this.monitor.handleMessage(var2);
      }
   }

   private boolean isTimeForNewProgressUpdate() {
      return this.timeOfFirstUpdate == 0L || System.currentTimeMillis() - this.timeOfLastUpdate >= this.progressUpdateInterval;
   }

   private NewProgressMessage createProgressMessage() {
      if (this.timeOfFirstUpdate == 0L) {
         this.timeOfFirstUpdate = System.currentTimeMillis();
      }

      QuantityBasedProgressMessage var1 = new QuantityBasedProgressMessage(
         "Sending data to Salesforce", this.timeOfFirstUpdate, this.totalNumberOfRecords, this.numberOfRecordsSent
      );
      var1.setMessageFormat("Sent %,d out of %,d records.");
      var1.setIncludeTimeEstimateInMessage(false);
      return new NewProgressMessage(var1);
   }
}
