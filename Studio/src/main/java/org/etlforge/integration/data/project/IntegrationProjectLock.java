package org.etlforge.integration.data.project;

import com.google.common.base.Preconditions;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public final class IntegrationProjectLock {
   private static final IntegrationProjectLock.LockImpl lock = new IntegrationProjectLock.LockImpl();
   private static final Executor executor = Executors.newFixedThreadPool(1);

   public static void acquire() {
      lock.lock();
   }

   public static boolean acquireWithin(long var0, TimeUnit var2) {
      try {
         return lock.tryLock(var0, var2);
      } catch (InterruptedException var4) {
         Thread.currentThread().interrupt();
         return false;
      }
   }

   public static void release() {
      lock.unlock();
   }

   public static boolean hasLock() {
      return lock.getOwner() == Thread.currentThread();
   }

   public static void ensureLockHeld() {
      if (!hasLock()) {
         throw new IntegrationProjectAccessException();
      }
   }

   public static void acquireAndRun(final Runnable var0) {
      Preconditions.checkNotNull(var0, "job");
      Runnable var1 = new Runnable() {
         @Override
         public void run() {
            IntegrationProjectLock.acquire();

            try {
               var0.run();
            } finally {
               IntegrationProjectLock.release();
            }
         }
      };
      executor.execute(var1);
   }

   private IntegrationProjectLock() {
      throw new UnsupportedOperationException("No instances of this class.");
   }

   private static final class LockImpl extends ReentrantLock {
      private LockImpl() {
      }

      @Override
      public Thread getOwner() {
         return super.getOwner();
      }
   }
}
