package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import com.google.common.collect.Maps;
import java.util.Map;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.data.entity.operation.OperationType;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.system.Disposable;

final class OperationTypeCache implements Disposable {
   private final Map<OperationWrapperNode, OperationTypeCache.PipelineCache> nodes = Maps.newHashMap();

   public OperationTypeCache() {
   }

   public OperationPipeline getPipeline(OperationWrapperNode var1, OperationType var2) {
      OperationTypeCache.PipelineCache var3 = this.nodes.get(var1);
      if (var3 == null) {
         var3 = new OperationTypeCache.PipelineCache(var1);
         this.nodes.put(var1, var3);
      }

      return var3.getPipelineFor(var2);
   }

   @Override
   public void dispose() {
      for (OperationTypeCache.PipelineCache var2 : this.nodes.values()) {
         var2.dispose();
      }
   }

   private static class PipelineCache implements Disposable {
      private final OperationWrapperNode opNode;
      private final Map<OperationType, OperationPipeline> pipelines;

      public PipelineCache(OperationWrapperNode var1) {
         this.opNode = var1;
         this.pipelines = Maps.newHashMap();
      }

      public OperationPipeline getPipelineFor(OperationType var1) {
         OperationPipeline var2 = this.opNode.getPipeline();
         if (var1 == var2.getType()) {
            return var2;
         }

         this.pipelines.put(var2.getType(), var2);
         OperationPipeline var3 = this.pipelines.get(var1);
         if (var3 == null) {
            var3 = var1.createNewTemplate();
            this.pipelines.put(var1, var3);
         }

         return var3;
      }

      @Override
      public void dispose() {
         for (OperationPipeline var2 : this.pipelines.values()) {
            for (PipelineActivity var4 : var2.getActivities()) {
               var4.dispose();
            }
         }
      }
   }
}
