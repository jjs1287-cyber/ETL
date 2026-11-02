package org.etlforge.integration.salesforce.wsdl;

public interface FileCacheErrorReporter {
   FileCacheErrorReporter STANDARD_ERR = new FileCacheErrorReporter() {
      @Override
      public void report(Exception var1, String var2) {
         var1.printStackTrace();
      }
   };

   void report(Exception var1, String var2);
}
