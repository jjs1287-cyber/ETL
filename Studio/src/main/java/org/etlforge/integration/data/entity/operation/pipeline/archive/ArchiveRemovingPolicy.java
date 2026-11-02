package org.etlforge.integration.data.entity.operation.pipeline.archive;

import org.etlforge.integration.data.entity.operation.pipeline.ActivityRemovingPolicy;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.RemoveActivityVetoException;
import org.etlforge.integration.data.entity.operation.pipeline.SourceActivity;
import org.etlforge.integration.data.entity.operation.pipeline.TargetActivity;

public final class ArchiveRemovingPolicy implements ActivityRemovingPolicy {
   @Override
   public void checkRemovalAllowed(PipelineActivity var1) throws RemoveActivityVetoException {
      if (var1 instanceof SourceActivity) {
         throw new RemoveActivityVetoException("The Source cannot be removed from an Archive operation.");
      }

      if (var1 instanceof TargetActivity) {
         throw new RemoveActivityVetoException("The Target cannot be removed from an Archive operation.");
      }
   }
}
