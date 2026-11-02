package org.etlforge.integration.server.engine.connector.sap;

import java.util.Properties;
import org.etlforge.integration.server.engine.connector.framework.ConnectorEndpointAbstract;
import org.etlforge.integration.server.engine.connector.framework.ConnectorEndpointParameters;
import org.etlforge.integration.server.engine.connector.framework.ConnectorEngine;
import org.etlforge.integration.server.engine.connector.sap.adapter.ConnectorEndpointSapAdapter;

public class ConnectorEndpointSap extends ConnectorEndpointAbstract {
   @Override
   public boolean testConnection() {
      ConnectorEndpointSapAdapter var1 = this.getConnectorEndpointSapAdapter();
      return var1.testConnection();
   }

   @Override
   public ConnectorEngine newConnectorEngine() {
      return new ConnectorEngineSap(this);
   }

   public ConnectorEndpointSapAdapter getConnectorEndpointSapAdapter() {
      return new ConnectorEndpointSapAdapter(this.getSapEndpointProperties());
   }

   private static void putProperties(Properties var0, String var1, String var2) {
      var0.put(var1, var2 == null ? "" : var2);
   }

   private Properties getSapEndpointProperties() {
      ConnectorEndpointParameters var1 = this.getEndpointParameters();
      Properties var2 = new Properties();
      putProperties(var2, "ashost", var1.getHost());
      putProperties(var2, "user", var1.getUser());
      putProperties(var2, "password", var1.getPassword());
      Properties var3 = var1.getParameters();

      for (String var5 : var3.stringPropertyNames()) {
         putProperties(var2, var5, var3.getProperty(var5));
      }

      return var2;
   }
}
