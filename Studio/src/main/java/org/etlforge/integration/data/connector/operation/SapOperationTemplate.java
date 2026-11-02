package org.etlforge.integration.data.connector.operation;

import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;

public final class SapOperationTemplate extends ConnectorCallOperationTemplate {
   public SapOperationTemplate() {
      super(SapOperationType.TYPE);
   }

   @Override
   protected PipelineActivity createEmptyFunctionActivity() {
      return new SapFunctionActivity("SAP Function");
   }
}
