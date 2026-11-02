package org.etlforge.integration.data.entity.operation.pipeline.transformation;

import com.google.common.base.Preconditions;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.TransformationActivity;
import org.etlforge.integration.data.entity.operation.pipeline.filter.EntityFilterProvider;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.functors.EntityFilters;

public final class TransformationSourceFilterProvider implements EntityFilterProvider<Source> {
   private final OperationPipeline pipeline;

   public TransformationSourceFilterProvider(OperationPipeline var1) {
      this.pipeline = Preconditions.checkNotNull(var1);
   }

   @Override
   public EntityFilter getFilter(PipelineActivity<Source> var1) {
      PipelineActivity var2 = this.pipeline.getNextActivity(var1);
      if (var2 != null) {
         EntityFilter var3 = this.checkFilter(var2);
         if (var3 != null) {
            return var3;
         }
      }

      return EntityFilters.allOfType(EntityType.Source);
   }

   private EntityFilter checkFilter(PipelineActivity var1) {
      EntityFilter var2 = this.getFilterImpl(var1);
      if (var2 != null) {
         return var2;
      }

      PipelineActivity var3 = this.pipeline.getNextActivity(var1);
      if (var3 != null) {
         var2 = this.checkFilter(var3);
      }

      return var2;
   }

   private EntityFilter getFilterImpl(PipelineActivity var1) {
      if (var1 instanceof TransformationActivity) {
         Transformation var2 = ((TransformationActivity)var1).getContent();
         return new TransformationSourceFilterProvider.TransformationBasedFilter(var2);
      } else {
         return null;
      }
   }

   private static final class TransformationBasedFilter implements EntityFilter {
      private final Transformation tf;

      public TransformationBasedFilter(Transformation var1) {
         this.tf = var1;
      }

      @Override
      public boolean apply(IntegrationEntity var1) {
         return var1 instanceof Source ? this.accept((Source)var1) : false;
      }

      private boolean accept(Source var1) {
         return this.tf == null || this.tf.canGetInputFrom(var1);
      }
   }
}
