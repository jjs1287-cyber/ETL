package org.etlforge.application.ui;

import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.lang.exception.ExceptionHandler;

public final class LoggingExceptionHandler implements ExceptionHandler<Throwable> {
   private final Object from;
   private final ErrorLevel level;
   private final String message;

   public static LoggingExceptionHandler log(Object var0) {
      return new LoggingExceptionHandler(var0, ErrorLevel.LOG);
   }

   public static LoggingExceptionHandler log(Object var0, String var1) {
      return new LoggingExceptionHandler(var0, ErrorLevel.LOG, var1);
   }

   public static LoggingExceptionHandler attention(Object var0) {
      return new LoggingExceptionHandler(var0, ErrorLevel.ATTENTION);
   }

   public static LoggingExceptionHandler attention(Object var0, String var1) {
      return new LoggingExceptionHandler(var0, ErrorLevel.ATTENTION, var1);
   }

   public static LoggingExceptionHandler internal(Object var0) {
      return new LoggingExceptionHandler(var0, ErrorLevel.INTERNAL);
   }

   public static LoggingExceptionHandler internal(Object var0, String var1) {
      return new LoggingExceptionHandler(var0, ErrorLevel.INTERNAL, var1);
   }

   public LoggingExceptionHandler(Object var1, String var2) {
      this(var1, ErrorLevel.LOG, var2);
   }

   public LoggingExceptionHandler(Object var1, ErrorLevel var2) {
      this(var1, var2, null);
   }

   public LoggingExceptionHandler(Object var1, ErrorLevel var2, String var3) {
      this.from = var1;
      this.level = var2;
      this.message = var3;
   }

   @Override
   public void caught(Throwable var1) {
      String var2 = "";
      if (this.message != null) {
         var2 = this.message + ": ";
      }

      var2 = var2 + var1.getMessage();
      ErrorLog.message(this.from, this.level, var2, var1);
   }
}
