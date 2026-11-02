package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node;

import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.dnd.DropBehavior;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.dnd.RejectDrop;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.dnd.WebServiceTransformationDropBehavior;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.operation.WebServiceOperationType;
import org.etlforge.integration.data.entity.operation.pipeline.TransformationActivity;
import org.etlforge.ui.graph2d.Graph2D;
import org.etlforge.ui.graph2d.node.GraphNodeUi2D;

public final class TransformationActivityNode extends EntityActivityNode<Transformation> {
   public TransformationActivityNode(TransformationActivity var1, OperationWrapperNode var2) {
      super(var1, var2);
   }

   public TransformationActivity getDataObject() {
      return (TransformationActivity)super.getDataObject();
   }

   @Override
   public GraphNodeUi2D createUi(Graph2D var1) {
      return new TransformationNodeUi2D(var1, this);
   }

   @Override
   public DropBehavior getDropBehavior(OperationGraphController var1) {
      if (this.isReadOnly()) {
         return new RejectDrop();
      }

      OperationWrapperNode var2 = this.getParent();
      return var2.getPipeline().getType() == WebServiceOperationType.TYPE
         ? new WebServiceTransformationDropBehavior(var1, var2, this)
         : super.getDropBehavior(var1);
   }
}
