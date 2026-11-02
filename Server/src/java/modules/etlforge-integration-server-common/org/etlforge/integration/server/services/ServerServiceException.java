package org.etlforge.integration.server.services;

public class ServerServiceException extends Exception {
   public ServerServiceException(String var1) {
      super(var1);
   }

   public ServerServiceException(Throwable var1) {
      super(var1);
   }

   public ServerServiceException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
