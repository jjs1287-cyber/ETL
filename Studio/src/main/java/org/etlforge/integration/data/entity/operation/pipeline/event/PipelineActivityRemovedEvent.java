package org.etlforge.integration.data.entity.operation.pipeline.event;

import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;

public final class PipelineActivityRemovedEvent extends OperationPipelineEvent {
   private final PipelineActivity removed;

   public PipelineActivityRemovedEvent(OperationPipeline var1, PipelineActivity var2) {
      super(var1);
      this.removed = var2;
   }

   public final PipelineActivity getRemovedActivity() {
      return this.removed;
   }
}
