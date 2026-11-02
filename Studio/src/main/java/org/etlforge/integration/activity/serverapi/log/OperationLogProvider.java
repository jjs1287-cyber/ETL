package org.etlforge.integration.activity.serverapi.log;

import org.etlforge.integration.client.server.ServerCall;

public interface OperationLogProvider extends ServerCall {
   void getOperationLog(OperationLogRequest var1, GetOperationLogCallback var2);
}
