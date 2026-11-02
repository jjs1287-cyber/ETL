package org.etlforge.integration.client.ui.connector.msax.operation;

import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.EntityActivityNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.data.connector.MSAXFunction;
import org.etlforge.integration.data.connector.operation.MSAXFunctionActivity;
import org.etlforge.ui.graph2d.Graph2D;
import org.etlforge.ui.graph2d.node.GraphNodeUi2D;

final class MSAXFunctionActivityNode extends EntityActivityNode<MSAXFunction> {
   public MSAXFunctionActivityNode(MSAXFunctionActivity var1, OperationWrapperNode var2) {
      super(var1, var2);
   }

   public MSAXFunctionActivity getDataObject() {
      return (MSAXFunctionActivity)super.getDataObject();
   }

   @Override
   public GraphNodeUi2D createUi(Graph2D var1) {
      return new MSAXFunctionNodeUi2D(var1, this);
   }
}
