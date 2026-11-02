package com.etlforge.integration.server.engine.bulkload;

public interface ErrorLog {
   ErrorLog CONSOLE = new ErrorLog() {
      @Override
      public synchronized void writeError(String var1, Exception var2) {
         System.err.println(var1);
         var2.printStackTrace();
      }

      @Override
      public void close() {
      }
   };

   void writeError(String var1, Exception var2);

   void close();
}
