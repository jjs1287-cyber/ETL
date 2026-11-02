package org.etlforge.integration.client.server.login;

public class ServerLoginException extends Exception {
   private final String userName;

   public ServerLoginException(String var1, String var2) {
      super(var2);
      this.userName = var1;
   }

   public ServerLoginException(String var1, Throwable var2) {
      super(var2);
      this.userName = var1;
   }

   public ServerLoginException(String var1, String var2, Throwable var3) {
      super(var2, var3);
      this.userName = var1;
   }

   public String getUserName() {
      return this.userName;
   }
}
