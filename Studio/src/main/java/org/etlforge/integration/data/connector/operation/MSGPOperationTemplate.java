package org.etlforge.integration.data.connector.operation;

import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;

public final class MSGPOperationTemplate extends ConnectorCallOperationTemplate {
   public MSGPOperationTemplate() {
      super(MSGPOperationType.TYPE);
   }

   @Override
   protected PipelineActivity createEmptyFunctionActivity() {
      return new MSGPFunctionActivity("MSGP Function");
   }
}
