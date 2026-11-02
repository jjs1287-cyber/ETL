package org.etlforge.integration.server.engine.connector.exception;

public class ConnectorInvalidInputRuntimeException extends ConnectorRuntimeException {
   static String ErrorMessageHeader = "Invalid Input: ";

   public ConnectorInvalidInputRuntimeException(String var1) {
      super(ErrorMessageHeader + var1);
   }
}
