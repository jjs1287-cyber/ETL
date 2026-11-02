package org.etlforge.integration.data.entity.operation.pipeline.transformation;

import com.google.common.collect.ImmutableSet;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityType;
import org.etlforge.integration.data.entity.operation.pipeline.RuleBasedActivityInsertionPolicy;

public final class TransformationInsertionPolicy extends RuleBasedActivityInsertionPolicy {
   private static boolean TRANSFORMATION_CHAINS_ENABLED = Boolean.getBoolean("AllowTransformationChains");

   public TransformationInsertionPolicy(OperationPipeline var1) {
      super(var1);
   }

   @Override
   protected Iterable<PipelineActivityType> getCandidates() {
      return ImmutableSet.of(PipelineActivityType.SCRIPT, PipelineActivityType.SOURCE, PipelineActivityType.TRANSFORMATION, PipelineActivityType.TARGET);
   }

   @Override
   protected RuleBasedActivityInsertionPolicy.ActivityChecker getRule() {
      return new TransformationInsertionPolicy.StartChecker();
   }

   private static class PostSourceChecker implements RuleBasedActivityInsertionPolicy.ActivityChecker {
      private PostSourceChecker() {
      }

      @Override
      public boolean accepts(PipelineActivityType var1) {
         return var1 == PipelineActivityType.SCRIPT || var1 == PipelineActivityType.TRANSFORMATION;
      }

      @Override
      public RuleBasedActivityInsertionPolicy.ActivityChecker next(PipelineActivityType var1) {
         assert this.accepts(var1);
         if (var1 == PipelineActivityType.SCRIPT) {
            return this;
         } else if (var1 == PipelineActivityType.TRANSFORMATION) {
            return new TransformationInsertionPolicy.TransformationChainChecker();
         } else {
            throw new RuntimeException("Unexpected type: " + var1);
         }
      }
   }

   private static class PostTargetChecker implements RuleBasedActivityInsertionPolicy.ActivityChecker {
      private PostTargetChecker() {
      }

      @Override
      public boolean accepts(PipelineActivityType var1) {
         return var1 == PipelineActivityType.SCRIPT;
      }

      @Override
      public RuleBasedActivityInsertionPolicy.ActivityChecker next(PipelineActivityType var1) {
         assert this.accepts(var1);
         return this;
      }
   }

   private static class PreTargetChecker implements RuleBasedActivityInsertionPolicy.ActivityChecker {
      private PreTargetChecker() {
      }

      @Override
      public boolean accepts(PipelineActivityType var1) {
         return var1 == PipelineActivityType.SCRIPT || var1 == PipelineActivityType.TARGET;
      }

      @Override
      public RuleBasedActivityInsertionPolicy.ActivityChecker next(PipelineActivityType var1) {
         assert this.accepts(var1);
         if (var1 == PipelineActivityType.SCRIPT) {
            return this;
         } else if (var1 == PipelineActivityType.TARGET) {
            return new TransformationInsertionPolicy.PostTargetChecker();
         } else {
            throw new RuntimeException("Unexpected type: " + var1);
         }
      }
   }

   private static class StartChecker implements RuleBasedActivityInsertionPolicy.ActivityChecker {
      private StartChecker() {
      }

      @Override
      public boolean accepts(PipelineActivityType var1) {
         return var1 == PipelineActivityType.SCRIPT || var1 == PipelineActivityType.SOURCE || var1 == PipelineActivityType.TRANSFORMATION;
      }

      @Override
      public RuleBasedActivityInsertionPolicy.ActivityChecker next(PipelineActivityType var1) {
         assert this.accepts(var1);
         if (var1 == PipelineActivityType.SCRIPT) {
            return this;
         } else if (var1 == PipelineActivityType.SOURCE) {
            return new TransformationInsertionPolicy.PostSourceChecker();
         } else if (var1 == PipelineActivityType.TRANSFORMATION) {
            return new TransformationInsertionPolicy.TransformationChainChecker();
         } else {
            throw new RuntimeException("Unexpected type: " + var1);
         }
      }
   }

   private static class TransformationChainChecker implements RuleBasedActivityInsertionPolicy.ActivityChecker {
      private TransformationChainChecker() {
      }

      @Override
      public boolean accepts(PipelineActivityType var1) {
         return var1 == PipelineActivityType.SCRIPT
            || var1 == PipelineActivityType.TARGET
            || TransformationInsertionPolicy.TRANSFORMATION_CHAINS_ENABLED && var1 == PipelineActivityType.TRANSFORMATION;
      }

      @Override
      public RuleBasedActivityInsertionPolicy.ActivityChecker next(PipelineActivityType var1) {
         assert this.accepts(var1);
         if (var1 == PipelineActivityType.TARGET) {
            return new TransformationInsertionPolicy.PostTargetChecker();
         } else if (var1 == PipelineActivityType.SCRIPT) {
            return new TransformationInsertionPolicy.PreTargetChecker();
         } else if (var1 == PipelineActivityType.TRANSFORMATION) {
            return this;
         } else {
            throw new RuntimeException("Unexpected type: " + var1);
         }
      }
   }
}
