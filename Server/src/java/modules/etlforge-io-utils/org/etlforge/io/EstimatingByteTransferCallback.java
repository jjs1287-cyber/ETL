package org.etlforge.io;

public abstract class EstimatingByteTransferCallback extends ProgressUpdateByteTransferCallback {
   private final long numberOfBytesToTransfer;
   private final TimeLeftEstimator estimator;

   public EstimatingByteTransferCallback(long var1) {
      this.numberOfBytesToTransfer = var1;
      this.estimator = new TimeLeftEstimator(var1);
   }

   public final long getNumberOfBytesToTransfer() {
      return this.numberOfBytesToTransfer;
   }

   @Override
   public final void started() {
      this.estimator.start();
   }

   @Override
   protected long estimateSecondsLeft(long var1) {
      this.estimator.update(var1);
      return this.estimator.estimateSecondsLeft();
   }

   public final String formatTimeLeftMessage(long var1, long var3) {
      if (var3 <= 0L) {
         return var1 < this.numberOfBytesToTransfer ? "0 secs left" : "";
      }

      if (var3 == 1L) {
         return "1 sec left";
      }

      if (var3 < 60L) {
         return var3 + " secs left";
      }

      long var5 = var3 / 60L;
      if (var5 == 1L) {
         return "1 min left";
      }

      if (var5 < 60L) {
         return var5 + " mins left";
      }

      long var7 = var5 / 60L;
      long var9 = var5 % 60L;
      String var11 = var7 + " h";
      if (var9 == 1L) {
         var11 = var11 + " 1 min";
      } else if (var9 > 1L) {
         var11 = var11 + " " + var9 + " mins";
      }

      return var11 + " left";
   }
}
