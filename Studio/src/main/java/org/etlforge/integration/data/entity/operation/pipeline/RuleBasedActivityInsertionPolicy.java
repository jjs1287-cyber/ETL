package org.etlforge.integration.data.entity.operation.pipeline;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableSet.Builder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class RuleBasedActivityInsertionPolicy implements ActivityInsertionPolicy {
   private final OperationPipeline pipeline;

   protected RuleBasedActivityInsertionPolicy(OperationPipeline var1) {
      this.pipeline = var1;
   }

   protected abstract Iterable<PipelineActivityType> getCandidates();

   protected abstract RuleBasedActivityInsertionPolicy.ActivityChecker getRule();

   @Override
   public ImmutableSet<PipelineActivityType> getAllowedActivities(ActivityConnection var1) {
      List var2 = this.getLeftChain(var1);
      List var3 = this.getRightChain(var1);
      Builder var4 = ImmutableSet.builder();

      for (PipelineActivityType var6 : this.getCandidates()) {
         if (this.isTypeAllowed(var6, var2, var3)) {
            var4.add(var6);
         }
      }

      return var4.build();
   }

   private boolean isTypeAllowed(PipelineActivityType var1, List<PipelineActivityType> var2, List<PipelineActivityType> var3) {
      ArrayList var4 = Lists.newArrayList(var2);
      var4.add(var1);
      var4.addAll(var3);
      return this.isChainValid(var4);
   }

   private List<PipelineActivityType> getLeftChain(ActivityConnection var1) {
      LinkedList var2 = new LinkedList();
      PipelineActivity var3 = var1.getFrom();

      while (var3 != null) {
         var2.addFirst(var3.getType());
         var3 = this.pipeline.getPreviousActivity(var3);
      }

      return var2;
   }

   private List<PipelineActivityType> getRightChain(ActivityConnection var1) {
      ArrayList var2 = Lists.newArrayList();
      PipelineActivity var3 = var1.getTo();

      while (var3 != null) {
         var2.add(var3.getType());
         var3 = this.pipeline.getNextActivity(var3);
      }

      return var2;
   }

   private boolean isChainValid(List<PipelineActivityType> var1) {
      RuleBasedActivityInsertionPolicy.ActivityChecker var2 = this.getRule();

      for (PipelineActivityType var4 : var1) {
         if (!var2.accepts(var4)) {
            return false;
         }

         var2 = var2.next(var4);
      }

      return true;
   }

   protected interface ActivityChecker {
      boolean accepts(PipelineActivityType var1);

      RuleBasedActivityInsertionPolicy.ActivityChecker next(PipelineActivityType var1);
   }
}
