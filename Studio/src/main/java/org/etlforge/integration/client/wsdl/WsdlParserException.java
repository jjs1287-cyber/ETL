package org.etlforge.integration.client.wsdl;

public class WsdlParserException extends Exception {
   public WsdlParserException(String var1) {
      super(var1);
   }

   public WsdlParserException(Throwable var1) {
      super(var1);
   }

   public WsdlParserException(String var1, Throwable var2) {
      super(var1, var2);
   }

   @Override
   public String getMessage() {
      String var1 = super.getMessage();
      Throwable var2 = this.getCause();
      if (var2 != null) {
         var1 = var1 + " " + var2.getMessage();
      }

      return var1;
   }
}
