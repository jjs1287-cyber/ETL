package org.etlforge.integration.data.entity.operation.pipeline.script;

import org.etlforge.integration.data.entity.operation.pipeline.ActivityRemovingPolicy;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityCounter;
import org.etlforge.integration.data.entity.operation.pipeline.RemoveActivityVetoException;

public final class ScriptRemovingPolicy implements ActivityRemovingPolicy {
   private final OperationPipeline pipeline;

   public ScriptRemovingPolicy(OperationPipeline var1) {
      this.pipeline = var1;
   }

   @Override
   public void checkRemovalAllowed(PipelineActivity var1) throws RemoveActivityVetoException {
      if (this.isLastScript()) {
         throw new RemoveActivityVetoException("The last Script cannot be removed from a script operation");
      }
   }

   private boolean isLastScript() {
      return PipelineActivityCounter.countScripts(this.pipeline) == 1;
   }
}
