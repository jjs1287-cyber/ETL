package org.etlforge.integration.server.engine.connector.netsuite.ws;

public class NetSuiteResponseStatusDetail {
   String type;
   String code;
   String message;

   @Override
   public String toString() {
      return this.type == null ? "code=" + this.code + ";message=" + this.message : this.type + ":code=" + this.code + ";message=" + this.message;
   }
}
