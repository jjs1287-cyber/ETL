package org.etlforge.integration.data.connector.operation;

import org.etlforge.integration.data.entity.operation.OperationType;
import org.etlforge.integration.data.entity.operation.pipeline.OperationTemplate;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.SourceActivity;
import org.etlforge.integration.data.entity.operation.pipeline.TargetActivity;
import org.etlforge.integration.data.entity.operation.pipeline.TransformationActivity;

public abstract class ConnectorCallOperationTemplate extends OperationTemplate {
   protected ConnectorCallOperationTemplate(OperationType var1) {
      super(var1);
   }

   @Override
   protected final PipelineActivity[] createChain() {
      return new PipelineActivity[]{
         new SourceActivity("Source"),
         new TransformationActivity("Request"),
         this.createEmptyFunctionActivity(),
         new TransformationActivity("Response"),
         new TargetActivity("Target")
      };
   }

   protected abstract PipelineActivity createEmptyFunctionActivity();
}
