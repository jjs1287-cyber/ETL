package org.etlforge.integration.server.engine.connector.endpoint;

import org.etlforge.integration.server.engine.connector.exception.ConnectorServerAccessRuntimeException;
import org.etlforge.integration.server.engine.connector.framework.ConnectorEndpoint;

public class ConnectorEndpointTypeManager {
   final ConnectorEndpointTypeConfig config = new ConnectorEndpointTypeConfig();

   private ConnectorEndpointType getEndpointType(String var1) {
      ConnectorEndpointType var2 = this.config.getConfiggedEndpointTypes().get(var1);
      if (var2 == null) {
         String var3 = "Connector Server cannot locate the connector " + var1;
         throw new ConnectorServerAccessRuntimeException(var3);
      } else {
         return var2;
      }
   }

   public ConnectorEndpoint newConnectorEndpoint(String var1) {
      ConnectorEndpoint var2 = this.getEndpointType(var1).newConnectorEndpoint();
      if (var2 == null) {
         String var3 = "Connector Server cannot create connection for the connector " + var1;
         throw new ConnectorServerAccessRuntimeException(var3);
      } else {
         return var2;
      }
   }
}
