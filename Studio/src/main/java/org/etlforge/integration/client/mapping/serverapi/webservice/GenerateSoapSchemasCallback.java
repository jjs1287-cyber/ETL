package org.etlforge.integration.client.mapping.serverapi.webservice;

import org.etlforge.integration.client.server.ServerCallback;

public interface GenerateSoapSchemasCallback extends ServerCallback {
   void filesWereGenerated();

   void fileConflictsDetected(String[] var1);
}
