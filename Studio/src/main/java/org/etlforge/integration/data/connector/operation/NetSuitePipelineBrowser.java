package org.etlforge.integration.data.connector.operation;

import org.etlforge.integration.data.connector.NetSuiteFunction;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.EntityTypeFilters;
import org.etlforge.integration.data.entity.operation.pipeline.FunctionCallPipelineBrowser;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;

public final class NetSuitePipelineBrowser extends FunctionCallPipelineBrowser<NetSuiteFunction, NetSuiteFunctionActivity> {
   public NetSuitePipelineBrowser(OperationPipeline var1) {
      super(
         var1,
         NetSuiteFunction.class,
         NetSuiteFunctionActivity.class,
         EntityTypeFilters.oneOf(EntityType.NetSuiteCreate, EntityType.NetSuiteUpdate, EntityType.NetSuiteDelete)
      );
   }
}
