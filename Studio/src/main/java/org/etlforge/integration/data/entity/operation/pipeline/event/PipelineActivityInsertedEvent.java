package org.etlforge.integration.data.entity.operation.pipeline.event;

import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;

public final class PipelineActivityInsertedEvent extends OperationPipelineEvent {
   private final PipelineActivity previous;
   private final PipelineActivity next;
   private final PipelineActivity inserted;

   public PipelineActivityInsertedEvent(OperationPipeline var1, PipelineActivity var2, PipelineActivity var3, PipelineActivity var4) {
      super(var1);
      this.previous = var2;
      this.next = var3;
      this.inserted = var4;
   }

   public PipelineActivity getInsertedActivity() {
      return this.inserted;
   }

   public PipelineActivity getPrevious() {
      return this.previous;
   }

   public PipelineActivity getNext() {
      return this.next;
   }
}
