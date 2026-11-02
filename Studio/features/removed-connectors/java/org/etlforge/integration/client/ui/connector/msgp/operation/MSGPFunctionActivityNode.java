package org.etlforge.integration.client.ui.connector.msgp.operation;

import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.EntityActivityNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.data.connector.MSGPFunction;
import org.etlforge.integration.data.connector.operation.MSGPFunctionActivity;
import org.etlforge.ui.graph2d.Graph2D;
import org.etlforge.ui.graph2d.node.GraphNodeUi2D;

final class MSGPFunctionActivityNode extends EntityActivityNode<MSGPFunction> {
   public MSGPFunctionActivityNode(MSGPFunctionActivity var1, OperationWrapperNode var2) {
      super(var1, var2);
   }

   public MSGPFunctionActivity getDataObject() {
      return (MSGPFunctionActivity)super.getDataObject();
   }

   @Override
   public GraphNodeUi2D createUi(Graph2D var1) {
      return new MSGPFunctionNodeUi2D(var1, this);
   }
}
