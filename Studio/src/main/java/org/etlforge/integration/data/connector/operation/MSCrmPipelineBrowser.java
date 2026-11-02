package org.etlforge.integration.data.connector.operation;

import org.etlforge.integration.data.connector.MSCrmFunction;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.EntityTypeFilters;
import org.etlforge.integration.data.entity.operation.pipeline.FunctionCallPipelineBrowser;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;

public final class MSCrmPipelineBrowser extends FunctionCallPipelineBrowser<MSCrmFunction, MSCrmFunctionActivity> {
   public MSCrmPipelineBrowser(OperationPipeline var1) {
      super(
         var1,
         MSCrmFunction.class,
         MSCrmFunctionActivity.class,
         EntityTypeFilters.oneOf(EntityType.MSCrmCreate, EntityType.MSCrmUpdate, EntityType.MSCrmDelete)
      );
   }
}
