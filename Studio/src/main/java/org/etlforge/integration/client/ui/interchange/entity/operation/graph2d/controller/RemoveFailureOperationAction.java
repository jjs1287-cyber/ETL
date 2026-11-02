package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.data.entity.operation.flow.RouteType;

class RemoveFailureOperationAction extends AbstractAction {
   private final OperationGraphController controller;
   private final OperationWrapperNode node;

   public RemoveFailureOperationAction(OperationGraphController var1, OperationWrapperNode var2) {
      super(Strings.get("Remove.Failure.Operation.Name"));
      this.controller = var1;
      this.node = var2;
      this.setEnabled(var2.getDataObject().getFailureOperationId() != null);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      this.controller.setNextOperation(this.node, null, RouteType.FAILURE);
   }
}
