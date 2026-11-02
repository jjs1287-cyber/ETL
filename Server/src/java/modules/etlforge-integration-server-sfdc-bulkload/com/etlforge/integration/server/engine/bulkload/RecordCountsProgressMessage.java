package com.etlforge.integration.server.engine.bulkload;

import com.google.common.base.Preconditions;
import java.text.NumberFormat;
import java.util.Date;
import org.etlforge.integration.salesforce.bulkload.backend.RecordCounts;
import org.etlforge.integration.server.progress.OperationProgressMessage;
import org.etlforge.system.ArgCheck;

final class RecordCountsProgressMessage implements OperationProgressMessage {
   private final RecordCounts counts;
   private final long startTime;

   public RecordCountsProgressMessage(RecordCounts var1, Date var2) {
      this.counts = Preconditions.checkNotNull(var1);
      ArgCheck.checkPositive(var1.sent);
      this.startTime = var2 == null ? -1L : var2.getTime();
   }

   @Override
   public String getSection() {
      return "Receiving batch results";
   }

   @Override
   public String getMessage() {
      NumberFormat var1 = NumberFormat.getInstance();
      return String.format(
         "Total records: %s. Processed records: %s. Failed records: %s.",
         var1.format(this.counts.sent),
         var1.format(this.counts.processed),
         var1.format(this.counts.failure)
      );
   }

   @Override
   public int getPercentageDone() {
      return (int)(100.0 * this.getFractionDone());
   }

   private double getFractionDone() {
      if (this.counts.processed > this.counts.sent) {
         System.out.println("More processed than sent. processed = " + this.counts.processed + "; sent = " + this.counts.sent);
         return 1.0;
      } else {
         return (double)this.counts.processed / this.counts.sent;
      }
   }

   @Override
   public long getEstimatedTimeLeftInSeconds() {
      if (this.startTime < 0L) {
         return -1L;
      }

      double var1 = this.getFractionDone();
      if (var1 < 0.05) {
         return -1L;
      }

      if (var1 == 1.0) {
         return 0L;
      }

      long var3 = System.currentTimeMillis() - this.startTime;
      if (var3 <= 0L) {
         return -1L;
      }

      double var5 = (1.0 - var1) / var1;
      long var7 = (long)(var3 * var5);
      return var7 / 1000L;
   }

   @Override
   public Date getEstimatedEndTime() {
      long var1 = this.getEstimatedTimeLeftInSeconds();
      return var1 < 0L ? null : new Date(System.currentTimeMillis() + var1 * 1000L);
   }
}
