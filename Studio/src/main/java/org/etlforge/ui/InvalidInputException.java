package org.etlforge.ui;

public class InvalidInputException extends Exception {
   public InvalidInputException(String var1) {
      super(var1);
   }

   public InvalidInputException(Throwable var1) {
      super(var1);
   }

   public InvalidInputException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
