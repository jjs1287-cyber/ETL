package org.etlforge.io;

public abstract class ProgressUpdateByteTransferCallback implements ByteTransferCallback {
   private long progressUpdateInterval = 500L;
   private long lastProgressUpdate;

   public final void setProgressUpdateInterval(long var1) {
      this.progressUpdateInterval = var1;
   }

   @Override
   public void started() {
   }

   @Override
   public final void update(long var1) {
      if (this.progressUpdateInterval >= 0L) {
         long var3 = System.currentTimeMillis();
         if (this.lastProgressUpdate == 0L) {
            this.lastProgressUpdate = var3;
         } else if (var3 - this.lastProgressUpdate > this.progressUpdateInterval) {
            long var5 = this.estimateSecondsLeft(var1);
            this.handleProgressUpdate(var1, var1, var5);
            this.lastProgressUpdate = var3;
         }
      }
   }

   protected abstract long estimateSecondsLeft(long var1);

   protected abstract void handleProgressUpdate(long var1, long var3, long var5);
}
