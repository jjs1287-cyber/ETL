package org.etlforge.application.ui;

import java.awt.EventQueue;
import java.lang.Thread.UncaughtExceptionHandler;
import org.etlforge.application.errorlog.ErrorLog;

public final class UncaughtExceptionLogger implements UncaughtExceptionHandler {
   @Override
   public void uncaughtException(Thread var1, Throwable var2) {
      this.handle(var2);
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            Thread.currentThread().setUncaughtExceptionHandler(UncaughtExceptionLogger.this);
         }
      });
   }

   public void handle(Throwable var1) {
      String var2 = var1 instanceof OutOfMemoryError ? "The client has run out of memory." : "An unexpected error occurred.";
      ErrorLog.log(ApplicationUi.class, var2, var1);
      var1.printStackTrace();
   }
}
