package org.etlforge.application.ui.job;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import javax.swing.SwingWorker;
import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.ui.LoggingExceptionHandler;
import org.etlforge.lang.exception.ExceptionHandler;
import org.etlforge.system.callback.NoReceiver;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.wait.MessageWaitService;
import org.etlforge.ui.wait.WaitLock;
import org.etlforge.ui.wait.WaitService;

public final class Computation<T> {
   private final Callable<T> producer;
   private final Receiver<? super T> consumer;
   private volatile ExceptionHandler<Throwable> errorHandler = ExceptionHandler.STANDARD_ERR;

   public static <E> Computation<E> create(Callable<E> var0) {
      return create(var0, NoReceiver.create());
   }

   public static <E> Computation<E> create(Callable<E> var0, Receiver<? super E> var1) {
      return new Computation(var0, var1);
   }

   public Computation(Callable<T> var1, Receiver<? super T> var2) {
      this.producer = var1;
      this.consumer = var2;
   }

   public Computation<T> withErrorHandler(ExceptionHandler<Throwable> var1) {
      this.errorHandler = Preconditions.checkNotNull(var1);
      return this;
   }

   public void run() {
      try {
         Object var1 = this.producer.call();
         this.consumer.handle((T)var1);
      } catch (Throwable var2) {
         this.errorHandler.caught(var2);
      }
   }

   public Computation.SwingWorkerBuilder<T> swingWorkerBuilder() {
      return new Computation.SwingWorkerBuilder<>(this.producer, this.consumer, this.errorHandler);
   }

   public static class SwingWorkerBuilder<V> {
      private final Callable<V> task;
      private final Receiver<? super V> consumer;
      private volatile ExceptionHandler<Throwable> errorHandler;
      private volatile WaitService waitService = WaitService.NONE;
      private volatile String waitMessage;
      private final List<Runnable> runWhenFinished = Collections.synchronizedList(new ArrayList<>());

      public SwingWorkerBuilder(Callable<V> var1, Receiver<? super V> var2, ExceptionHandler<Throwable> var3) {
         this.task = var1;
         this.consumer = var2;
         this.errorHandler = var3;
      }

      public Computation.SwingWorkerBuilder<V> withErrorLog(ErrorLevel var1, Object var2) {
         return this.withErrorHandler(new LoggingExceptionHandler(var2, var1));
      }

      public Computation.SwingWorkerBuilder<V> withErrorHandler(ExceptionHandler<Throwable> var1) {
         this.errorHandler = var1;
         return this;
      }

      public Computation.SwingWorkerBuilder<V> withWaitService(WaitService var1) {
         this.waitService = Preconditions.checkNotNull(var1);
         return this;
      }

      public Computation.SwingWorkerBuilder<V> withWaitMessage(WaitService var1, String var2) {
         this.waitService = Preconditions.checkNotNull(var1);
         this.waitMessage = var2;
         return this;
      }

      public Computation.SwingWorkerBuilder<V> runWhenFinished(Runnable var1) {
         this.runWhenFinished.add(Preconditions.checkNotNull(var1));
         return this;
      }

      public SwingWorker<V, Void> build() {
         final WaitLock var1 = this.startWait();
         KongaSwingWorker var2 = new KongaSwingWorker<V, Void>() {
            @Override
            protected V doInBackground() throws Exception {
               return SwingWorkerBuilder.this.task.call();
            }

            @Override
            protected void onSuccess(V var1x) {
               SwingWorkerBuilder.this.consumer.handle((V)var1x);
            }

            @Override
            protected void onFailure(Throwable var1x) {
               SwingWorkerBuilder.this.errorHandler.caught(var1x);
            }

            @Override
            protected void finished() {
               var1.release();

               for (Runnable var2x : SwingWorkerBuilder.this.runWhenFinished) {
                  var2x.run();
               }
            }
         };
         var2.doNotLogErrors();
         return var2;
      }

      private WaitLock startWait() {
         return !com.google.common.base.Strings.isNullOrEmpty(this.waitMessage) && this.waitService instanceof MessageWaitService
            ? ((MessageWaitService)this.waitService).startWait(this.waitMessage)
            : this.waitService.startWait();
      }

      public Future<V> buildAndExecute() {
         SwingWorker var1 = this.build();
         var1.execute();
         return var1;
      }
   }
}
