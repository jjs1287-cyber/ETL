package org.etlforge.integration.activity.serverapi.history;

import org.etlforge.integration.client.server.ServerCall;

public interface OperationHistoryProvider extends ServerCall {
   void getOperationHistory(HistoryRequest var1, GetOperationHistoryCallback var2);
}
