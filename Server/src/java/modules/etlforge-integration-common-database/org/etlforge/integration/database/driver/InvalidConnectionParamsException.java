package org.etlforge.integration.database.driver;

public class InvalidConnectionParamsException extends Exception {
   private final ConnectionParams params;

   public InvalidConnectionParamsException(ConnectionParams var1, String var2) {
      super(var2);
      this.params = var1;
   }

   public ConnectionParams getParamters() {
      return this.params;
   }
}
