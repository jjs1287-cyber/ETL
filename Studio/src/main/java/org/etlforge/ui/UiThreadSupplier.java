package org.etlforge.ui;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import org.etlforge.lang.exception.ExceptionHandler;

public final class UiThreadSupplier<T> implements Supplier<T> {
   private final Supplier<T> supplier;
   private ExceptionHandler<Throwable> errorHandler;
   private final Object lock;

   public static <E> UiThreadSupplier<E> wrap(Supplier<E> var0) {
      return new UiThreadSupplier(var0);
   }

   public static <E> E call(Supplier<E> var0) {
      return wrap(var0).get();
   }

   public UiThreadSupplier(Supplier<T> var1) {
      Preconditions.checkNotNull(var1, "supplier");
      this.supplier = var1;
      this.lock = new Object();
   }

   public void setErrorHandler(ExceptionHandler<Throwable> var1) {
      synchronized (this.lock) {
         this.errorHandler = var1;
      }
   }

   private ExceptionHandler<Throwable> getErrorHandler() {
      synchronized (this.lock) {
         return this.errorHandler;
      }
   }

   @Override
   public T get() {
      return EventQueue.isDispatchThread() ? this.supplier.get() : this.getFromBackgroundThread();
   }

   private T getFromBackgroundThread() {
      UiThreadSupplier.BackgroundRunner var1 = new UiThreadSupplier.BackgroundRunner();
      var1.submit();
      return (T)var1.get();
   }

   private class BackgroundRunner {
      private T value;
      private Throwable error;

      private BackgroundRunner() {
      }

      public void submit() {
         try {
            EventQueue.invokeAndWait(new Runnable() {
               @Override
               public void run() {
                  BackgroundRunner.this.set(UiThreadSupplier.this.supplier.get());
               }
            });
         } catch (InterruptedException var2) {
            Thread.currentThread().interrupt();
            this.handleError(var2);
         } catch (InvocationTargetException var3) {
            this.handleError(var3.getTargetException());
         }
      }

      public T get() {
         if (this.error != null) {
            throw Throwables.propagate(this.error);
         }

         synchronized (this) {
            return this.value;
         }
      }

      private synchronized void set(T var1) {
         this.value = (T)var1;
      }

      private void handleError(Throwable var1) {
         ExceptionHandler var2 = UiThreadSupplier.this.getErrorHandler();
         if (var2 != null) {
            var2.caught(var1);
         } else {
            this.error = var1;
         }
      }
   }
}
