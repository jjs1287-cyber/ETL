package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.ui.graph2d.GraphNodeEditor;

class OperationNodeRenamer extends AbstractNodeRenamer<OperationWrapperNode> {
   public OperationNodeRenamer(OperationGraphController var1, OperationWrapperNode var2) {
      super(var1, var2);
   }

   @Override
   protected void processNewValue(GraphNodeEditor var1) {
      String var2 = (String)var1.getCellEditorValue();
      this.getController().setOperationName(this.getNode(), var2);
   }
}
