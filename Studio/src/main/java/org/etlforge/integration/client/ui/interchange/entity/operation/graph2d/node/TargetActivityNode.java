package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node;

import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.operation.pipeline.TargetActivity;
import org.etlforge.ui.graph2d.Graph2D;
import org.etlforge.ui.graph2d.node.GraphNodeUi2D;

public final class TargetActivityNode extends EntityActivityNode<Target> {
   public TargetActivityNode(TargetActivity var1, OperationWrapperNode var2) {
      super(var1, var2);
   }

   public TargetActivity getDataObject() {
      return (TargetActivity)super.getDataObject();
   }

   @Override
   public GraphNodeUi2D createUi(Graph2D var1) {
      return new TargetNodeUi2D(var1, this);
   }
}
