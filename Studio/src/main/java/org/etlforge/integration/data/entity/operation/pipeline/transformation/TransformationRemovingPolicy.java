package org.etlforge.integration.data.entity.operation.pipeline.transformation;

import org.etlforge.integration.data.entity.operation.pipeline.ActivityRemovingPolicy;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityType;
import org.etlforge.integration.data.entity.operation.pipeline.RemoveActivityVetoException;

public final class TransformationRemovingPolicy implements ActivityRemovingPolicy {
   @Override
   public void checkRemovalAllowed(PipelineActivity var1) throws RemoveActivityVetoException {
      if (var1.getType() == PipelineActivityType.TRANSFORMATION) {
         throw new RemoveActivityVetoException("The Transformation cannot be removed from an Operation of type Transformation.");
      }
   }
}
