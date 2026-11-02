package org.etlforge.integration.server.progress;

import com.google.common.base.Preconditions;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.etlforge.system.ArgCheck;

public final class QuantityBasedProgressMessage implements OperationProgressMessage {
   private final long processedQuantity;
   private final long totalQuantity;
   private final long timeOfEstimate;
   private final long estimatedTimeLeft;
   private final int percentageDone;
   private final DateFormat timeMarker = new SimpleDateFormat("HH:mm:ss");
   private volatile boolean includeTimeEstimateInMessage;
   private volatile String messageFormat = "%d / %d";
   private volatile String section;

   public QuantityBasedProgressMessage(String var1, long var2, long var4, long var6) {
      ArgCheck.checkNotEmpty(var1, "section");
      ArgCheck.checkPositive(var2, "startTime");
      this.processedQuantity = var6;
      this.totalQuantity = var4;
      this.timeOfEstimate = System.currentTimeMillis();
      this.estimatedTimeLeft = this.estimateTimeLeft(var2, var4, var6);
      this.percentageDone = calculatePercentageDone(var4, var6);
      this.setSection(var1);
   }

   private long estimateTimeLeft(long var1, long var3, long var5) {
      ArgCheck.checkNotNegative(var3, "totalQuantity");
      ArgCheck.checkNotNegative(var5, "processedQuantity");
      Preconditions.checkArgument(var3 >= var5, "totalQuantity must be >= processedQuantity");
      if (var5 == var3) {
         return 0L;
      } else if (this.timeOfEstimate > var1 && var5 != 0L) {
         long var7 = var3 - var5;
         double var9 = (double)(this.timeOfEstimate - var1) / var5;
         return (long)(var9 * var7);
      } else {
         return -1L;
      }
   }

   private static int calculatePercentageDone(long var0, long var2) {
      if (var0 == 0L) {
         return 100;
      } else {
         return var2 == 0L ? 0 : (int)(100.0 * ((double)var2 / var0));
      }
   }

   @Override
   public String getSection() {
      return this.section;
   }

   public void setSection(String var1) {
      ArgCheck.checkNotEmpty(var1, "section");
      this.section = var1;
   }

   public void setMessageFormat(String var1) {
      ArgCheck.checkNotEmpty(var1, "f cannot be null or empty");
      this.messageFormat = var1;
   }

   public void setIncludeTimeEstimateInMessage(boolean var1) {
      this.includeTimeEstimateInMessage = var1;
   }

   @Override
   public Date getEstimatedEndTime() {
      return this.estimatedTimeLeft < 0L ? null : new Date(this.timeOfEstimate + this.estimatedTimeLeft);
   }

   @Override
   public long getEstimatedTimeLeftInSeconds() {
      return this.estimatedTimeLeft / 1000L;
   }

   @Override
   public int getPercentageDone() {
      return this.percentageDone;
   }

   @Override
   public String getMessage() {
      String var1 = String.format(this.messageFormat, this.processedQuantity, this.totalQuantity);
      if (this.includeTimeEstimateInMessage) {
         var1 = var1 + " " + this.getTimeLeftMessage();
      }

      return var1;
   }

   private String getTimeLeftMessage() {
      if (this.estimatedTimeLeft < 0L) {
         return "Cannot give a time estimate";
      }

      if (this.estimatedTimeLeft == 0L) {
         return "Done";
      }

      long var1 = this.estimatedTimeLeft / 1000L;
      long var3 = var1 / 60L;
      var1 -= 60L * var3;
      long var5 = var3 / 60L;
      var3 -= 60L * var5;
      long var7 = var5 / 24L;
      var5 -= 24L * var7;
      return String.format("Expect to be done in %dD %dH %dm %d s (at %s)", var7, var5, var3, var1, this.timeMarker.format(this.getEstimatedEndTime()));
   }

   @Override
   public String toString() {
      return this.getMessage();
   }
}
