package org.etlforge.ui.graph2d.layout;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.etlforge.ui.graph.layout.GraphLayoutMemento;
import org.etlforge.ui.graph.model.GraphModel;
import org.etlforge.ui.graph.model.GraphModelEvent;
import org.etlforge.ui.graph.model.NodesConnectedEvent;
import org.etlforge.ui.graph.model.NodesInsertedEvent;
import org.etlforge.ui.graph.model.NodesRemovedEvent;
import org.etlforge.ui.graph2d.Graph2D;
import org.etlforge.ui.graph2d.RelativePosition;
import org.etlforge.ui.graph2d.node.GraphNodeUi2D;
import org.etlforge.ui.graphics.RectanglePredicates;

public class DefaultGraphLayout2D extends AbstractGraphLayout2D implements GraphLayout2D {
   private static final int MIN_X = 5;
   private static final int MIN_Y = 5;
   private final GraphModel dataModel = this.getGraph().getModel();
   private final Map<Object, Rectangle> nodeBounds = Maps.newHashMap();
   private final LinkedList<Object> zOrder = new LinkedList<>();

   public DefaultGraphLayout2D(Graph2D var1) {
      super(var1);
      this.setInitialZOrder();
   }

   public void doInitialLayout(LayoutAlgorithm var1) {
      this.nodeBounds.putAll(var1.doLayout());
      this.updateEdges();
      this.fireLayoutChanged();
   }

   public int getZOrder(Object var1) {
      return this.zOrder.indexOf(var1);
   }

   @Override
   public List<Object> getZOrder() {
      return null;
   }

   @Override
   public void moveToTop(Object var1) {
      if (this.getZOrder(var1) > 0) {
         this.setToLowestZOrder(var1);
         this.fireZOrderChanged();
      }
   }

   @Override
   public void collapse(Object var1) {
   }

   @Override
   public void expand(Object var1) {
   }

   @Override
   public boolean isCollapsed(Object var1) {
      return false;
   }

   @Override
   public boolean isExpanded(Object var1) {
      return false;
   }

   private void setInitialZOrder() {
      for (Object var2 : this.dataModel.getNodes()) {
         this.zOrder.addFirst(var2);
      }
   }

   private void setToLowestZOrder(Object var1) {
      this.zOrder.remove(var1);
      this.zOrder.addFirst(var1);
   }

   @Override
   public Rectangle getBounds(Object var1) {
      return this.nodeBounds.get(var1);
   }

   @Override
   public Object getNodeAt(Point var1) {
      ArrayList var2 = Lists.newArrayList(Maps.filterValues(this.nodeBounds, RectanglePredicates.contains(var1)).keySet());
      if (var2.isEmpty()) {
         return null;
      }

      if (var2.size() > 1) {
         Collections.sort(var2, new Comparator<Object>() {
            @Override
            public int compare(Object var1, Object var2x) {
               return DefaultGraphLayout2D.this.getZOrder(var1) - DefaultGraphLayout2D.this.getZOrder(var2x);
            }
         });
      }

      return var2.get(0);
   }

   @Override
   public Collection<Object> getNodesWithin(Rectangle var1) {
      return Lists.newArrayList(Maps.filterValues(this.nodeBounds, RectanglePredicates.intersects(var1)).keySet());
   }

   @Override
   public void move(Object var1, Point var2) {
      Rectangle var3 = this.nodeBounds.get(var1);
      if (var3 != null) {
         this.moveNodeImpl(var1, var3, var2);
      }
   }

   private void moveNodeImpl(Object var1, Rectangle var2, Point var3) {
      var3.x = Math.max(var3.x, 5);
      var3.y = Math.max(var3.y, 5);
      Point var4 = var2.getLocation();
      if (!var4.equals(var3)) {
         Rectangle var5 = new Rectangle(var2);
         Rectangle var6 = new Rectangle(var2);
         var6.setLocation(var3);
         this.setToLowestZOrder(var1);
         DefaultGraphLayout2D.LayoutUpdater var7 = new DefaultGraphLayout2D.LayoutUpdater(var1, var5, var6);
         var7.update();
      }
   }

   @Override
   public void moveBy(Object var1, Point var2) {
      Rectangle var3 = this.nodeBounds.get(var1);
      if (var3 != null) {
         Point var4 = new Point(var3.x + var2.x, var3.y + var2.y);
         this.moveNodeImpl(var1, var3, var4);
      }
   }

   @Override
   public void resize(Object var1, int var2, int var3) {
      if (var2 != 0 && var3 != 0) {
         GraphNodeUi2D var4 = this.getGraph().getNodeUi(var1);
         if (!var4.isResizable()) {
            throw new IllegalArgumentException("node is not resizable");
         }

         Dimension var5 = var4.getSize();
         Dimension var6 = new Dimension(var5.width + var2, var5.height + var3);
         this.resizeImpl(var1, var4, var5, var6);
      }
   }

   @Override
   public void resize(Object var1, Dimension var2) {
      GraphNodeUi2D var3 = this.getGraph().getNodeUi(var1);
      if (!var3.isResizable()) {
         throw new IllegalArgumentException("node is not resizable");
      }

      Dimension var4 = var3.getSize();
      this.resizeImpl(var1, var3, var4, var2);
   }

   private void resizeImpl(Object var1, GraphNodeUi2D var2, Dimension var3, Dimension var4) {
      Dimension var5 = var2.getMinimumSize();
      var4.width = Math.max(var4.width, var5.width);
      var4.height = Math.max(var4.height, var5.height);
      if (!var3.equals(var4)) {
         Rectangle var6 = this.nodeBounds.get(var1);
         Rectangle var7 = new Rectangle(var6.getLocation(), var4);
         ArrayList var8 = new ArrayList();
         var8.add(var1);
         this.setToLowestZOrder(var1);
         if (this.isLayoutUpdateNeededAfterResize(var6.getSize(), var7.getSize())) {
            DefaultGraphLayout2D.LayoutUpdater var9 = new DefaultGraphLayout2D.LayoutUpdater(var1, var6, var7);
            var9.update();
         } else {
            this.nodeBounds.put(var1, var7);
            this.fireLayoutChanged(var1);
         }
      }
   }

