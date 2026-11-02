package org.etlforge.integration.data.entity.operation.pipeline.ws;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.EntityTypeFilters;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.entity.operation.pipeline.FunctionCallPipelineBrowser;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.WebServiceCallActivity;

public final class WebServicePipelineBrowser extends FunctionCallPipelineBrowser<WebServiceCall, WebServiceCallActivity> {
   public WebServicePipelineBrowser(OperationPipeline var1) {
      super(var1, WebServiceCall.class, WebServiceCallActivity.class, EntityTypeFilters.is(EntityType.WebServiceCall));
   }
}
