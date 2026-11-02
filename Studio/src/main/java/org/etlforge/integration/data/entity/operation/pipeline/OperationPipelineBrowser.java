package org.etlforge.integration.data.entity.operation.pipeline;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Target;

public class OperationPipelineBrowser {
   private final OperationPipeline pipeline;

   public OperationPipelineBrowser(OperationPipeline var1) {
      this.pipeline = var1;
   }

   public final int getActivityCount() {
      return this.pipeline.getActivityCount();
   }

   protected final <A extends PipelineActivity> A getFirstActivityOfType(Class<A> var1) {
      return this.pipeline.getFirstActivityOfType(var1);
   }

   protected final <A extends PipelineActivity> A getLastActivityOfType(Class<A> var1) {
      return this.pipeline.getLastActivityOfType(var1);
   }

   public final SourceActivity getSourceActivity() {
      return this.getFirstActivityOfType(SourceActivity.class);
   }

   public final Source getSource() {
      return this.getFirstContent(SourceActivity.class, Source.class);
   }

   public final void setSource(Source var1) {
      SourceActivity var2 = this.getSourceActivity();
      if (var2 != null) {
         var2.setContent(var1);
      }
   }

   public final TargetActivity getTargetActivity() {
      return this.getFirstActivityOfType(TargetActivity.class);
   }

   public final Target getTarget() {
      return this.getFirstContent(TargetActivity.class, Target.class);
   }

   public final void setTarget(Target var1) {
      TargetActivity var2 = this.getFirstActivityOfType(TargetActivity.class);
      if (var2 != null) {
         var2.setContent(var1);
      }
   }

   public final OperationPipeline getPipeline() {
      return this.pipeline;
   }

   public final <A extends PipelineActivity, T> T getFirstContent(Class<A> var1, Class<T> var2) {
      PipelineActivity var3 = this.getFirstActivityOfType(var1);
      return var3 != null ? this.getActivityContent(var3, var2) : null;
   }

   protected final <T> T getActivityContent(PipelineActivity var1, Class<T> var2) {
      Object var3 = var1.getContent();
      return (T)(var2.isInstance(var3) ? var2.cast(var3) : null);
   }

   public final <A extends PipelineActivity, T> T getLastContent(Class<A> var1, Class<T> var2) {
      PipelineActivity var3 = this.getLastActivityOfType(var1);
      return var3 != null ? this.getActivityContent(var3, var2) : null;
   }

   protected final <A extends PipelineActivity> A getNextActivity(PipelineActivity var1, Class<A> var2) {
      for (PipelineActivity var3 = this.pipeline.getNextActivity(var1); var3 != null; var3 = this.pipeline.getNextActivity(var3)) {
         if (var2.isInstance(var3)) {
            return (A)var2.cast(var3);
         }
      }

      return null;
   }

   protected final <A extends PipelineActivity, T> T getNextContent(PipelineActivity var1, Class<A> var2, Class<T> var3) {
      PipelineActivity var4 = this.getNextActivity(var1, var2);
      return var4 != null ? this.getActivityContent(var4, var3) : null;
   }

   public final Iterable<IntegrationEntity> collectAllProjectItems() {
      return FluentIterable.from(this.pipeline.getActivities()).transform(new Function<PipelineActivity, Object>() {
         public Object apply(PipelineActivity var1) {
            return var1.getContent();
         }
      }).filter(IntegrationEntity.class);
   }
}
