package org.etlforge.integration.client.ui.connector.netsuite.operation;

import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.EntityActivityNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.data.connector.NetSuiteFunction;
import org.etlforge.integration.data.connector.operation.NetSuiteFunctionActivity;
import org.etlforge.ui.graph2d.Graph2D;
import org.etlforge.ui.graph2d.node.GraphNodeUi2D;

final class NetSuiteFunctionActivityNode extends EntityActivityNode<NetSuiteFunction> {
   public NetSuiteFunctionActivityNode(NetSuiteFunctionActivity var1, OperationWrapperNode var2) {
      super(var1, var2);
   }

   public NetSuiteFunctionActivity getDataObject() {
      return (NetSuiteFunctionActivity)super.getDataObject();
   }

   @Override
   public GraphNodeUi2D createUi(Graph2D var1) {
      return new NetSuiteFunctionNodeUi2D(var1, this);
   }
}
