package org.etlforge.application.ui.job;

import com.google.common.base.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;
import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.ui.wait.WaitLock;
import org.etlforge.ui.wait.WaitService;

public abstract class KongaSwingWorker<T, V> extends SwingWorker<T, V> {
   private final Class<?> clientClass;
   private volatile String errorMessage;
   private volatile Optional<ErrorLevel> logLevel;

   public KongaSwingWorker() {
      this(null);
   }

   public KongaSwingWorker(Class<?> var1) {
      this.clientClass = var1;
      this.setLogLevel(ErrorLevel.ATTENTION);
   }

   public void setErrorMessage(String var1) {
      this.errorMessage = var1;
   }

   public void setLogLevel(ErrorLevel var1) {
      this.logLevel = Optional.of(var1);
   }

   public void doNotLogErrors() {
      this.logLevel = Optional.absent();
   }

   @Override
   protected final void done() {
      this.finished();
      if (!this.isCancelled()) {
         try {
            Object var1 = this.get();
            this.onSuccess((T)var1);
         } catch (InterruptedException var2) {
            Thread.currentThread().interrupt();
         } catch (ExecutionException var3) {
            this.onFailure(var3.getCause());
            this.logError(var3);
         }
      }
   }

   private void logError(ExecutionException var1) {
      if (this.logLevel.isPresent()) {
         ErrorLog.message(this.getClientClass(), this.logLevel.get(), this.errorMessage, var1.getCause());
      }
   }

   private Class<?> getClientClass() {
      return this.clientClass != null ? this.clientClass : this.getClass();
   }

   protected void finished() {
   }

   protected void onSuccess(T var1) {
   }

   protected void onFailure(Throwable var1) {
   }

   public static void runCallable(Callable<?> var0) {
      runCallableWhileWaiting(var0, WaitService.NONE);
   }

   public static void runCallableWhileWaiting(final Callable<?> var0, WaitService var1) {
      final WaitLock var2 = var1.startWait();
      KongaSwingWorker var3 = new KongaSwingWorker<Void, Void>() {
         protected Void doInBackground() throws Exception {
            var0.call();
            return null;
         }

         @Override
         protected void finished() {
            var2.release();
         }
      };
      var3.execute();
   }

   public static void runWhileWaiting(final Runnable var0, WaitService var1) {
      Callable var2 = new Callable<Void>() {
         public Void call() {
            var0.run();
            return null;
         }
      };
      runCallableWhileWaiting(var2, var1);
   }
}
