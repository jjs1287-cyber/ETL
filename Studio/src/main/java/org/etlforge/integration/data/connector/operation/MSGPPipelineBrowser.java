package org.etlforge.integration.data.connector.operation;

import org.etlforge.integration.data.connector.MSGPFunction;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.EntityTypeFilters;
import org.etlforge.integration.data.entity.operation.pipeline.FunctionCallPipelineBrowser;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;

public final class MSGPPipelineBrowser extends FunctionCallPipelineBrowser<MSGPFunction, MSGPFunctionActivity> {
   public MSGPPipelineBrowser(OperationPipeline var1) {
      super(var1, MSGPFunction.class, MSGPFunctionActivity.class, EntityTypeFilters.oneOf(EntityType.MSGPCreate, EntityType.MSGPUpdate, EntityType.MSGPDelete));
   }
}
