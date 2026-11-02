package org.etlforge.integration.activity.serverapi.queue;

import java.util.List;
import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.integration.client.server.ServerCallback;

public interface GetOperationQueueCallback extends ServerCallback {
   void queueWasRetrieved(List<OperationLogEntry> var1);
}
