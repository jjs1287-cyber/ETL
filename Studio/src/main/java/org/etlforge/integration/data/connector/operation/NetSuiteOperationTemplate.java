package org.etlforge.integration.data.connector.operation;

import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;

public final class NetSuiteOperationTemplate extends ConnectorCallOperationTemplate {
   public NetSuiteOperationTemplate() {
      super(NetSuiteOperationType.TYPE);
   }

   @Override
   protected PipelineActivity createEmptyFunctionActivity() {
      return new NetSuiteFunctionActivity("NetSuite Function");
   }
}
