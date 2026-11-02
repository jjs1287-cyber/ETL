package org.etlforge.application.errorlog;

public interface ErrorDetailsGenerator {
   ErrorDetailsGenerator DEFAULT = new ErrorDetailsGenerator() {
      @Override
      public String getDetails(ErrorLevel var1, Throwable var2) {
         if (var2 == null) {
            return null;
         }

         String var3 = var2.getMessage();
         return var3 != null && !var3.isEmpty() ? var3 : "An unexpected error occurred.";
      }
   };

   String getDetails(ErrorLevel var1, Throwable var2);
}
