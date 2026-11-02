package org.etlforge.integration.client.ui.connector.ebs.operation;

import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.EntityActivityNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.data.connector.EBSFunction;
import org.etlforge.integration.data.connector.operation.EBSFunctionActivity;
import org.etlforge.ui.graph2d.Graph2D;
import org.etlforge.ui.graph2d.node.GraphNodeUi2D;

final class EBSFunctionActivityNode extends EntityActivityNode<EBSFunction> {
   public EBSFunctionActivityNode(EBSFunctionActivity var1, OperationWrapperNode var2) {
      super(var1, var2);
   }

   public EBSFunctionActivity getDataObject() {
      return (EBSFunctionActivity)super.getDataObject();
   }

   @Override
   public GraphNodeUi2D createUi(Graph2D var1) {
      return new EBSFunctionNodeUi2D(var1, this);
   }
}
