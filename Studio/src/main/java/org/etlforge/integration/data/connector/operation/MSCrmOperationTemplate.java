package org.etlforge.integration.data.connector.operation;

import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;

public final class MSCrmOperationTemplate extends ConnectorCallOperationTemplate {
   public MSCrmOperationTemplate() {
      super(MSCrmOperationType.TYPE);
   }

   @Override
   protected PipelineActivity createEmptyFunctionActivity() {
      return new MSCrmFunctionActivity("MSCRM Function");
   }
}
