package org.etlforge.integration.data.connector.operation;

import org.etlforge.integration.data.connector.EBSFunction;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.EntityTypeFilters;
import org.etlforge.integration.data.entity.operation.pipeline.FunctionCallPipelineBrowser;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;

public final class EBSPipelineBrowser extends FunctionCallPipelineBrowser<EBSFunction, EBSFunctionActivity> {
   public EBSPipelineBrowser(OperationPipeline var1) {
      super(var1, EBSFunction.class, EBSFunctionActivity.class, EntityTypeFilters.is(EntityType.EBSFunction));
   }
}
