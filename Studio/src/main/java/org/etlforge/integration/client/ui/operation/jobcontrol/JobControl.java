package org.etlforge.integration.client.ui.operation.jobcontrol;

import org.etlforge.integration.client.server.ServerCall;
import org.etlforge.integration.data.entity.id.OperationId;

public interface JobControl extends ServerCall {
   void runOperation(OperationId var1, RunOperationCallback var2);

   void runOperationWithSourceData(OperationId var1, String var2, RunOperationCallback var3);

   void runAllScheduledOperations(RunAllScheduledOperationsCallback var1);
}
