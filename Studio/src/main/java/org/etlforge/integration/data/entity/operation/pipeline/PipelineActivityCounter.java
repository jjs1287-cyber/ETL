package org.etlforge.integration.data.entity.operation.pipeline;

public final class PipelineActivityCounter {
   public static int countTransformations(OperationPipeline var0) {
      return count(var0, PipelineActivityType.TRANSFORMATION);
   }

   public static int countScripts(OperationPipeline var0) {
      PipelineActivityType var1 = PipelineActivityType.SCRIPT;
      return count(var0, var1);
   }

   private static int count(OperationPipeline var0, PipelineActivityType var1) {
      int var2 = 0;

      for (PipelineActivity var4 : var0.getActivities()) {
         if (var4.getType() == var1) {
            var2++;
         }
      }

      return var2;
   }
}
