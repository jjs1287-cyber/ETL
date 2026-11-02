package org.etlforge.integration.client.connector.mscrm.serverapi;

import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.mscrm.metadata.MSCrmObject;

public interface GetCompleteMetaDataCallback extends ServerCallback {
   void gotMetaData(Iterable<MSCrmObject> var1);
}
