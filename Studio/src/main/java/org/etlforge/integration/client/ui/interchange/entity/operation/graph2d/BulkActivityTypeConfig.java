package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d;

import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.BulkLoadActivityNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.operation.flow.OperationWrapper;
import org.etlforge.integration.data.entity.operation.pipeline.BulkLoadActivity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityType;

public final class BulkActivityTypeConfig extends EntityTypeBasedActivityTypeConfig {
   public BulkActivityTypeConfig() {
      super(PipelineActivityType.BULK_LOAD, EntityType.SalesforceBulkLoad);
   }

   @Override
   public PipelineActivityNode createNode(PipelineActivity var1, OperationWrapperNode var2) {
      return new BulkLoadActivityNode((BulkLoadActivity)var1, var2);
   }

   @Override
   public EntityContentProvider<?> getContentProvider(OperationGraphController var1, OperationWrapper var2, PipelineActivity var3) {
      return null;
   }
}
