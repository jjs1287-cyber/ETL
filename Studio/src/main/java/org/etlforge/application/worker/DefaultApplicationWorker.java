package org.etlforge.application.worker;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.lang.exception.ExceptionHandler;

public final class DefaultApplicationWorker implements ApplicationWorker {
   private static final ExecutorService singleThreadExecutor = Executors.newFixedThreadPool(1);
   private static final ExecutorService cachedThreadExecutor = Executors.newCachedThreadPool();

   @Override
   public synchronized Future<?> submitForSequential(Runnable var1) {
      return singleThreadExecutor.submit(new DefaultApplicationWorker.RunnableWrapper(var1));
   }

   @Override
   public synchronized <T> Future<T> submitForSequential(Callable<T> var1) {
      return singleThreadExecutor.submit(new DefaultApplicationWorker.CallableWrapper<>(var1));
   }

   @Override
   public synchronized Future<?> submitForParallel(Runnable var1) {
      return cachedThreadExecutor.submit(new DefaultApplicationWorker.RunnableWrapper(var1));
   }

   @Override
   public void submitForParallelAndWait(Runnable var1, ExceptionHandler<Throwable> var2) throws InterruptedException {
      Future var3 = this.submitForParallel(var1);

      try {
         var3.get();
      } catch (ExecutionException var5) {
         var2.caught(var5.getCause());
      }
   }

   @Override
   public synchronized <T> Future<T> submitForParallel(Callable<T> var1) {
      return cachedThreadExecutor.submit(new DefaultApplicationWorker.CallableWrapper<>(var1));
   }

   private static class CallableWrapper<T> implements Callable<T> {
      private final Callable<T> callable;

      public CallableWrapper(Callable<T> var1) {
         this.callable = var1;
      }

      @Override
      public T call() throws Exception {
         return this.callable.call();
      }
   }

   private static class ExceptionGuard implements UncaughtExceptionHandler {
      private final Object source;

      public ExceptionGuard(Object var1) {
         this.source = var1;
      }

      @Override
      public void uncaughtException(Thread var1, Throwable var2) {
         ErrorLog.attention(this.source, "An error occurred", var2);
      }
   }

   private static class RunnableWrapper implements Runnable {
      private final Runnable job;

      public RunnableWrapper(Runnable var1) {
         this.job = var1;
      }

      @Override
      public void run() {
         if (this.job instanceof ApplicationWorker.SafeRunnable) {
            this.runSafely();
         } else {
            this.job.run();
         }
      }

      private void runSafely() {
         try {
            this.job.run();
         } catch (Throwable var2) {
            new DefaultApplicationWorker.ExceptionGuard(this.job).uncaughtException(Thread.currentThread(), var2);
         }
      }
   }
}
