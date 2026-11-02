package org.etlforge.integration.server.services;

class WindowsServerRestarter extends ServerRestarter {
   private static final String APACHE = "ETLForge Apache Server";
   private static final String PROCESS_ENGINE = "ETLForge Process Engine";

   @Override
   public void restartServer() throws ServerServiceException {
      try {
         this.stopService("ETLForge Apache Server");
         this.stopService("ETLForge Process Engine");
         this.startService("ETLForge Apache Server");
         this.startService("ETLForge Process Engine");
      } catch (Exception var2) {
         throw new ServerServiceException("Failed to restart the ETLForge server: " + var2.getMessage(), var2);
      }
   }

   private void stopService(String var1) throws Exception {
      String[] var2 = new String[]{"net", "stop", var1};
      new ProcessRunner(var2).run();
   }

   private void startService(String var1) throws Exception {
      String[] var2 = new String[]{"net", "start", var1};
      new ProcessRunner(var2).run();
   }
}