   private boolean isLayoutUpdateNeededAfterResize(Dimension var1, Dimension var2) {
      return var2.width > var1.width || var2.height > var1.height;
   }

   @Override
   public GraphLayoutMemento getMemento() {
      return null;
   }

   @Override
   public void nodesInserted(NodesInsertedEvent var1) {
      new DefaultGraphLayout2D.NodeInserter(var1).insert();
   }

   @Override
   public void nodesRemoved(NodesRemovedEvent var1) {
      super.nodesRemoved(var1);
      ArrayList var2 = new ArrayList();

      for (Object var4 : var1.getRemoved()) {
         this.nodeBounds.remove(var4);
         this.zOrder.remove(var4);
         var2.add(var4);
      }

      this.fireLayoutChanged(var2);
   }

   @Override
   public void dataModelChanged(GraphModelEvent var1) {
   }

   @Override
   public void nodesConnected(NodesConnectedEvent var1) {
   }

   @Override
   public void nodeSizeChanged(GraphNodeUi2D var1, Dimension var2) {
   }

   private final class LayoutUpdater {
      private final Map<Object, Rectangle> affectedNodes;
      private boolean veto;

      public LayoutUpdater(Object var2, Rectangle var3, Rectangle var4) {
         assert !var3.equals(var4);
         this.affectedNodes = Maps.newIdentityHashMap();
         this.affectedNodes.put(var2, var4);
      }

      public void update() {
         if (!this.veto) {
            this.apply();
         }
      }

      private void apply() {
         assert !this.veto : "The layout change has been vetoed, and should not be applied";
         ArrayList var1 = new ArrayList();

         for (Entry var3 : this.affectedNodes.entrySet()) {
            DefaultGraphLayout2D.this.nodeBounds.put(var3.getKey(), (Rectangle)var3.getValue());
            var1.add(var3.getKey());
         }

         DefaultGraphLayout2D.this.fireLayoutChanged(var1);
      }
   }

   private final class NodeInserter {
      private final NodesInsertedEvent evt;
      private final Rectangle startBounds;
      private final Rectangle endBounds;
      private Map<Object, Rectangle> nodesToShift;
      private int shift;

      public NodeInserter(NodesInsertedEvent var2) {
         this.evt = var2;
         this.startBounds = DefaultGraphLayout2D.this.nodeBounds.get(var2.getBefore());
         this.endBounds = DefaultGraphLayout2D.this.nodeBounds.get(var2.getAfter());
         this.getNodesToShift();
      }

      public void insert() {
         Rectangle var1 = this.startBounds;
         List var2 = this.evt.getInserted();
         int var3 = var2.size();

         for (Object var5 : var2) {
            DefaultGraphLayout2D.this.zOrder.addFirst(var5);
            Rectangle var6 = this.applyNewBounds(var5, var1, var3);
            var1 = var6;
            var3--;
         }

         this.shiftNodes();
         DefaultGraphLayout2D.this.fireLayoutChanged();
      }

      private Rectangle applyNewBounds(Object var1, Rectangle var2, int var3) {
         GraphNodeUi2D var4 = DefaultGraphLayout2D.this.getGraph().getNodeUi(var1);
         Dimension var5 = var4.getSize();
         int var6 = this.getX(var2);
         int var7 = this.getY(var2, this.endBounds, var5, var3);
         Rectangle var8 = new Rectangle(var6, var7, var5.width, var5.height);
         this.shift = this.shift + var5.width;
         DefaultGraphLayout2D.this.nodeBounds.put(var1, var8);
         return var8;
      }

      private void getNodesToShift() {
         this.nodesToShift = Maps.newIdentityHashMap();

         for (Entry var2 : DefaultGraphLayout2D.this.nodeBounds.entrySet()) {
            Object var3 = var2.getKey();
            Rectangle var4 = (Rectangle)var2.getValue();
            if (this.startBounds == null || RelativePosition.RIGHT.isTrue(var4, this.startBounds)) {
               this.nodesToShift.put(var3, new Rectangle(var4));
            }
         }
      }

      private void shiftNodes() {
         for (Entry var2 : this.nodesToShift.entrySet()) {
            Rectangle var10000 = (Rectangle)var2.getValue();
            var10000.x = var10000.x + this.shift;
            DefaultGraphLayout2D.this.nodeBounds.put(var2.getKey(), (Rectangle)var2.getValue());
         }
      }

      private int getX(Rectangle var1) {
         if (var1 != null) {
            byte var2 = 50;
            this.shift += var2;
            return var1.x + var1.width + var2;
         } else {
            this.shift += 5;
            return 5;
         }
      }

      private int getY(Rectangle var1, Rectangle var2, Dimension var3, int var4) {
         if (var1 == null && var2 == null) {
            return 5;
         }

         int var5 = 0;
         if (var1 != null && var2 != null) {
            int var6 = this.getMidY(var1);
            int var7 = this.getMidY(var2);
            var5 = var6 + (var7 - var6) / var4;
         } else if (var1 != null) {
            var5 = this.getMidY(var1);
         } else if (var2 != null) {
            var5 = this.getMidY(var2);
         }

         return var5 - var3.height / 2;
      }

      private int getMidY(Rectangle var1) {
         return var1.y + var1.height / 2;
      }
   }
}
