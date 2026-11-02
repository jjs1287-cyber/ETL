package org.etlforge.application.worker;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import org.etlforge.lang.exception.ExceptionHandler;

public interface ApplicationWorker {
   ApplicationWorker.Guard GUARD = new ApplicationWorker.Guard() {
      @Override
      public ApplicationWorker.SafeRunnable protect(final Runnable var1) {
         return new ApplicationWorker.SafeRunnable() {
            @Override
            public void run() {
               var1.run();
            }
         };
      }
   };

   Future<?> submitForSequential(Runnable var1);

   <T> Future<T> submitForSequential(Callable<T> var1);

   Future<?> submitForParallel(Runnable var1);

   void submitForParallelAndWait(Runnable var1, ExceptionHandler<Throwable> var2) throws InterruptedException;

   <T> Future<T> submitForParallel(Callable<T> var1);

   interface Guard {
      ApplicationWorker.SafeRunnable protect(Runnable var1);
   }

   interface SafeRunnable extends Runnable {
   }
}
