package org.etlforge.integration.server.engine.connector.framework;

public abstract class ConnectorEndpointAbstract implements ConnectorEndpoint {
   private ConnectorEndpointParameters connectorEndpointParamters;

   @Override
   public void setEndpointParameters(ConnectorEndpointParameters var1) {
      this.connectorEndpointParamters = var1;
   }

   @Override
   public String getEndpointTypeName() {
      return this.connectorEndpointParamters.getEndpointTypeName();
   }

   protected ConnectorEndpointParameters getEndpointParameters() {
      return this.connectorEndpointParamters;
   }
}
