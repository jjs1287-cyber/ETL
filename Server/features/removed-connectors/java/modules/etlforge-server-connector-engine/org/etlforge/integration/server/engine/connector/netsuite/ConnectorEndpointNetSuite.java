package org.etlforge.integration.server.engine.connector.netsuite;

import org.etlforge.integration.server.engine.connector.framework.ConnectorEndpointAbstract;
import org.etlforge.integration.server.engine.connector.framework.ConnectorEndpointParameters;
import org.etlforge.integration.server.engine.connector.framework.ConnectorEngine;
import org.etlforge.integration.server.engine.connector.netsuite.adapter.ConnectorEndpointNetSuiteAdapter;

public class ConnectorEndpointNetSuite extends ConnectorEndpointAbstract {
   @Override
   public ConnectorEngine newConnectorEngine() {
      return new ConnectorEngineNetSuite(this);
   }

   @Override
   public boolean testConnection() {
      ConnectorEndpointNetSuiteAdapter var1 = this.getConnectorEndpointNetSuiteAdapter();
      return var1.testConnection();
   }

   public ConnectorEndpointNetSuiteAdapter getConnectorEndpointNetSuiteAdapter() {
      ConnectorEndpointParameters var1 = this.getEndpointParameters();
      return new ConnectorEndpointNetSuiteAdapter(var1);
   }
}
