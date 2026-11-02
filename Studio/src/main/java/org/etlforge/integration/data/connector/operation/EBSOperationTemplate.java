package org.etlforge.integration.data.connector.operation;

import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;

public final class EBSOperationTemplate extends ConnectorCallOperationTemplate {
   public EBSOperationTemplate() {
      super(EBSOperationType.TYPE);
   }

   @Override
   protected PipelineActivity createEmptyFunctionActivity() {
      return new EBSFunctionActivity("EBS Program");
   }
}
