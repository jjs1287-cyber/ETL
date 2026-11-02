package org.etlforge.integration.server.engine.connector.framework;

public interface ConnectorEndpoint {
   void setEndpointParameters(ConnectorEndpointParameters var1);

   String getEndpointTypeName();

   ConnectorEngine newConnectorEngine();

   boolean testConnection();
}
