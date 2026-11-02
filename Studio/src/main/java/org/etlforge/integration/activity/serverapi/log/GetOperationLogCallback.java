package org.etlforge.integration.activity.serverapi.log;

import java.util.List;
import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.integration.client.server.ServerCallback;

public interface GetOperationLogCallback extends ServerCallback {
   void logWasRetrieved(List<OperationLogEntry> var1);
}
