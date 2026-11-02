package org.etlforge.integration.data.connector.operation;

import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;

public final class MSAXOperationTemplate extends ConnectorCallOperationTemplate {
   public MSAXOperationTemplate() {
      super(MSAXOperationType.TYPE);
   }

   @Override
   protected PipelineActivity createEmptyFunctionActivity() {
      return new MSAXFunctionActivity("MSAX Function");
   }
}
