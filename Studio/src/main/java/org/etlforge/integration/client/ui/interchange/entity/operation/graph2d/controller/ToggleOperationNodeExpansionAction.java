package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.ui.graph2d.ToggleNodeExpansionAction;

class ToggleOperationNodeExpansionAction extends ToggleNodeExpansionAction {
   private final OperationGraphController controller;

   public ToggleOperationNodeExpansionAction(OperationGraphController var1) {
      super(var1.getGraph());
      this.controller = var1;
   }

   public ToggleOperationNodeExpansionAction(OperationGraphController var1, OperationWrapperNode var2) {
      super(var1.getGraph(), var2);
      this.controller = var1;
   }

   @Override
   protected void toggle(Object var1) {
      if (var1 instanceof OperationWrapperNode) {
         this.controller.toggleNodeExpansion((OperationWrapperNode)var1);
      } else {
         super.toggle(var1);
      }
   }
}
