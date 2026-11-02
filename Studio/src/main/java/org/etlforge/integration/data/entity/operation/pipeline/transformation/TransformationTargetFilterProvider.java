package org.etlforge.integration.data.entity.operation.pipeline.transformation;

import com.google.common.base.Preconditions;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.TransformationActivity;
import org.etlforge.integration.data.entity.operation.pipeline.filter.EntityFilterProvider;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.functors.EntityFilters;

public final class TransformationTargetFilterProvider implements EntityFilterProvider<Target> {
   private final OperationPipeline pipeline;

   public TransformationTargetFilterProvider(OperationPipeline var1) {
      this.pipeline = Preconditions.checkNotNull(var1);
   }

   @Override
   public EntityFilter getFilter(PipelineActivity<Target> var1) {
      for (PipelineActivity var2 = this.pipeline.getPreviousActivity(var1); var2 != null; var2 = this.pipeline.getPreviousActivity(var2)) {
         EntityFilter var3 = this.getFilterImpl(var2);
         if (var3 != null) {
            return var3;
         }
      }

      return EntityFilters.allOfType(EntityType.Target);
   }

   private EntityFilter getFilterImpl(PipelineActivity var1) {
      if (var1 instanceof TransformationActivity) {
         Transformation var2 = ((TransformationActivity)var1).getContent();
         return new TransformationTargetFilterProvider.TransformationBasedFilter(var2);
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
         return var1 instanceof Target ? this.accept((Target)var1) : false;
      }

      private boolean accept(Target var1) {
         return this.tf == null || this.tf.canSendOutputTo(var1);
      }
   }
}
