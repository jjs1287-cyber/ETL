package org.etlforge.ui.graph2d.layout;

import com.google.common.collect.Maps;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D.Double;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import org.etlforge.ui.graph.edge.GraphEdge;
import org.etlforge.ui.graph.layout.AbstractGraphLayout;
import org.etlforge.ui.graph.layout.GraphLayoutMemento;
import org.etlforge.ui.graph.model.GraphModelEvent;
import org.etlforge.ui.graph.model.NodeExpansionState;
import org.etlforge.ui.graph.model.NodesConnectedEvent;
import org.etlforge.ui.graph.model.NodesInsertedEvent;
import org.etlforge.ui.graph.model.NodesRemovedEvent;
import org.etlforge.ui.graph2d.Graph2D;
import org.etlforge.ui.graph2d.edge.GraphEdgeUi2D;
import org.etlforge.ui.graph2d.node.GraphNodeUi2D;
import org.etlforge.ui.graphics.LineUtils;
import org.etlforge.ui.graphics.PointUtils;

public abstract class AbstractGraphLayout2D extends AbstractGraphLayout implements GraphLayout2D {
   private final Graph2D graph;
   private final Map<Object, NodeExpansionState> expansionState;
   private final List<NodeExpansionListener> expansionListeners;

   protected AbstractGraphLayout2D(Graph2D var1) {
      this.graph = var1;
      this.expansionListeners = new CopyOnWriteArrayList<>();
      this.expansionState = Maps.newHashMap();
   }

   @Override
   public void collapse(Object var1) {
      if (this.expansionState.get(var1) != NodeExpansionState.COLLAPSED) {
         this.expansionState.put(var1, NodeExpansionState.COLLAPSED);
         this.fireNodeCollapsed(var1);
         this.nodeWasCollapsed(var1);
      }
   }

   protected void nodeWasCollapsed(Object var1) {
   }

   protected final NodeExpansionState getExpansionState(Object var1) {
      return this.expansionState.get(var1);
   }

   @Override
   public void expand(Object var1) {
      if (this.expansionState.get(var1) != NodeExpansionState.EXPANDED) {
         this.expansionState.put(var1, NodeExpansionState.EXPANDED);
         this.fireNodeExpanded(var1);
         this.nodeWasExpanded(var1);
      }
   }

   protected void nodeWasExpanded(Object var1) {
   }

   @Override
   public boolean isCollapsed(Object var1) {
      return this.expansionState.get(var1) == NodeExpansionState.COLLAPSED;
   }

   @Override
   public boolean isExpanded(Object var1) {
      return this.expansionState.get(var1) == NodeExpansionState.EXPANDED;
   }

   protected final void setExpansionState(Object var1, NodeExpansionState var2) {
      this.expansionState.put(var1, var2);
   }

   @Override
   public boolean shouldBePainted(GraphEdge var1) {
      return true;
   }

   @Override
   public final void addNodeExpansionListener(NodeExpansionListener var1) {
      this.expansionListeners.add(var1);
   }

   @Override
   public final void removeNodeExpansionListener(NodeExpansionListener var1) {
      this.expansionListeners.add(var1);
   }

   @Override
   public void dataModelChanged(GraphModelEvent var1) {
   }

   @Override
   public GraphLayoutMemento getMemento() {
      return null;
   }

   @Override
   public void nodesConnected(NodesConnectedEvent var1) {
   }

   @Override
   public void nodesInserted(NodesInsertedEvent var1) {
   }

   @Override
   public void nodesRemoved(NodesRemovedEvent var1) {
      for (Object var3 : var1.getRemoved()) {
         this.expansionState.remove(var3);
      }
   }

   protected Graph2D getGraph() {
      return this.graph;
   }

   @Override
   protected void fireLayoutChanged() {
      this.preLayoutChangeEvent();
      super.fireLayoutChanged();
   }

   @Override
   protected void fireLayoutChanged(Object var1) {
      this.preLayoutChangeEvent();
      super.fireLayoutChanged(var1);
   }

   @Override
   protected void fireLayoutChanged(List var1) {
      this.preLayoutChangeEvent();
      super.fireLayoutChanged(var1);
   }

   private void preLayoutChangeEvent() {
      this.updateEdges();
   }

   protected final void updateEdges() {
      for (GraphEdge var2 : this.graph.getModel().getEdges()) {
         this.updateEdge(var2);
      }
   }

   protected final void updateEdge(GraphEdge var1) {
      Object var2 = var1.from();
      Object var3 = var1.to();
      Line2D var4 = this.getLinkPoints(var2, var3);
      if (var4 != null) {
         GraphEdgeUi2D var5 = this.graph.getEdgeUi(var1);
         if (var5 == null) {
            return;
         }

         var5.setStartPoint(var4.getP1());
         var5.setEndPoint(var4.getP2());
      }
   }

   protected void fireNodeCollapsed(Object var1) {
      NodeCollapsedEvent var2 = new NodeCollapsedEvent(this, var1);

      for (NodeExpansionListener var4 : this.expansionListeners) {
         var4.nodeCollapsed(var2);
      }
   }

   protected void fireNodeExpanded(Object var1) {
      this.fireNodeExpanded(var1, null);
   }

   protected void fireNodeExpanded(Object var1, List<? extends Object> var2) {
      NodeExpandedEvent var3 = new NodeExpandedEvent(this, var1, var2);

      for (NodeExpansionListener var5 : this.expansionListeners) {
         var5.nodeExpanded(var3);
      }
   }

   private Collection<? extends Line2D> getNonCrossoverEdges(
      Rectangle var1, Collection<? extends Point2D> var2, Rectangle var3, Collection<? extends Point2D> var4
   ) {
      ArrayList var5 = new ArrayList();

      for (Point2D var7 : var2) {
         for (Point2D var9 : var4) {
            Double var10 = new Double(var7.getX(), var7.getY(), var9.getX(), var9.getY());
            if (!var1.intersectsLine(var10) && !var3.intersectsLine(var10)) {
               var5.add(var10);
            }
         }
      }

      return var5;
   }

   private Line2D getLinkPoints(Object var1, Object var2) {
      GraphNodeUi2D var3 = this.graph.getNodeUi(var1);
      GraphNodeUi2D var4 = this.graph.getNodeUi(var2);
      if (var3 != null && var4 != null) {
         List var5 = var3.getConnectionPoints();
         List var6 = var4.getConnectionPoints();
         Rectangle var7 = this.getBounds(var1);
         Rectangle var8 = this.getBounds(var2);
         if (var7 != null && var8 != null) {
            Rectangle var9 = new Rectangle((int)var7.getX() + 1, (int)var7.getY() + 1, (int)var7.getWidth() - 2, (int)var7.getHeight() - 2);
            Rectangle var10 = new Rectangle((int)var8.getX() + 1, (int)var8.getY() + 1, (int)var8.getWidth() - 2, (int)var8.getHeight() - 2);
            Set var11 = PointUtils.translatePoints(var5, new Point(var7.x, var7.y));
            Set var12 = PointUtils.translatePoints(var6, new Point(var8.x, var8.y));
            Collection var13 = this.getNonCrossoverEdges(var9, var11, var10, var12);
            return var13.isEmpty() ? PointUtils.getMinimumDistance(var11, var12) : LineUtils.findShortest(var13);
         } else {
            return null;
         }
      } else {
         return null;
      }
   }
}
