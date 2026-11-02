package org.etlforge.integration.data.entity.operation.pipeline.event;

import java.util.EventObject;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;

public class OperationPipelineEvent extends EventObject {
   public OperationPipelineEvent(OperationPipeline var1) {
      super(var1);
   }

   public final OperationPipeline getSource() {
      return (OperationPipeline)super.getSource();
   }
}
