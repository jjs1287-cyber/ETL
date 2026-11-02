package org.etlforge.integration.client.connector.serverapi;

import org.etlforge.integration.client.server.ServerCall;
import org.etlforge.integration.data.connector.ConnectorEndpoint;

public interface TestConnectorConnectionApi extends ServerCall {
   void testConnection(ConnectorEndpoint var1, TestConnectionCallback var2);
}
