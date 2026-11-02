package org.etlforge.integration.data.entity.operation.pipeline.event;

import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityEvent;

public abstract class OperationPipelineAdapter implements OperationPipelineListener {
   @Override
   public void activityInserted(PipelineActivityInsertedEvent var1) {
   }

   @Override
   public void activityRemoved(PipelineActivityRemovedEvent var1) {
   }

   @Override
   public void pipelineChanged(OperationPipelineEvent var1) {
   }

   @Override
   public void activityObjectChanged(PipelineActivityEvent var1) {
   }

   @Override
   public void contentChanged(PipelineActivityEvent var1) {
   }
}
