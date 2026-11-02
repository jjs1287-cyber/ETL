package org.etlforge.system.thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public abstract class AbstractInterruptible implements Interruptible {
   private final CountDownLatch latch = new CountDownLatch(1);
   private boolean stopHasBeenGranted;
   private int status;
   private Throwable uncaughtException;

   public AbstractInterruptible() {
      this.setStatus(0);
   }

   protected abstract void runImpl();

   @Override
   public final synchronized int status() {
      return this.status;
   }

   @Override
   public final synchronized boolean stop() {
      if (this.stopHasBeenGranted) {
         return true;
      }

      if (this.canBeStopped()) {
         this.stopHasBeenGranted = true;
         if (!this.isInState(4)) {
            this.appendStatus(2);
         }

         return true;
      } else {
         return false;
      }
   }

   protected abstract boolean canBeStopped();

   protected final synchronized boolean isStopping() {
      return this.isInState(2);
   }

   @Override
   public final void waitUntilStopped() throws InterruptedException {
      this.latch.await();
   }

   @Override
   public final void waitUntilStopped(long var1, TimeUnit var3) throws InterruptedException {
      this.latch.await(var1, var3);
   }

   @Override
   public final void run() {
      try {
         try {
            synchronized (this) {
               if (this.isStopping()) {
                  return;
               }

               this.setStatus(1);
            }

            this.runImpl();
         } catch (Throwable var19) {
            this.uncaughtException = var19;
            this.appendStatus(32);
         }
      } finally {
         synchronized (this) {
            this.revokeState(1);
            this.revokeState(2);
            this.appendStatus(4);
            this.latch.countDown();
         }
      }
   }

   public Throwable getUncaughtException() {
      if (!this.isInState(8)) {
         throw new IllegalStateException("This method cannot be called until this runnable has completed.");
      } else {
         return this.uncaughtException;
      }
   }

   protected final synchronized void setCompleted() {
      this.appendStatus(8);
      this.revokeState(2);
   }

   protected final synchronized void setInterrupted() {
      this.appendStatus(16);
      this.revokeState(2);
   }

   private synchronized void setStatus(int var1) {
      this.status = var1;
   }

   private synchronized void appendStatus(int var1) {
      this.status |= var1;
   }

   private synchronized void revokeState(int var1) {
      this.status ^= var1;
   }

   private synchronized boolean isInState(int var1) {
      return (this.status & var1) != 0;
   }

   public static void main(String[] var0) {
      int var1 = 0;
      var1 ^= 0;
      var1 |= 1;
      assert var1 == 1;
      var1 |= 2;
      var1 |= 16;
      var1 ^= 1;
      var1 ^= 0;
      assert var1 == 18;
      var1 ^= 2;
      var1 |= 4;
      assert var1 == 20;
   }
}
