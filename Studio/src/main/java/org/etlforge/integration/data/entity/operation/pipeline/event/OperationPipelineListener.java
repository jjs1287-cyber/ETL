package org.etlforge.integration.data.entity.operation.pipeline.event;

import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityListener;

public interface OperationPipelineListener extends PipelineActivityListener {
   void activityInserted(PipelineActivityInsertedEvent var1);

   void activityRemoved(PipelineActivityRemovedEvent var1);

   void pipelineChanged(OperationPipelineEvent var1);
}
