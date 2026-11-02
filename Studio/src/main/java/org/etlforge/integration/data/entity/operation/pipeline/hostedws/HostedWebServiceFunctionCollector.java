package org.etlforge.integration.data.entity.operation.pipeline.hostedws;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.id.TransformationId;
import org.etlforge.integration.data.entity.operation.pipeline.OperationFunctionCollector;
import org.etlforge.integration.data.entity.operation.pipeline.OperationFunctions;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;

public final class HostedWebServiceFunctionCollector implements OperationFunctionCollector {
   @Override
   public OperationFunctions collect(OperationPipeline var1) {
      ArrayList var2 = Lists.newArrayList();
      TransformationId var3 = null;
      HostedWebServicePipelineBrowser var4 = new HostedWebServicePipelineBrowser(var1);
      Transformation var5 = var4.getFirstTransformation();
      if (var5 != null) {
         var2.add(var5);
         var3 = var5.getID();
         Transformation var6 = var4.getSecondTransformation();
         if (var6 != null) {
            var2.add(var6);
         }
      }

      return new OperationFunctions(var2, var3);
   }
}
