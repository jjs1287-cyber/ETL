package org.etlforge.integration.client.connector.mscrm.serverapi;

import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.mscrm.metadata.MSCrmObject;

public interface GetDetailedMetaDataCallback extends ServerCallback {
   void gotMetaData(MSCrmObject var1);
}
