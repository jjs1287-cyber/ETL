package org.etlforge.lang.exception;

public class ExceptionHandlers {
   public static <T extends Throwable> ExceptionHandler<T> toStandardErr() {
      return new ExceptionHandler<T>() {
         @Override
         public void caught(T var1) {
            var1.printStackTrace();
         }
      };
   }

   private ExceptionHandlers() {
   }
}
