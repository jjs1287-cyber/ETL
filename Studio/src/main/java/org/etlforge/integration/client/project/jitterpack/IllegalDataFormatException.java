package org.etlforge.integration.client.project.jitterpack;

public class IllegalDataFormatException extends JitterPackException {
   public IllegalDataFormatException() {
   }

   public IllegalDataFormatException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public IllegalDataFormatException(String var1) {
      super(var1);
   }

   public IllegalDataFormatException(Throwable var1) {
      super(var1);
   }

   @Override
   public String getMessage() {
      String var1 = super.getMessage();
      return var1 != null && var1.trim().length() != 0 ? var1 : "The file is not a valid Jitterpak.";
   }
}
