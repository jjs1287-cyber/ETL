package org.etlforge.integration.client.connector.mscrm.serverapi;

import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.etlforgeconnector.SchemaData;

public interface GetResponseSchemaCallback extends ServerCallback {
   void gotResponseSchema(SchemaData var1);
}
