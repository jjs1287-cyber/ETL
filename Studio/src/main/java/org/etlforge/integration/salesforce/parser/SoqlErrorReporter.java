package org.etlforge.integration.salesforce.parser;

public interface SoqlErrorReporter {
   SoqlErrorReporter STDERR = new SoqlErrorReporter() {
      @Override
      public void reportError(String var1, String var2) {
         System.err.println(var1 + ": " + var2);
      }
   };

   void reportError(String var1, String var2);
}
