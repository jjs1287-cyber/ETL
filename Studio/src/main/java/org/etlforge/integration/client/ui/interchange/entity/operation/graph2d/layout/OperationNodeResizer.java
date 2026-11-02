package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.layout;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Collection;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.ui.DimensionUtils;

class OperationNodeResizer {
   private final OperationGraphLayout layout;
   private final OperationWrapperNode opNode;
   private final Rectangle opNodeBounds;
   private final Collection<? extends Rectangle> activityNodeBounds;

   OperationNodeResizer(OperationGraphLayout var1, OperationWrapperNode var2, Rectangle var3, Collection<? extends Rectangle> var4) {
      this.layout = var1;
      this.opNode = var2;
      this.opNodeBounds = var3;
      this.activityNodeBounds = var4;
   }

   void resize(Dimension var1) {
      Dimension var2 = this.layout.getGraph().getNodeUi(this.opNode).getMinimumSize();
      Dimension var3 = DimensionUtils.max(var1, var2);
      this.forceActivityNodesWithinOpNode(var3);
      this.opNodeBounds.setSize(var3);
      this.layout.fireLayoutChanged(this.opNode);
   }

   private void forceActivityNodesWithinOpNode(Dimension var1) {
      boolean var2 = true;
      boolean var3 = true;

      for (Rectangle var5 : this.activityNodeBounds) {
         if (var2) {
            var2 = this.checkX(var1, var5);
         }

         if (var3) {
            var3 = this.checkY(var1, var5);
         }

         if (!var2 && !var3) {
            return;
         }
      }
   }

   private boolean checkY(Dimension var1, Rectangle var2) {
      int var3 = var2.y + var2.height - var1.height + 30;
      if (var3 > 0) {
         var2.y -= var3;
         if (var2.y < 0) {
            var1.height += var3;
            var2.y = 30;
            return false;
         }
      }

      return true;
   }

   private boolean checkX(Dimension var1, Rectangle var2) {
      int var3 = var2.x + var2.width - var1.width + 30;
      if (var3 > 0) {
         var2.x -= var3;
         if (var2.x < 0) {
            var1.width += var3;
            var2.x = 30;
            return false;
         }
      }

      return true;
   }
}
