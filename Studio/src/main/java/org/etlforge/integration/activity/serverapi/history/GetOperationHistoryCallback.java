package org.etlforge.integration.activity.serverapi.history;

import java.util.List;
import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.integration.client.server.ServerCallback;

public interface GetOperationHistoryCallback extends ServerCallback {
   void historyWasRetrieved(List<OperationLogEntry> var1);
}
