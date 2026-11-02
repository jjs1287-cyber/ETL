package org.etlforge.integration.server.engine.connector.exception;

public class ConnectorRuntimeException extends RuntimeException {
   public ConnectorRuntimeException(String var1) {
      super(var1);
   }

   public ConnectorRuntimeException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public ConnectorRuntimeException(Throwable var1) {
      super(var1);
   }
}
