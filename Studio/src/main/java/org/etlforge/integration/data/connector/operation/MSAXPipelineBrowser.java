package org.etlforge.integration.data.connector.operation;

import org.etlforge.integration.data.connector.MSAXFunction;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.EntityTypeFilters;
import org.etlforge.integration.data.entity.operation.pipeline.FunctionCallPipelineBrowser;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;

public final class MSAXPipelineBrowser extends FunctionCallPipelineBrowser<MSAXFunction, MSAXFunctionActivity> {
   public MSAXPipelineBrowser(OperationPipeline var1) {
      super(var1, MSAXFunction.class, MSAXFunctionActivity.class, EntityTypeFilters.is(EntityType.MSAXFunction));
   }
}
