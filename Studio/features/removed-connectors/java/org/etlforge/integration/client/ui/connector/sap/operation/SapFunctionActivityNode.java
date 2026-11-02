package org.etlforge.integration.client.ui.connector.sap.operation;

import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.EntityActivityNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.data.connector.SapFunction;
import org.etlforge.integration.data.connector.operation.SapFunctionActivity;
import org.etlforge.ui.graph2d.Graph2D;
import org.etlforge.ui.graph2d.node.GraphNodeUi2D;

final class SapFunctionActivityNode extends EntityActivityNode<SapFunction> {
   public SapFunctionActivityNode(SapFunctionActivity var1, OperationWrapperNode var2) {
      super(var1, var2);
   }

   public SapFunctionActivity getDataObject() {
      return (SapFunctionActivity)super.getDataObject();
   }

   @Override
   public GraphNodeUi2D createUi(Graph2D var1) {
      return new SapFunctionNodeUi2D(var1, this);
   }
}
