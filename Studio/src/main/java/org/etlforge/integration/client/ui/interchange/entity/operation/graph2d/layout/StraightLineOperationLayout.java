package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.layout;

import com.google.common.collect.Maps;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.IdentityHashMap;
import java.util.Map;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.OperationGraphModel;
import org.etlforge.ui.graph2d.Graph2D;
import org.etlforge.ui.graph2d.layout.LayoutAlgorithm;

public final class StraightLineOperationLayout implements LayoutAlgorithm {
   private final Graph2D graph;
   private final OperationGraphModel model;
   private final int startX;
   private final int startY;
   private final int horizontalGap;

   public StraightLineOperationLayout(Graph2D var1, OperationGraphModel var2) {
      this(var1, var2, 50, 100, 50);
   }

   public StraightLineOperationLayout(Graph2D var1, OperationGraphModel var2, int var3, int var4, int var5) {
      this.graph = var1;
      this.model = var2;
      this.startX = var3;
      this.startY = var4;
      this.horizontalGap = var5;
   }

   @Override
   public Map<Object, Rectangle> doLayout() {
      IdentityHashMap var1 = Maps.newIdentityHashMap();
      int var2 = this.startX;

      for (Object var4 : this.model.getNodes()) {
         Dimension var5 = this.graph.getNodeUi(var4).getPreferredSize();
         int var6 = this.startY - var5.height / 2;
         Rectangle var7 = new Rectangle(new Point(var2, var6), var5);
         var1.put(var4, var7);
         var2 += var7.width + this.horizontalGap;
      }

      return var1;
   }
}
