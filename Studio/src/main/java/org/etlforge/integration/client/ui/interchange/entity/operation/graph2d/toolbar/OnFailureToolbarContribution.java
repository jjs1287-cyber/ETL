package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.toolbar;

import javax.swing.JMenu;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OnFailureMenuBuilder;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;

public class OnFailureToolbarContribution extends SingleOperationNodeToolbarContribution {
   private final OperationGraphController controller;
   private final JMenu menu;
   private OperationWrapperNode currentNode;

   public OnFailureToolbarContribution(OperationGraphController var1) {
      this.controller = var1;
      this.menu = new JMenu("On Failure");
      this.menu.setEnabled(false);
   }

   @Override
   public void contributeTo(OperationGraphToolbar var1) {
      var1.addMenu(this.menu);
   }

   @Override
   protected void handleNoSelection() {
      this.menu.setEnabled(false);
   }

   @Override
   protected void handleOperationNode(OperationWrapperNode var1) {
      if (var1 != this.currentNode) {
         this.currentNode = var1;
         OnFailureMenuBuilder var2 = new OnFailureMenuBuilder(this.controller, var1);
         this.menu.removeAll();
         var2.addToMenu(this.menu);
      }

      this.menu.setEnabled(true);
   }
}
