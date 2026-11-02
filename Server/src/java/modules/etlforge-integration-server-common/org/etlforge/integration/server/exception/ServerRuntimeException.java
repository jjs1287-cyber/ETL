package org.etlforge.integration.server.exception;

public class ServerRuntimeException extends RuntimeException {
   public ServerRuntimeException(String var1) {
      super(var1);
   }

   public ServerRuntimeException(Throwable var1) {
      super(var1);
   }

   public ServerRuntimeException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
