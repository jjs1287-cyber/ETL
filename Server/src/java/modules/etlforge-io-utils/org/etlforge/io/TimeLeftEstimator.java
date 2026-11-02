package org.etlforge.io;

import com.google.common.base.Preconditions;
import org.etlforge.system.ArgCheck;

public final class TimeLeftEstimator {
   private final long totalBytes;
   private long lastUpdate;
   private long bytesSoFar;
   private double averageBytesPerSeconds = -1.0;
   private final double smoothingFactor = 0.01;

   public TimeLeftEstimator(long var1) {
      this.totalBytes = ArgCheck.checkPositive(var1);
   }

   public void start() {
      this.lastUpdate = System.currentTimeMillis();
      this.bytesSoFar = 0L;
      this.averageBytesPerSeconds = -1.0;
   }

   public void update(long var1) {
      Preconditions.checkState(this.lastUpdate > 0L, "Must call start first");
      long var3 = var1 - this.bytesSoFar;
      ArgCheck.checkNotNegative(var3);
      long var5 = System.currentTimeMillis();
      long var7 = var5 - this.lastUpdate;
      if (var7 != 0L) {
         double var9 = var3 / (var7 / 1000.0);
         if (this.averageBytesPerSeconds < 0.0) {
            this.averageBytesPerSeconds = var9;
         } else {
            this.averageBytesPerSeconds = 0.01 * var9 + 0.99 * this.averageBytesPerSeconds;
         }

         this.bytesSoFar = var1;
         this.lastUpdate = var5;
      }
   }

   public long estimateSecondsLeft() {
      if (this.averageBytesPerSeconds < 0.0) {
         return -1L;
      }

      long var1 = this.totalBytes - this.bytesSoFar;
      return var1 <= 0L ? 0L : (long)(var1 / this.averageBytesPerSeconds);
   }
}
