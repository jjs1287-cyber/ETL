package org.etlforge.integration.client.connector.serverapi;

import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;

public interface ListObjectsCallback extends ServerCallback {
   void handleObjectList(Iterable<? extends ConnectorObject> var1);
}
