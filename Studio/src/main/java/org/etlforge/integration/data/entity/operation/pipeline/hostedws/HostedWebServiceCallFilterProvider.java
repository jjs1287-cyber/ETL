package org.etlforge.integration.data.entity.operation.pipeline.hostedws;

import com.google.common.base.Preconditions;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.entity.operation.HostedWebServiceOperationType;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.filter.EntityFilterProvider;
import org.etlforge.integration.data.functors.EntityFilter;

public final class HostedWebServiceCallFilterProvider implements EntityFilterProvider<WebServiceCall> {
   private final OperationPipeline pipeline;

   public HostedWebServiceCallFilterProvider(OperationPipeline var1) {
      Preconditions.checkArgument(var1.getType() == HostedWebServiceOperationType.TYPE, "The pipeline must be a hosted web service");
      this.pipeline = var1;
   }

   @Override
   public EntityFilter getFilter(PipelineActivity<WebServiceCall> var1) {
      return new HostedWebServiceCallFilterProvider.FilterImpl(this.pipeline);
   }

   private static class FilterImpl implements EntityFilter {
      private final HostedWebServicePipelineBrowser browser;

      public FilterImpl(OperationPipeline var1) {
         this.browser = new HostedWebServicePipelineBrowser(var1);
      }

      @Override
      public boolean apply(IntegrationEntity var1) {
         return var1 instanceof WebServiceCall ? this.isMatchWithFirstTransformation((WebServiceCall)var1) : false;
      }

      private boolean isMatchWithFirstTransformation(WebServiceCall var1) {
         Transformation var2 = this.browser.getFirstTransformation();
         return var2 == null || var1.getInputStructure().equals(var2.getInputStructure());
      }
   }
}
