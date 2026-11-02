package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import javax.swing.JPopupMenu;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.ui.widget.popup.KongaPopupMenu;

class OperationContextMenuFactory {
   private final OperationGraphController controller;
   private final OperationNodeMenuProviderImpl actionMenuProvider;

   public OperationContextMenuFactory(OperationGraphController var1) {
      this.controller = var1;
      this.actionMenuProvider = new OperationNodeMenuProviderImpl(var1);
   }

   public JPopupMenu createMenu(OperationWrapperNode var1) {
      KongaPopupMenu var2 = new KongaPopupMenu();
      this.actionMenuProvider.provideActionItems(var1, var2);
      var2.add(new ToggleOperationNodeExpansionAction(this.controller, var1));
      return var2;
   }
}
