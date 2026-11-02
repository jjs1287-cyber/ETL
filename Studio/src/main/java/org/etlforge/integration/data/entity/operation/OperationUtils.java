package org.etlforge.integration.data.entity.operation;

import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.data.entity.operation.pipeline.OperationFunctionCollector;
import org.etlforge.integration.data.entity.operation.pipeline.OperationFunctions;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.SourceActivity;
import org.etlforge.integration.data.entity.operation.pipeline.TargetActivity;
import org.etlforge.integration.data.entity.operation.pipeline.hostedws.HostedWebServicePipelineBrowser;

public final class OperationUtils {
   public static void applyDeployProperties(Operation var0, OperationPipeline var1) {
      var0.setOperationType(var1.getType());
      var0.setProperty("source_id", getSourceId(var1));
      var0.setProperty("target_id", getTargetId(var1));
      applyFunctions(var0, var1);
      applyHostedWebServiceSettings(var0, var1);
   }

   private static SourceId getSourceId(OperationPipeline var0) {
      SourceActivity var1 = var0.getFirstActivityOfType(SourceActivity.class);
      return var1 != null && !var1.isEmpty() ? var1.getContent().getID() : null;
   }

   private static TargetId getTargetId(OperationPipeline var0) {
      TargetActivity var1 = var0.getFirstActivityOfType(TargetActivity.class);
      return var1 != null && !var1.isEmpty() ? var1.getContent().getID() : null;
   }

   private static void applyFunctions(Operation var0, OperationPipeline var1) {
      OperationFunctionCollector var2 = var1.getType().getFunctionCollector();
      OperationFunctions var3 = var2.collect(var1);
      var0.setFunctionChain(var3);
   }

   private static void applyHostedWebServiceSettings(Operation var0, OperationPipeline var1) {
      if (var1.getType() == HostedWebServiceOperationType.TYPE) {
         HostedWebServicePipelineBrowser var2 = new HostedWebServicePipelineBrowser(var1);
         var0.setHostedWebService(var2.getWebServiceCall());
      }
   }

   private OperationUtils() {
   }
}
