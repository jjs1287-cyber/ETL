package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationNodeController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationNodeMenuProvider;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;

class OperationNodeControllerImpl implements OperationNodeController {
   private final OperationGraphController controller;
   private final OperationNodeMenuProvider menuProvider;

   public OperationNodeControllerImpl(OperationGraphController var1) {
      this.controller = var1;
      this.menuProvider = new OperationNodeMenuProviderImpl(var1);
   }

   @Override
   public void collapseNode(OperationWrapperNode var1) {
      if (this.controller.getGraph().getLayout().isExpanded(var1)) {
         this.controller.toggleNodeExpansion(var1);
      }
   }

   @Override
   public void expandNode(OperationWrapperNode var1) {
      if (this.controller.getGraph().getLayout().isCollapsed(var1)) {
         this.controller.toggleNodeExpansion(var1);
      }
   }

   @Override
   public OperationNodeMenuProvider getMenuProvider() {
      return this.menuProvider;
   }
}
