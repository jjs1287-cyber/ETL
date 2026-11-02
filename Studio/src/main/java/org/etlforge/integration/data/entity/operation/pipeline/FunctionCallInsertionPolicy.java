package org.etlforge.integration.data.entity.operation.pipeline;

import com.google.common.collect.ImmutableSet;

public final class FunctionCallInsertionPolicy extends RuleBasedActivityInsertionPolicy {
   private final PipelineActivityType functionType;

   public FunctionCallInsertionPolicy(OperationPipeline var1, PipelineActivityType var2) {
      super(var1);
      this.functionType = var2;
   }

   @Override
   public ImmutableSet<PipelineActivityType> getAllowedActivities(ActivityConnection var1) {
      if (this.isConnectedToTheFunction(var1)) {
         return var1.getFrom() != null && !(var1.getFrom() instanceof ScriptActivity) ? ImmutableSet.of() : ImmutableSet.of(PipelineActivityType.SCRIPT);
      } else {
         return super.getAllowedActivities(var1);
      }
   }

   @Override
   protected Iterable<PipelineActivityType> getCandidates() {
      return ImmutableSet.of(PipelineActivityType.SCRIPT, PipelineActivityType.SOURCE, PipelineActivityType.TARGET);
   }

   @Override
   protected RuleBasedActivityInsertionPolicy.ActivityChecker getRule() {
      return new FunctionCallInsertionPolicy.StartChecker(this.functionType);
   }

   private boolean isConnectedToTheFunction(ActivityConnection var1) {
      return this.isFunctionCall(var1.getFrom()) || this.isFunctionCall(var1.getTo());
   }

   private boolean isFunctionCall(PipelineActivity var1) {
      return var1 != null && var1.getType() == this.functionType;
   }

   private static class PostSourceChecker implements RuleBasedActivityInsertionPolicy.ActivityChecker {
      private final PipelineActivityType functionType;

      public PostSourceChecker(PipelineActivityType var1) {
         this.functionType = var1;
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
            return new FunctionCallInsertionPolicy.RequestChainChecker(this.functionType);
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
            return new FunctionCallInsertionPolicy.PostTargetChecker();
         } else {
            throw new RuntimeException("Unexpected type: " + var1);
         }
      }
   }

   private static class RequestChainChecker implements RuleBasedActivityInsertionPolicy.ActivityChecker {
      private final PipelineActivityType functionType;

      public RequestChainChecker(PipelineActivityType var1) {
         this.functionType = var1;
      }

      @Override
      public boolean accepts(PipelineActivityType var1) {
         return var1 == this.functionType;
      }

      @Override
      public RuleBasedActivityInsertionPolicy.ActivityChecker next(PipelineActivityType var1) {
         assert this.accepts(var1);
         return new FunctionCallInsertionPolicy.ResponseChainChecker();
      }
   }

   private static class ResponseChainChecker implements RuleBasedActivityInsertionPolicy.ActivityChecker {
      private ResponseChainChecker() {
      }

      @Override
      public boolean accepts(PipelineActivityType var1) {
         return var1 == PipelineActivityType.SCRIPT || var1 == PipelineActivityType.TRANSFORMATION || var1 == PipelineActivityType.TARGET;
      }

      @Override
      public RuleBasedActivityInsertionPolicy.ActivityChecker next(PipelineActivityType var1) {
         assert this.accepts(var1);
         if (var1 == PipelineActivityType.TRANSFORMATION) {
            return new FunctionCallInsertionPolicy.PreTargetChecker();
         } else if (var1 == PipelineActivityType.TARGET) {
            return new FunctionCallInsertionPolicy.PostTargetChecker();
         } else if (var1 == PipelineActivityType.SCRIPT) {
            return new FunctionCallInsertionPolicy.PreTargetChecker();
         } else {
            throw new RuntimeException("Unexpected type: " + var1);
         }
      }
   }

   private static class StartChecker implements RuleBasedActivityInsertionPolicy.ActivityChecker {
      private final PipelineActivityType functionType;

      public StartChecker(PipelineActivityType var1) {
         this.functionType = var1;
      }

      @Override
      public boolean accepts(PipelineActivityType var1) {
         return var1 == PipelineActivityType.SCRIPT
            || var1 == PipelineActivityType.SOURCE
            || var1 == PipelineActivityType.TRANSFORMATION
            || var1 == this.functionType;
      }

      @Override
      public RuleBasedActivityInsertionPolicy.ActivityChecker next(PipelineActivityType var1) {
         assert this.accepts(var1);
         if (var1 == PipelineActivityType.SCRIPT) {
            return this;
         } else if (var1 == PipelineActivityType.SOURCE) {
            return new FunctionCallInsertionPolicy.PostSourceChecker(this.functionType);
         } else if (var1 == PipelineActivityType.TRANSFORMATION) {
            return new FunctionCallInsertionPolicy.RequestChainChecker(this.functionType);
         } else if (var1 == this.functionType) {
            return new FunctionCallInsertionPolicy.ResponseChainChecker();
         } else {
            throw new RuntimeException("Unexpected type: " + var1);
         }
      }
   }
}
