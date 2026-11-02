package org.etlforge.integration.data.entity.operation.pipeline.hostedws;

import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.entity.operation.HostedWebServiceOperationType;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.TransformationActivity;
import org.etlforge.integration.data.entity.operation.pipeline.WebServiceCallActivity;
import org.etlforge.integration.data.entity.operation.pipeline.filter.EntityFilterProvider;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.structure.DataStructureType;

public final class HostedWebServiceTransformationFilterProvider implements EntityFilterProvider<Transformation> {
   private final OperationPipeline pipeline;

   public HostedWebServiceTransformationFilterProvider(OperationPipeline var1) {
      assert var1.getType() == HostedWebServiceOperationType.TYPE : "Wrong operation type: " + var1.getType();
      this.pipeline = var1;
   }

   @Override
   public EntityFilter getFilter(PipelineActivity<Transformation> var1) {
      WebServiceCall var2 = this.getWebServiceCall();
      if (var1 == this.pipeline.getFirstActivityOfType(TransformationActivity.class)) {
         return new HostedWebServiceTransformationFilterProvider.FirstTransformationFilter(var2);
      } else if (var1 == this.pipeline.getLastActivityOfType(TransformationActivity.class)) {
         return new HostedWebServiceTransformationFilterProvider.SecondTransformationFilter(var2);
      } else {
         throw new IllegalArgumentException("entry is not part of this pipeline");
      }
   }

   private WebServiceCall getWebServiceCall() {
      PipelineActivity var1 = this.pipeline.getFirstActivityOfType(WebServiceCallActivity.class);
      return var1 != null && !var1.isEmpty() ? (WebServiceCall)var1.getContent() : null;
   }

   private Transformation getFirstTransformation() {
      TransformationActivity var1 = this.pipeline.getFirstActivityOfType(TransformationActivity.class);
      return var1 != null && !var1.isEmpty() ? var1.getContent() : null;
   }

   private Transformation getSecondTransformation() {
      TransformationActivity var1 = this.pipeline.getLastActivityOfType(TransformationActivity.class);
      return var1 != null && !var1.isEmpty() ? var1.getContent() : null;
   }

   private class FirstTransformationFilter implements EntityFilter {
      private final WebServiceCall wsCall;

      public FirstTransformationFilter(WebServiceCall var2) {
         this.wsCall = var2;
      }

      @Override
      public boolean apply(IntegrationEntity var1) {
         return var1 instanceof Transformation ? this.evaluate((Transformation)var1) : false;
      }

      private boolean evaluate(Transformation var1) {
         if (this.wsCall != null && !this.wsCall.getInputStructure().equals(var1.getInputStructure())) {
            return false;
         }

         Transformation var2 = HostedWebServiceTransformationFilterProvider.this.getSecondTransformation();
         return var2 != null && !var2.acceptsOutputFrom(var1) ? false : var1.getInputStructure().getStructureType() == DataStructureType.WebServiceRequest;
      }
   }

   private class SecondTransformationFilter implements EntityFilter {
      private final WebServiceCall wsCall;

      public SecondTransformationFilter(WebServiceCall var2) {
         this.wsCall = var2;
      }

      @Override
      public boolean apply(IntegrationEntity var1) {
         return var1 instanceof Transformation ? this.evaluate((Transformation)var1) : false;
      }

      private boolean evaluate(Transformation var1) {
         if (this.wsCall != null && !this.wsCall.getOutputStructure().equals(var1.getOutputStructure())) {
            return false;
         }

         Transformation var2 = HostedWebServiceTransformationFilterProvider.this.getFirstTransformation();
         return var2 != null && !var1.acceptsOutputFrom(var2) ? false : var1.getOutputStructure().getStructureType() == DataStructureType.WebServiceResponse;
      }
   }
}
