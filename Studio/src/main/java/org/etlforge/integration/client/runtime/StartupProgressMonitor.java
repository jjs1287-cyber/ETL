package org.etlforge.integration.client.runtime;

public interface StartupProgressMonitor {
   StartupProgressMonitor CONSOLE = new StartupProgressMonitor() {
      @Override
      public void publishMessage(String var1) {
         System.out.println(var1);
      }
   };

   void publishMessage(String var1);
}
