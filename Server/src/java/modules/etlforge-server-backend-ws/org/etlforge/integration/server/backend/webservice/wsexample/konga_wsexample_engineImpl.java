package org.etlforge.integration.server.backend.webservice.wsexample;

public class konga_wsexample_engineImpl {
   public WsStatus testExample(WsParameters var1) {
      byte var2 = 1;
      boolean var3 = true;
      String var4 = var1.getUser() + var1.getPassword();
      return new WsStatus(var2, var3, var4);
   }

   public void testNoOutput(WsParameters var1) {
      String var2 = var1.getUser() + var1.getPassword();
      System.out.print(var2);
   }

   public WsStatus testNoInput() {
      int var1 = (int)System.currentTimeMillis() / 1000;
      boolean var2 = false;
      String var3 = "Time:" + System.nanoTime();
      return new WsStatus(var1, var2, var3);
   }

   public void testNoInputOutput() {
      throw new RuntimeException("Not Implemented!");
   }
}
