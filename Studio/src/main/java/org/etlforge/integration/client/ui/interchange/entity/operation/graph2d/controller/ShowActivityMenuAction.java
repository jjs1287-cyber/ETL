package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;

class ShowActivityMenuAction extends NodeContextMenuShortCutAction<OperationWrapperNode> {
   public ShowActivityMenuAction(OperationGraphController var1) {
      super(var1, OperationWrapperNode.class);
      this.putValue("AcceleratorKey", KeyStroke.getKeyStroke(65, 0));
   }

   protected JPopupMenu getPopupMenu(OperationWrapperNode var1) {
      return var1.isOperationTypeChangeable() ? new OperationTypeMenuBuilder(this.controller, var1).buildPopupMenu() : null;
   }
}
