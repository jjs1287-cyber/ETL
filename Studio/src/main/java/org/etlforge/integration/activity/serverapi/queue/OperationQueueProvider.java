package org.etlforge.integration.activity.serverapi.queue;

import org.etlforge.integration.client.server.ServerCall;

public interface OperationQueueProvider extends ServerCall {
   void getOperationQueue(OperationQueueRequest var1, GetOperationQueueCallback var2);

   void deleteQueueEntries(long[] var1, DeleteQueueEntriesCallback var2);

   void deleteSystemQueue(DeleteSystemQueueCallback var1);
}
