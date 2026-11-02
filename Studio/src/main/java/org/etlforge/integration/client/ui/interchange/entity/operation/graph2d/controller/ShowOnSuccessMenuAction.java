package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;

class ShowOnSuccessMenuAction extends NodeContextMenuShortCutAction<OperationWrapperNode> {
   public ShowOnSuccessMenuAction(OperationGraphController var1) {
      super(var1, OperationWrapperNode.class);
      this.putValue("AcceleratorKey", KeyStroke.getKeyStroke(83, 0));
   }

   protected JPopupMenu getPopupMenu(OperationWrapperNode var1) {
      JPopupMenu var2 = new JPopupMenu();
      OnSuccessMenuBuilder var3 = new OnSuccessMenuBuilder(this.controller, var1);
      var2.add(var3.buildMenu());
      return var2;
   }
}
