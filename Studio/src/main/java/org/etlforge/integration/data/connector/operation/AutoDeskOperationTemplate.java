package org.etlforge.integration.data.connector.operation;

import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;

public final class AutoDeskOperationTemplate extends ConnectorCallOperationTemplate {
   public AutoDeskOperationTemplate() {
      super(AutoDeskOperationType.TYPE);
   }

   @Override
   protected PipelineActivity createEmptyFunctionActivity() {
      return new AutoDeskFunctionActivity("PLM 360 Function");
   }
}
