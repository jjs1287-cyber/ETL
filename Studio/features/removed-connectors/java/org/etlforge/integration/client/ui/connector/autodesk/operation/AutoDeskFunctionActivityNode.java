package org.etlforge.integration.client.ui.connector.autodesk.operation;

import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.EntityActivityNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.data.connector.AutoDeskFunction;
import org.etlforge.integration.data.connector.operation.AutoDeskFunctionActivity;
import org.etlforge.ui.graph2d.Graph2D;
import org.etlforge.ui.graph2d.node.GraphNodeUi2D;

final class AutoDeskFunctionActivityNode extends EntityActivityNode<AutoDeskFunction> {
   public AutoDeskFunctionActivityNode(AutoDeskFunctionActivity var1, OperationWrapperNode var2) {
      super(var1, var2);
   }

   public AutoDeskFunctionActivity getDataObject() {
      return (AutoDeskFunctionActivity)super.getDataObject();
   }

   @Override
   public GraphNodeUi2D createUi(Graph2D var1) {
      return new AutoDeskFunctionNodeUi2D(var1, this);
   }
}
