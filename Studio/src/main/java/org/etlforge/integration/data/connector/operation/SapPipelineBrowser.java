package org.etlforge.integration.data.connector.operation;

import org.etlforge.integration.data.connector.SapFunction;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.EntityTypeFilters;
import org.etlforge.integration.data.entity.operation.pipeline.FunctionCallPipelineBrowser;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;

public final class SapPipelineBrowser extends FunctionCallPipelineBrowser<SapFunction, SapFunctionActivity> {
   public SapPipelineBrowser(OperationPipeline var1) {
      super(var1, SapFunction.class, SapFunctionActivity.class, EntityTypeFilters.is(EntityType.SapFunction));
   }
}
