package org.etlforge.lang.exception;

public final class NotImplementedYetException extends RuntimeException {
   public NotImplementedYetException() {
      super("This feature is not yet implemented");
   }

   public NotImplementedYetException(String var1) {
      super(var1);
   }
}
