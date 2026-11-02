package org.etlforge.integration.data.entity.operation.pipeline;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.operation.pipeline.filter.EntityFilterProvider;
import org.etlforge.integration.data.function.Function;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.FunctionCallStructure;

public final class FunctionTransformationFilterProvider implements EntityFilterProvider<Transformation> {
   private final FunctionCallPipelineBrowser<?, ?> browser;
   private final Function function;

   public FunctionTransformationFilterProvider(FunctionCallPipelineBrowser<?, ?> var1) {
      this.browser = var1;
      this.function = (Function)var1.getFunction();
   }

   @Override
   public EntityFilter getFilter(PipelineActivity<Transformation> var1) {
      if (var1 == this.browser.getRequestActivity()) {
         return new FunctionTransformationFilterProvider.RequestFilter();
      } else if (var1 == this.browser.getResponseActivity()) {
         return new FunctionTransformationFilterProvider.ResponseFilter();
      } else {
         throw new IllegalArgumentException("activity is not part of this pipeline");
      }
   }

   private boolean isCompatibleEntityType(DataStructure var1) {
      if (var1 instanceof FunctionCallStructure) {
         EntityType var2 = ((FunctionCallStructure)var1).getFunctionCallId().getEntityType();
         return this.browser.isCompatibleWith(var2);
      } else {
         return false;
      }
   }

   private class RequestFilter implements EntityFilter {
      private RequestFilter() {
      }

      @Override
      public boolean apply(IntegrationEntity var1) {
         return var1 instanceof Transformation ? this.evaluate((Transformation)var1) : false;
      }

      private boolean evaluate(Transformation var1) {
         DataStructure var2 = var1.getOutputStructure();
         if (FunctionTransformationFilterProvider.this.function != null && !FunctionTransformationFilterProvider.this.function.getInputStructure().equals(var2)
            )
          {
            return false;
         } else {
            Source var3 = this.getSource();
            if (var3 != null && !var1.canGetInputFrom(var3)) {
               return false;
            } else {
               return FunctionTransformationFilterProvider.this.function == null
                  ? FunctionTransformationFilterProvider.this.isCompatibleEntityType(var2)
                  : var2.getStructureType() == FunctionTransformationFilterProvider.this.function.getInputStructure().getStructureType();
            }
         }
      }

      private Source getSource() {
         SourceActivity var1 = FunctionTransformationFilterProvider.this.browser.getSourceActivity();
         return var1 != null && !var1.isEmpty() ? var1.getContent() : null;
      }
   }

   private class ResponseFilter implements EntityFilter {
      private ResponseFilter() {
      }

      @Override
      public boolean apply(IntegrationEntity var1) {
         return var1 instanceof Transformation ? this.evaluate((Transformation)var1) : false;
      }

      private boolean evaluate(Transformation var1) {
         DataStructure var2 = var1.getInputStructure();
         if (FunctionTransformationFilterProvider.this.function != null
            && !FunctionTransformationFilterProvider.this.function.getOutputStructure().equals(var2)) {
            return false;
         } else {
            Target var3 = this.getTarget();
            if (var3 != null && !var1.canSendOutputTo(var3)) {
               return false;
            } else {
               return FunctionTransformationFilterProvider.this.function == null
                  ? FunctionTransformationFilterProvider.this.isCompatibleEntityType(var2)
                  : var2.getStructureType() == FunctionTransformationFilterProvider.this.function.getOutputStructure().getStructureType();
            }
         }
      }

      private Target getTarget() {
         TargetActivity var1 = FunctionTransformationFilterProvider.this.browser.getTargetActivity();
         return var1 != null && !var1.isEmpty() ? var1.getContent() : null;
      }
   }
}
