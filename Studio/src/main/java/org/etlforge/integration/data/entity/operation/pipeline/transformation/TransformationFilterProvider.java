package org.etlforge.integration.data.entity.operation.pipeline.transformation;

import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.operation.TransformationOperationType;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.SourceActivity;
import org.etlforge.integration.data.entity.operation.pipeline.TargetActivity;
import org.etlforge.integration.data.entity.operation.pipeline.filter.EntityFilterProvider;
import org.etlforge.integration.data.functors.EntityFilter;

public final class TransformationFilterProvider implements EntityFilterProvider<Transformation> {
   private final OperationPipeline pipeline;

   public TransformationFilterProvider(OperationPipeline var1) {
      assert var1.getType() == TransformationOperationType.TYPE : "Wrong operation type: " + var1.getType();
      this.pipeline = var1;
   }

   @Override
   public EntityFilter getFilter(PipelineActivity<Transformation> var1) {
      return new TransformationFilterProvider.FilterImpl();
   }

   private class FilterImpl implements EntityFilter {
      private FilterImpl() {
      }

      @Override
      public boolean apply(IntegrationEntity var1) {
         return var1 instanceof Transformation ? this.evaluate((Transformation)var1) : false;
      }

      private boolean evaluate(Transformation var1) {
         Source var2 = this.getSource();
         if (var2 != null && !var1.canGetInputFrom(var2)) {
            return false;
         }

         Target var3 = this.getTarget();
         return var3 == null || var1.canSendOutputTo(var3);
      }

      private Source getSource() {
         SourceActivity var1 = TransformationFilterProvider.this.pipeline.getFirstActivityOfType(SourceActivity.class);
         return var1 != null && !var1.isEmpty() ? var1.getContent() : null;
      }

      private Target getTarget() {
         TargetActivity var1 = TransformationFilterProvider.this.pipeline.getFirstActivityOfType(TargetActivity.class);
         return var1 != null && !var1.isEmpty() ? var1.getContent() : null;
      }
   }
}
