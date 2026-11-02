package org.etlforge.integration.server.engine.connector.msax.adapter;

public class MsAXOperation {
   private final String serviceName;
   private final String operationName;

   public MsAXOperation(String var1, String var2) {
      this.serviceName = var1;
      this.operationName = var2;
   }

   public String getServiceName() {
      return this.serviceName;
   }

   public String getOperationName() {
      return this.operationName;
   }
}
