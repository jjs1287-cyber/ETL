package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import javax.swing.JPopupMenu;
import org.etlforge.ui.graph.node.AbstractGraphNode;
import org.etlforge.ui.graphics.RectangleUtils;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;

abstract class NodeContextMenuShortCutAction<T extends AbstractGraphNode> extends AbstractSingleObjectSelectionAction<T> {
   protected final OperationGraphController controller;

   protected NodeContextMenuShortCutAction(OperationGraphController var1, Class<T> var2) {
      super(var2);
      this.controller = var1;
   }

   protected void run(T var1, ActionEvent var2) {
      JPopupMenu var3 = this.getPopupMenu((T)var1);
      if (var3 != null) {
         Rectangle var4 = this.controller.getGraph().getLayout().getBounds(var1);
         this.controller.getGraph().showContextMenu(var3, RectangleUtils.getCenterPoint(var4));
      }
   }

   protected abstract JPopupMenu getPopupMenu(T var1);
}
