package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node;

import java.awt.Point;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

abstract class OperationNodeToolBarDropDown extends OperationNodeToolBarButton {
   public OperationNodeToolBarDropDown(AbstractOperationNodeUi var1) {
      super(var1);
   }

   protected abstract JPopupMenu getPopupMenu(OperationNodeController var1);

   @Override
   protected final void doClick() {
      OperationNodeController var1 = this.getNode().getNodeController();
      if (var1 != null) {
         JPopupMenu var2 = this.getPopupMenu(var1);
         if (var2 != null) {
            Point var3 = SwingUtilities.convertPoint(this.getButton(), new Point(8, 16), this.getNodeUi());
            this.getNodeUi().getGraph().showContextMenu(var2, this.getNode(), var3);
         }
      }
   }
}
