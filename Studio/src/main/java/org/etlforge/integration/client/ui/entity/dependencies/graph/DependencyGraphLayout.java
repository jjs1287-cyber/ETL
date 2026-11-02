package org.etlforge.integration.client.ui.entity.dependencies.graph;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.etlforge.ui.graph2d.Graph2D;
import org.etlforge.ui.graph2d.layout.AbstractGraphLayout2D;
import org.etlforge.ui.graph2d.node.GraphNodeUi2D;
import org.etlforge.ui.graphics.RectanglePredicates;

final class DependencyGraphLayout extends AbstractGraphLayout2D {
   private final DependencyGraphModel dataModel;
   private final Map<Object, Rectangle> nodeBounds = Maps.newHashMap();

   public DependencyGraphLayout(Graph2D var1, DependencyGraphModel var2) {
      super(var1);
      this.dataModel = var2;
      this.buildLayout(var1);
      this.updateEdges();
   }

   @Override
   public Rectangle getBounds(Object var1) {
      return this.nodeBounds.get(var1);
   }

   @Override
   public Object getNodeAt(Point var1) {
      Map var2 = Maps.filterValues(this.nodeBounds, RectanglePredicates.contains(var1));
      return Iterables.getFirst(var2.keySet(), null);
   }

   @Override
   public Collection<Object> getNodesWithin(Rectangle var1) {
      Map var2 = Maps.filterValues(this.nodeBounds, RectanglePredicates.intersects(var1));
      return Sets.newHashSet(var2.keySet());
   }

   @Override
   public List<Object> getZOrder() {
      return Lists.newArrayList(this.dataModel.getNodes());
   }

   @Override
   public void move(Object var1, Point var2) {
   }

   @Override
   public void moveBy(Object var1, Point var2) {
   }

   @Override
   public void moveToTop(Object var1) {
   }

   @Override
   public void nodeSizeChanged(GraphNodeUi2D var1, Dimension var2) {
   }

   @Override
   public void resize(Object var1, Dimension var2) {
   }

   @Override
   public void resize(Object var1, int var2, int var3) {
   }

   private void buildLayout(Graph2D var1) {
      DependencyGraphLayout.NodeColumn var2 = new DependencyGraphLayout.NodeColumn(var1, this.dataModel.getDependsOn());
      DependencyGraphLayout.NodeColumn var3 = new DependencyGraphLayout.NodeColumn(var1, this.dataModel.getRequiredBy());
      Dimension var4 = var1.getNodeUi(this.dataModel.getEntity()).getMinimumSize();
      if (var2.isEmpty() && var3.isEmpty()) {
         this.nodeBounds.put(this.dataModel.getEntity(), new Rectangle(100, 100, var4.width, var4.height));
      } else {
         int var5 = this.getEntityX(var2, var3, var4.width);
         int var6 = this.getEntityY(var2, var3);
         this.nodeBounds.put(this.dataModel.getEntity(), new Rectangle(var5, var6 - var4.height / 2, var4.width, var4.height));
         this.nodeBounds.putAll(var2.bounds);
         this.nodeBounds.putAll(var3.bounds);
      }
   }

   private int getEntityX(DependencyGraphLayout.NodeColumn var1, DependencyGraphLayout.NodeColumn var2, int var3) {
      byte var4 = 50;
      int var5 = Math.max(var1.width + var4, 100);
      var2.shiftRight(var5 + var3 + var4);
      return var5;
   }

   private int getEntityY(DependencyGraphLayout.NodeColumn var1, DependencyGraphLayout.NodeColumn var2) {
      int var3 = 100;
      int var4 = Math.max(var1.height / 2, var2.height / 2);
      int var5 = Math.max(0, var4 - var3);
      var3 += var5;
      var1.centerVertically(var3);
      var2.centerVertically(var3);
      return var3;
   }

   private static class NodeColumn {
      private final Map<Object, Rectangle> bounds = Maps.newHashMap();
      private final int width;
      private final int height;

      public NodeColumn(Graph2D var1, Iterable<?> var2) {
         int var4 = 20;
         int var5 = 0;

         for (Object var7 : var2) {
            GraphNodeUi2D var8 = var1.getNodeUi(var7);
            Dimension var9 = var8.getMinimumSize();
            Rectangle var10 = new Rectangle(20, var4, var9.width, var9.height);
            this.bounds.put(var7, var10);
            if (var9.width > var5) {
               var5 = var9.width;
            }

            var4 += var9.height + 20;
         }

         this.width = 20 + var5;
         this.height = var4;
      }

      public boolean isEmpty() {
         return this.bounds.isEmpty();
      }

      public void centerVertically(int var1) {
         if (!this.isEmpty()) {
            int var2 = var1 - this.height / 2;
            if (var2 > 0) {
               for (Rectangle var4 : this.bounds.values()) {
                  var4.y += var2;
               }
            }
         }
      }

      public void shiftRight(int var1) {
         if (!this.isEmpty()) {
            for (Rectangle var3 : this.bounds.values()) {
               var3.x += var1;
            }
         }
      }
   }
}
