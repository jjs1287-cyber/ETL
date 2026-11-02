package org.etlforge.ui.widget.tree;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;
import javax.swing.tree.TreePath;
import org.etlforge.ui.util.AbstractMouseDragSelector;

final class MouseDragSelector extends AbstractMouseDragSelector {
   private static final int MINIMUM_DISTANCE_FOR_DRAGGING = 5;
   private final KongaTree tree;
   private int startRow;
   private List<KongaTreeNode> selectedWhenStart;

   public MouseDragSelector(KongaTree var1) {
      super(var1);
      this.tree = var1;
   }

   @Override
   protected void onMousePressed() {
      Point var1 = this.getStartPoint();
      this.startRow = this.tree.getRowForLocation(var1.x, var1.y);
   }

   @Override
   protected void onInitialize() {
      this.selectedWhenStart = this.tree.getSelectedNodes();
   }

   @Override
   protected boolean allowDragging() {
      return this.startRow == -1 && this.getStartPoint().distance(this.getCurrentPoint()) > 5.0;
   }

   @Override
   protected void onDragging() {
      Rectangle var1 = this.getDraggingArea();
      int var2 = 0;

      for (int var3 = this.tree.getRowCount(); var2 < var3; var2++) {
         TreePath var4 = this.tree.getPathForRow(var2);
         if (var4 != null) {
            KongaTreeNode var5 = (KongaTreeNode)var4.getLastPathComponent();
            Rectangle var6 = this.tree.getRowBounds(var2);
            if (var6 != null && var6.intersects(var1)) {
               if (this.selectedWhenStart.contains(var5)) {
                  if (this.isControlDown()) {
                     this.tree.getSelectionModel().removeSelectionPath(var4);
                     continue;
                  }

                  this.selectedWhenStart.remove(var5);
               }

               this.tree.makePathSelected(this.tree.getPathForRow(var2), true);
            } else if (this.selectedWhenStart.contains(var5)) {
               if (this.isControlDown()) {
                  this.tree.makePathSelected(var4, true);
               } else if (!this.isShiftDown()) {
                  this.tree.getSelectionModel().removeSelectionPath(var4);
               }
            } else {
               this.tree.getSelectionModel().removeSelectionPath(var4);
            }
         }
      }
   }

   @Override
   protected void onStopDrag() {
      this.selectedWhenStart = null;
      this.tree.fireTreeSelectionChanged();
   }
}
