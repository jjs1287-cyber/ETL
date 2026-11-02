package org.etlforge.lang.exception;

import com.google.common.base.Throwables;

public final class ExceptionUtils {
   public static String getStackTrace(Throwable var0) {
      return Throwables.getStackTraceAsString(var0);
   }

   public static <T extends Throwable> void handle(T var0, ExceptionHandler<? super T> var1) {
      if (var1 == null) {
         var0.printStackTrace();
      } else {
         var1.caught(var0);
      }
   }

   private ExceptionUtils() {
   }
}
