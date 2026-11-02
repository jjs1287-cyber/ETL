package org.etlforge.integration.client.ui.connector.mscrm.operation;

import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.EntityActivityNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.data.connector.MSCrmFunction;
import org.etlforge.integration.data.connector.operation.MSCrmFunctionActivity;
import org.etlforge.ui.graph2d.Graph2D;
import org.etlforge.ui.graph2d.node.GraphNodeUi2D;

final class MSCrmFunctionActivityNode extends EntityActivityNode<MSCrmFunction> {
   public MSCrmFunctionActivityNode(MSCrmFunctionActivity var1, OperationWrapperNode var2) {
      super(var1, var2);
   }

   public MSCrmFunctionActivity getDataObject() {
      return (MSCrmFunctionActivity)super.getDataObject();
   }

   @Override
   public GraphNodeUi2D createUi(Graph2D var1) {
      return new MSCrmFunctionNodeUi2D(var1, this);
   }
}
