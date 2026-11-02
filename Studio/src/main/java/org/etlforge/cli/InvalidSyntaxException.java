package org.etlforge.cli;

public final class InvalidSyntaxException extends RuntimeException {
   public InvalidSyntaxException() {
   }

   public InvalidSyntaxException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public InvalidSyntaxException(String var1) {
      super(var1);
   }

   public InvalidSyntaxException(Throwable var1) {
      super(var1);
   }
}
