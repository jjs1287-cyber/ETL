package org.etlforge.integration.data.entity.operation.pipeline;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.id.TransformationId;
import org.etlforge.integration.data.function.Function;

public final class DefaultOperationFunctionCollector implements OperationFunctionCollector {
   @Override
   public OperationFunctions collect(OperationPipeline var1) {
      ArrayList var2 = Lists.newArrayList();

      for (PipelineActivity var4 : var1.getActivities()) {
         Object var5 = var4.getContent();
         if (var5 instanceof Function) {
            var2.add((Function)var5);
         }
      }

      TransformationId var6 = this.getIdOfFirstTransformation(var2);
      return new OperationFunctions(var2, var6);
   }

   private TransformationId getIdOfFirstTransformation(List<Function> var1) {
      if (!var1.isEmpty()) {
         Function var2 = (Function)var1.get(0);
         if (var2 instanceof Transformation) {
            return ((Transformation)var2).getID();
         }
      }

      return null;
   }
}
