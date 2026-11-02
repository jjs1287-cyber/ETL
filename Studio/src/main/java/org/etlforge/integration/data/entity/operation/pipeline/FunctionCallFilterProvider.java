package org.etlforge.integration.data.entity.operation.pipeline;

import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.operation.pipeline.filter.EntityFilterProvider;
import org.etlforge.integration.data.function.Function;
import org.etlforge.integration.data.functors.EntityFilter;

public final class FunctionCallFilterProvider<E extends IntegrationEntity, T extends EntityActivity<E>> implements EntityFilterProvider<E> {
   private final FunctionCallPipelineBrowser<?, T> browser;

   public FunctionCallFilterProvider(FunctionCallPipelineBrowser<?, T> var1) {
      this.browser = var1;
   }

   @Override
   public EntityFilter getFilter(PipelineActivity<E> var1) {
      return new FunctionCallFilterProvider.FilterImpl(this.browser);
   }

   private static class FilterImpl implements EntityFilter {
      private final FunctionCallPipelineBrowser<?, ?> browser;

      public FilterImpl(FunctionCallPipelineBrowser<?, ?> var1) {
         this.browser = var1;
      }

      @Override
      public boolean apply(IntegrationEntity var1) {
         if (!(var1 instanceof Function)) {
            return false;
         }

         Function var2 = (Function)var1;
         return this.isMatchWithRequestTransformation(var2) && this.isMatchWithResponseTransformation(var2);
      }

      private boolean isMatchWithRequestTransformation(Function var1) {
         Transformation var2 = this.browser.getRequest();
         return var2 == null || var1.getInputStructure().equals(var2.getOutputStructure());
      }

      private boolean isMatchWithResponseTransformation(Function var1) {
         Transformation var2 = this.browser.getResponse();
         return var2 == null || var1.getOutputStructure().equals(var2.getInputStructure());
      }
   }
}
