package org.etlforge.ui.widget.tree;

import com.google.common.base.Preconditions;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import javax.swing.tree.TreePath;
import org.etlforge.system.ArgCheck;
import org.etlforge.ui.widget.ComponentPainter;

public final class NodePairLineConnector implements ComponentPainter {
   public static final int DEFAULT_MARGIN = 5;
   public static final int DEFAULT_EXTENSION = 15;
   public static final int DEFAULT_CURVE_WIDTH = 3;
   private final KongaTree tree;
   private Color color = Color.BLACK;
   private int margin = 5;
   private int extension = 15;
   private int curveWidth = 3;
   private int extraPreferredWidth;
   private List<NodePairLineConnector.NodePair> nodePairs;

   public NodePairLineConnector(KongaTree var1) {
      this.tree = Preconditions.checkNotNull(var1);
      this.updateExtraPreferredWidth();
   }

   public void setMargin(int var1) {
      this.margin = ArgCheck.checkNotNegative(var1);
   }

   public void setExtension(int var1) {
      this.extension = ArgCheck.checkNotNegative(var1);
   }

   public void setCurveWidth(int var1) {
      this.curveWidth = ArgCheck.checkNotNegative(var1);
   }

   public void setColor(Color var1) {
      this.color = Preconditions.checkNotNull(var1);
   }

   public void removeAllPairs() {
      this.nodePairs = null;
   }

   public void addNodePair(KongaTreeNode var1, KongaTreeNode var2) {
      Preconditions.checkNotNull(var1, "n1");
      Preconditions.checkNotNull(var2, "n2");
      Preconditions.checkArgument(var1 != var2, "n1 and n2 must be different nodes.");
      int var3 = this.getRow(var1);
      if (var3 >= 0) {
         int var4 = this.getRow(var2);
         if (var4 >= 0) {
            NodePairLineConnector.NodePair var5;
            if (var3 < var4) {
               var5 = new NodePairLineConnector.NodePair(var1, var2);
            } else {
               var5 = new NodePairLineConnector.NodePair(var2, var1);
            }

            if (this.nodePairs == null) {
               this.nodePairs = new ArrayList<>();
            }

            this.nodePairs.add(var5);
         }
      }
   }

   private int getRow(KongaTreeNode var1) {
      TreePath var2 = this.tree.getPath(var1);
      return var2 != null ? this.tree.getRowForPath(var2) : -1;
   }

   @Override
   public Dimension getPreferredSize(Dimension var1) {
      var1.width = var1.width + this.extraPreferredWidth;
      return var1;
   }

   @Override
   public boolean preRendering(Component var1, Graphics2D var2) {
      return false;
   }

   @Override
   public void postRendering(Component var1, Graphics2D var2) {
      if (var1 != this.tree) {
         throw new IllegalArgumentException("postRendering was called with the wrong component");
      }

      if (this.nodePairs != null) {
         Color var3 = var2.getColor();
         var2.setColor(this.color);

         for (NodePairLineConnector.NodePair var5 : this.nodePairs) {
            this.paintPair(var5, var2);
         }

         var2.setColor(var3);
      }
   }

   private void paintPair(NodePairLineConnector.NodePair var1, Graphics var2) {
      TreePath var3 = this.tree.getPath(var1.first);
      TreePath var4 = this.tree.getPath(var1.second);
      Rectangle var5 = this.tree.getPathBounds(var3);
      if (var5 != null) {
         Rectangle var6 = this.tree.getPathBounds(var4);
         if (var6 != null) {
            int var7 = var5.x + var5.width + this.margin;
            int var8 = var5.y + var5.height / 2;
            int var9 = var6.x + var6.width + this.margin;
            int var10 = var6.y + var6.height / 2;
            int var11 = Math.max(var7, var9) + this.extension;
            int var12 = this.tree.getRowForPath(var3);
            int var13 = this.tree.getRowForPath(var4);

            for (int var14 = var12 + 1; var14 < var13; var14++) {
               Rectangle var15 = this.tree.getRowBounds(var14);
               int var16 = var15.x + var15.width + 2 * this.margin;
               if (var16 > var11) {
                  var11 = var16;
               }
            }

            var2.drawLine(var7, var8, var11, var8);
            var2.drawLine(var11, var8, var11 + this.curveWidth, var8 + this.curveWidth);
            var2.drawLine(var11 + this.curveWidth, var8 + this.curveWidth, var11 + this.curveWidth, var10 - this.curveWidth);
            var2.drawLine(var11 + this.curveWidth, var10 - this.curveWidth, var11, var10);
            var2.drawLine(var9, var10, var11, var10);
         }
      }
   }

   private void updateExtraPreferredWidth() {
      this.extraPreferredWidth = this.margin + this.extension + this.curveWidth + 20;
   }

   private static class NodePair {
      public final KongaTreeNode first;
      public final KongaTreeNode second;

      public NodePair(KongaTreeNode var1, KongaTreeNode var2) {
         this.first = var1;
         this.second = var2;
      }
   }
}
