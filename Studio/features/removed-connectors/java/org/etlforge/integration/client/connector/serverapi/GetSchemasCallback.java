package org.etlforge.integration.client.connector.serverapi;

import org.etlforge.integration.client.server.ServerCallback;

public interface GetSchemasCallback extends ServerCallback {
   void handleSchemas(Schemas var1);
}
