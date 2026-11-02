package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node;

import com.google.common.annotations.VisibleForTesting;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.ActivityTypeConfig;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.OperationGraphConfig;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityType;

public final class NodeFactory {
   private final OperationGraphConfig graphConfig;

   @VisibleForTesting
   public static NodeFactory standardTypesOnly() {
      return new NodeFactory(OperationGraphConfig.standardConfig());
   }

   public NodeFactory(OperationGraphConfig var1) {
      this.graphConfig = var1;
   }

   public PipelineActivityNode createNode(PipelineActivity var1, OperationWrapperNode var2) {
      PipelineActivityType var3 = var1.getType();
      ActivityTypeConfig var4 = this.graphConfig.getTypeConfig(var3);
      return var4.createNode(var1, var2);
   }
}
