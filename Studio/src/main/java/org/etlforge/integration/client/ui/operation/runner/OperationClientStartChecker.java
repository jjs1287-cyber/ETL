package org.etlforge.integration.client.ui.operation.runner;

import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.operation.OperationExecutionMode;
import org.etlforge.integration.data.entity.operation.RunnableStatus;

public class OperationClientStartChecker {
   private final OperationExecutionMode mode;

   public OperationClientStartChecker(OperationExecutionMode var1) {
      this.mode = var1;
   }

   public RunnableStatus checkRunnableStatus(Operation var1) {
      RunnableStatus var2 = var1.getOperationType().checkRunnable(this.mode);
      if (!var2.isRunnable()) {
         return var2;
      } else if (var1.getsSourceFromPreviousOperation()) {
         return this.notRunnable("NotRunnable.SourceFromPreviousOperation");
      } else {
         return var1.getsSourceFromHttpEndpoint() ? this.notRunnable("NotRunnable.SourceFromHttpEndpoint") : RunnableStatus.RUNNABLE;
      }
   }

   private RunnableStatus notRunnable(String var1) {
      String var2 = this.getNotRunnableMessage(var1);
      return RunnableStatus.notRunnable(var2);
   }

   private String getNotRunnableMessage(String var1) {
      return Strings.format(var1, Strings.get("RunOperation.Mode." + this.mode.name()));
   }
}
