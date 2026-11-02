package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node;

import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.dnd.DropBehavior;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.dnd.SourceActivityDropBehavior;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.operation.pipeline.SourceActivity;
import org.etlforge.ui.graph2d.Graph2D;
import org.etlforge.ui.graph2d.node.GraphNodeUi2D;

public final class SourceActivityNode extends EntityActivityNode<Source> {
   public SourceActivityNode(SourceActivity var1, OperationWrapperNode var2) {
      super(var1, var2);
   }

   public SourceActivity getDataObject() {
      return (SourceActivity)super.getDataObject();
   }

   @Override
   public GraphNodeUi2D createUi(Graph2D var1) {
      return new SourceNodeUi2D(var1, this);
   }

   @Override
   public DropBehavior getDropBehavior(OperationGraphController var1) {
      return new SourceActivityDropBehavior(var1, this);
   }
}
