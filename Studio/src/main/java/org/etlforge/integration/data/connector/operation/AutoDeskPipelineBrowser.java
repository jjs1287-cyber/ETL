package org.etlforge.integration.data.connector.operation;

import org.etlforge.integration.data.connector.AutoDeskFunction;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.EntityTypeFilters;
import org.etlforge.integration.data.entity.operation.pipeline.FunctionCallPipelineBrowser;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;

public final class AutoDeskPipelineBrowser extends FunctionCallPipelineBrowser<AutoDeskFunction, AutoDeskFunctionActivity> {
   public AutoDeskPipelineBrowser(OperationPipeline var1) {
      super(
         var1,
         AutoDeskFunction.class,
         AutoDeskFunctionActivity.class,
         EntityTypeFilters.oneOf(EntityType.AutoDeskCreate, EntityType.AutoDeskUpdate, EntityType.AutoDeskDelete, EntityType.AutoDeskGet)
      );
   }
}
