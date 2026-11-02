package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import java.awt.Point;
import java.awt.event.ActionEvent;
import org.etlforge.ui.graph.node.AbstractGraphNode;
import org.etlforge.ui.selection.AbstractSelectionAction;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.Selection;
import org.etlforge.ui.selection.SelectionUtils;
import org.etlforge.ui.util.KeyUtils;

class TriggerContextMenuAction extends AbstractSelectionAction {
   private final OperationGraphController controller;

   public TriggerContextMenuAction(OperationGraphController var1) {
      this.controller = var1;
      this.putValue("AcceleratorKey", KeyUtils.CONTEXT_MENU);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      ContextMenuRequestHandler var2 = new ContextMenuRequestHandler(this.controller);
      AbstractGraphNode var3 = this.getSelectedNode();
      if (var3 != null) {
         var2.handle(var3);
      } else {
         var2.handleCanvas(new Point(4, 4));
      }
   }

   @Override
   public boolean isEnabled() {
      return true;
   }

   private AbstractGraphNode getSelectedNode() {
      Selection var1 = this.getSelection();
      return var1 instanceof OrderedSelection ? SelectionUtils.getSingleSelection(var1, AbstractGraphNode.class) : null;
   }
}
