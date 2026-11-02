package org.etlforge.integration.data.entity.operation.pipeline.hostedws;

import com.google.common.collect.ImmutableSet;
import org.etlforge.integration.data.entity.operation.pipeline.ActivityConnection;
import org.etlforge.integration.data.entity.operation.pipeline.ActivityInsertionPolicy;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityCounter;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityType;

public final class HostedWebServiceInsertionPolicy implements ActivityInsertionPolicy {
   private final OperationPipeline pipeline;

   public HostedWebServiceInsertionPolicy(OperationPipeline var1) {
      this.pipeline = var1;
   }

   @Override
   public ImmutableSet<PipelineActivityType> getAllowedActivities(ActivityConnection var1) {
      int var2 = PipelineActivityCounter.countTransformations(this.pipeline);
      return var2 == 1 && !this.isPreviousWsCall(var1) ? ImmutableSet.of(PipelineActivityType.TRANSFORMATION) : ImmutableSet.of();
   }

   private boolean isPreviousWsCall(ActivityConnection var1) {
      PipelineActivity var2 = var1.getFrom();
      return var2 != null && var2.getType() == PipelineActivityType.WEBSERVICECALL;
   }
}
