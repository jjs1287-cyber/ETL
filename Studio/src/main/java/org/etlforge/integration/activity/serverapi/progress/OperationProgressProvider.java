package org.etlforge.integration.activity.serverapi.progress;

import org.etlforge.integration.client.server.ServerCall;

public interface OperationProgressProvider extends ServerCall {
   void getOperationProgress(OperationProgressRequest var1, OperationProgressCallback var2);
}
