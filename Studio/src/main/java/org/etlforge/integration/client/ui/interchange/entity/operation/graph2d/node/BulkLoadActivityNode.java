package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node;

import org.etlforge.integration.data.entity.SalesforceBulkLoad;
import org.etlforge.integration.data.entity.operation.pipeline.BulkLoadActivity;
import org.etlforge.ui.graph2d.Graph2D;
import org.etlforge.ui.graph2d.node.GraphNodeUi2D;

public final class BulkLoadActivityNode extends EntityActivityNode<SalesforceBulkLoad> {
   public BulkLoadActivityNode(BulkLoadActivity var1, OperationWrapperNode var2) {
      super(var1, var2);
   }

   @Override
   public boolean isRenamable() {
      return false;
   }

   @Override
   public GraphNodeUi2D createUi(Graph2D var1) {
      return new BulkLoadNodeUi2D(var1, this);
   }
}
