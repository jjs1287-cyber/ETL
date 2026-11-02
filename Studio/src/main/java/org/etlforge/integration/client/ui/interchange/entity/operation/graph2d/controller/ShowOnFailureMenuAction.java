package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;

class ShowOnFailureMenuAction extends NodeContextMenuShortCutAction<OperationWrapperNode> {
   public ShowOnFailureMenuAction(OperationGraphController var1) {
      super(var1, OperationWrapperNode.class);
      this.putValue("AcceleratorKey", KeyStroke.getKeyStroke(70, 0));
   }

   protected JPopupMenu getPopupMenu(OperationWrapperNode var1) {
      JPopupMenu var2 = new JPopupMenu();
      OnFailureMenuBuilder var3 = new OnFailureMenuBuilder(this.controller, var1);
      var2.add(var3.buildMenu());
      return var2;
   }
}
