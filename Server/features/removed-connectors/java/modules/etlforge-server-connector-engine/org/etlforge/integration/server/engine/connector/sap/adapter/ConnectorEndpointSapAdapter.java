package org.etlforge.integration.server.engine.connector.sap.adapter;

import com.ubs.core.adapter.sap.idoc.connection.SapIDocDesignConnection;
import com.ubs.core.adapter.sap.idoc.connection.SapIDocExecutionConnection;
import com.ubs.core.adapter.sap.rfc.connection.SapDesignConnection;
import com.ubs.core.adapter.sap.rfc.connection.SapExecutionConnection;
import java.util.Properties;

public class ConnectorEndpointSapAdapter {
   private final Properties sapEndpointProperties;

   public ConnectorEndpointSapAdapter(Properties var1) {
      this.sapEndpointProperties = var1;
   }

   public boolean testConnection() {
      SapExecutionConnection var1 = null;

      try {
         var1 = this.getOpenExecutionConnection();
         return true;
      } finally {
         SapConnectorConnectionAdapter.closeConnection(var1);
      }
   }

   public SapDesignConnection getOpenDesignConnection() {
      Properties var1 = this.getSapEndpointProperties();
      return SapConnectorConnectionAdapter.getOpenDesignConnection(var1);
   }

   public SapIDocDesignConnection getOpenSapIDocDesignConnectionn() {
      Properties var1 = this.getSapEndpointProperties();
      return SapConnectorConnectionAdapter.getOpenSapIDocDesignConnection(var1);
   }

   public SapIDocExecutionConnection getOpenSapIDocExecutionConnection() {
      Properties var1 = this.getSapEndpointProperties();
      return SapConnectorConnectionAdapter.getOpenSapIDocExecutionConnection(var1);
   }

   public SapExecutionConnection getOpenExecutionConnection() {
      Properties var1 = this.getSapEndpointProperties();
      return SapConnectorConnectionAdapter.getOpenExecutionConnection(var1);
   }

   private Properties getSapEndpointProperties() {
      return this.sapEndpointProperties;
   }
}
