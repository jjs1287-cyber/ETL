package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.layout;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.OperationActivityEdge;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.OperationGraph2D;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.OperationGraphModel;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.event.OperationTypeChangedEvent;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.EmailWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
import org.etlforge.integration.data.entity.id.EmailMessageId;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.operation.flow.RouteType;
import org.etlforge.lang.exception.NotImplementedYetException;
import org.etlforge.ui.graph.edge.GraphEdge;
import org.etlforge.ui.graph.layout.GraphLayoutMemento;
import org.etlforge.ui.graph.model.GraphModelEvent;
import org.etlforge.ui.graph.model.NodeExpansionState;
import org.etlforge.ui.graph.model.NodesConnectedEvent;
import org.etlforge.ui.graph.model.NodesInsertedEvent;
import org.etlforge.ui.graph.model.NodesRemovedEvent;
import org.etlforge.ui.graph2d.layout.AbstractGraphLayout2D;
import org.etlforge.ui.graph2d.layout.GraphLayout2D;
import org.etlforge.ui.graph2d.node.GraphNodeUi2D;
import org.etlforge.ui.graphics.RectanglePredicates;
import org.etlforge.util.persist.Persistor;

public final class OperationGraphLayout extends AbstractGraphLayout2D implements GraphLayout2D {
   static final int MIN_X = 5;
   static final int MIN_Y = 5;
   private final Map<OperationWrapperNode, OperationNodeLayout> opNodeLayouts;
   private final Map<EmailWrapperNode, Rectangle> emailNodeBounds;
   private final OperationGraphModel dataModel;
   private final ZOrder zOrder;
   private OperationNodeLayoutCache opLayoutCache;
   private EmailNodeLayoutCache emailLayoutCache;
   private boolean hideEmailNodes;
   private RouteTypeFilter edgeFilter;

   public OperationGraphLayout(OperationGraph2D var1) {
      super(var1);
      this.dataModel = var1.getModel();
      this.opNodeLayouts = Maps.newHashMap();
      this.emailNodeBounds = Maps.newHashMap();
      this.emailLayoutCache = new EmailNodeLayoutCache();
      this.zOrder = new ZOrder(this);
      this.opLayoutCache = new OperationNodeLayoutCache();
      this.setInitialExpansionState();
      this.setRouteTypesToShow(EnumSet.allOf(RouteType.class));
      this.buildInitialLayout();
      this.buildInitialZOrder();
   }

   protected OperationGraph2D getGraph() {
      return (OperationGraph2D)super.getGraph();
   }

   @Override
   public Rectangle getBounds(Object var1) {
      if (var1 instanceof EmailWrapperNode) {
         return this.emailNodeBounds.get(var1);
      }

      OperationNodeLayout var2 = this.getOperationLayoutFor(var1);
      return var2 != null ? var2.getBounds(var1) : null;
   }

   @Override
   public Object getNodeAt(Point var1) {
      List var2 = this.getNodesThatContainsPoint(var1);
      if (var2.isEmpty()) {
         return null;
      }

      if (var2.size() > 1) {
         this.sortNodeListAfterZOrder(var2);
      }

      for (Object var4 : var2) {
         if (this.isPointContainedByNodeUi(var4, var1)) {
            return var4;
         }
      }

      return null;
   }

   public void collapseAll() {
      for (OperationWrapperNode var2 : this.dataModel.getOperationNodes()) {
         this.collapse(var2);
      }
   }

   public void setRouteTypesToShow(EnumSet<RouteType> var1) {
      this.edgeFilter = new RouteTypeFilter(this.dataModel.getStartNode(), var1);
   }

   public EnumSet<RouteType> getRouteTypesToShow() {
      return this.edgeFilter.getAcceptedTypes();
   }

   public void setHideEmailNodes(boolean var1) {
      this.hideEmailNodes = var1;
   }

   public boolean isHidingEmailNodes() {
      return this.hideEmailNodes;
   }

   private List<Object> getNodesThatContainsPoint(Point var1) {
      ArrayList var2 = Lists.newArrayList();
      Predicate var3 = this.edgeFilter.asNodeFilter();

      for (OperationNodeLayout var5 : this.opNodeLayouts.values()) {
         if (var3.apply(var5.getOperationNode())) {
            var5.collect(var1, var2);
         }
      }

      if (!this.hideEmailNodes) {
         this.collectEmailNodes(RectanglePredicates.contains(var1), var3, var2);
      }

      return var2;
   }

   private void collectEmailNodes(Predicate<Rectangle> var1, Predicate<Object> var2, List<Object> var3) {
      Map var4 = Maps.filterValues(this.emailNodeBounds, var1);
      Iterables.addAll(var3, Iterables.filter(var4.keySet(), var2));
   }

   private boolean isPointContainedByNodeUi(Object var1, Point var2) {
      Rectangle var3 = this.getBounds(var1);
      Point var4 = new Point(var2);
      var4.translate(-var3.x, -var3.y);
      return this.getGraph().getNodeUi(var1).containsPoint(var4);
   }

   @Override
   public Collection<Object> getNodesWithin(Rectangle var1) {
      ArrayList var2 = Lists.newArrayList();
      Predicate var3 = this.edgeFilter.asNodeFilter();

      for (OperationNodeLayout var5 : this.opNodeLayouts.values()) {
         if (var3.apply(var5.getOperationNode())) {
            var5.collect(var1, var2);
         }
      }

      if (!this.hideEmailNodes) {
         this.collectEmailNodes(RectanglePredicates.intersects(var1), var3, var2);
      }

      return var2;
   }

   @Override
   public List<Object> getZOrder() {
      ArrayList var1 = Lists.newArrayList(this.zOrder.getZOrder());
      if (this.hideEmailNodes) {
         Iterables.removeIf(var1, Predicates.instanceOf(EmailWrapperNode.class));
      }

      return KongaListUtils.filter(var1, this.edgeFilter.asNodeFilter());
   }

   @Override
   public boolean shouldBePainted(GraphEdge var1) {
      Object var2 = var1.from();
      if (this.isExpanded(var2) && var1 instanceof OperationActivityEdge && ((OperationActivityEdge)var1).getType() == RouteType.SCRIPTED) {
         return false;
      } else {
         return this.hideEmailNodes && var1.to() instanceof EmailWrapperNode ? false : this.edgeFilter.apply(var1);
      }
   }

   @Override
   public void move(Object var1, Point var2) {
      Point var3 = new Point(Math.max(5, var2.x), Math.max(5, var2.y));
      if (var1 instanceof OperationWrapperNode) {
         this.moveOperationNodeTo((OperationWrapperNode)var1, var3);
      } else {
         if (!(var1 instanceof EmailWrapperNode)) {
            throw new NotImplementedYetException();
         }

         this.moveEmailNodeTo((EmailWrapperNode)var1, var3);
      }
   }

   @Override
   public void moveBy(Object var1, Point var2) {
      if (var1 instanceof EmailWrapperNode) {
         this.moveEmailNodeBy((EmailWrapperNode)var1, var2);
      } else {
         OperationNodeLayout var3 = this.getOperationLayoutFor(var1);
         if (var3 == null) {
            throw new RuntimeException("This should not happen");
         }

         var3.moveBy(var1, var2);
      }
   }

   @Override
   public void moveToTop(Object var1) {
      if (this.zOrder.moveToTop(var1)) {
         this.fireZOrderChanged();
      }
   }

   @Override
   public void resize(Object var1, int var2, int var3) {
      throw new NotImplementedYetException();
   }

   @Override
   public void resize(Object var1, Dimension var2) {
      GraphNodeUi2D var3 = this.getGraph().getNodeUi(var1);
      if (!var3.isResizable()) {
         throw new IllegalArgumentException("node is not resizable");
      }

      Dimension var4 = var3.getSize();
      Dimension var5 = var3.getMinimumSize();
      var2.width = Math.max(var2.width, var5.width);
      var2.height = Math.max(var2.height, var5.height);
      if (!var4.equals(var2)) {
         if (var1 instanceof EmailWrapperNode) {
            this.resizeEmailNode((EmailWrapperNode)var1, var2);
         } else {
            OperationNodeLayout var6 = this.getOperationLayoutFor(var1);
            if (var6 == null) {
               throw new NotImplementedYetException();
            }

            var6.resize(var1, var2);
         }
      }
   }

   public void resetLayout() {
      this.opNodeLayouts.clear();
      this.emailNodeBounds.clear();
      this.buildInitialLayout();
      this.buildInitialZOrder();
      this.fireLayoutChanged();
   }

   public void resetOperationNodeLayout(OperationWrapperNode var1) {
      OperationNodeLayout var2 = this.opNodeLayouts.get(var1);
      var2.reset();
      this.fireLayoutChanged();
   }

   @Override
   public void dataModelChanged(GraphModelEvent var1) {
      if (var1 instanceof OperationTypeChangedEvent) {
         this.handleOperationTypeChanged((OperationTypeChangedEvent)var1);
      } else {
         this.rebuildEdgeFilter();
         this.resetLayout();
      }
   }

   private void handleOperationTypeChanged(OperationTypeChangedEvent var1) {
      OperationWrapperNode var2 = var1.getNode();
      this.zOrder.removeOperation(var2);
      OperationNodeLayout var3 = this.opNodeLayouts.remove(var2);
      this.opLayoutCache.add(var3);
      OperationNodeLayout var4 = new OperationNodeLayout(this, var2, null, this.opLayoutCache);
      this.opNodeLayouts.put(var2, var4);
      var4.setOperationNodeLocation(var3.getOperationNodeBounds().getLocation());
      this.zOrder.add(var2, Lists.newArrayList(var2.getChildren()));
      this.fireLayoutChanged();
   }

   private void rebuildEdgeFilter() {
      OperationWrapperNode var1 = this.dataModel.getStartNode();
      this.edgeFilter = this.edgeFilter.copy(var1);
   }

   @Override
   public GraphLayoutMemento getMemento() {
      return new OperationGraphLayoutMemento(this);
   }

   @Override
   protected void nodeWasCollapsed(Object var1) {
      this.nodeExpansionStateChanged(var1);
   }

   @Override
   protected void nodeWasExpanded(Object var1) {
      this.nodeExpansionStateChanged(var1);
   }

   private void nodeExpansionStateChanged(final Object var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            OperationNodeLayout var1x = OperationGraphLayout.this.getOperationLayoutFor(var1);
            if (var1x != null) {
               var1x.expansionStateChanged();
            }

            OperationGraphLayout.this.updateEdges(var1);
            OperationGraphLayout.this.fireLayoutChanged();
         }
      });
   }

   @Override
   public void nodeSizeChanged(GraphNodeUi2D var1, Dimension var2) {
      Object var3 = var1.getNodeObject();
      if (!(var3 instanceof EmailWrapperNode)) {
         OperationNodeLayout var4 = this.getOperationLayoutFor(var3);
         if (var4 == null) {
            throw new NotImplementedYetException();
         }

         var4.setNewSize(var3, var2);
      }
   }

   @Override
   public void nodesConnected(NodesConnectedEvent var1) {
      GraphEdge var2 = var1.getNewEdge();
      this.updateEdge(var2);
      super.fireLayoutChanged(Arrays.asList(var2.from(), var2.to()));
   }

   @Override
   public void nodesInserted(NodesInsertedEvent var1) {
      List var2 = var1.getInserted();
      Object var3 = var2.get(0);
      if (var3 instanceof OperationWrapperNode) {
         this.insertOperationNode((OperationWrapperNode)var3, var1.getBefore());
      } else if (var3 instanceof EmailWrapperNode) {
         this.insertNewEmailNode((EmailWrapperNode)var3, var1.getBefore());
      } else {
         Object var4 = var1.getBefore();
         Object var5 = var1.getAfter();
         OperationNodeLayout var6 = var4 != null ? this.getOperationLayoutFor(var4) : this.getOperationLayoutFor(var5);
         var6.insertPipelineActivityNodes(var4, var5, var2);
      }
   }

   private void insertOperationNode(OperationWrapperNode var1, Object var2) {
      OperationNodeLayout var3 = new OperationNodeLayout(this, var1, var2, this.opLayoutCache);
      this.opNodeLayouts.put(var1, var3);
      if (var3.isCollapsed()) {
         this.zOrder.add(var1, Lists.newArrayList());
      } else {
         this.zOrder.add(var1, Lists.newArrayList(var1.getChildren()));
      }

      this.zOrder.moveToTop(var1);
      this.fireLayoutChanged();
      this.expand(var1);
   }

   private void insertNewEmailNode(EmailWrapperNode var1, Object var2) {
      GraphNodeUi2D var3 = this.getGraph().getNodeUi(var1);
      Dimension var4 = var3.getPreferredSize();
      Rectangle var5 = this.emailLayoutCache.getBounds(var1.getEmailId());
      if (var5 == null) {
         int var6 = 5;
         int var7 = 5;
         Rectangle var8 = this.getBounds(var2);
         if (var8 != null) {
            var6 = var8.x + 40;
            var7 = var8.y + var8.height + 40;
         }

         var5 = new Rectangle(var6, var7, var4.width, var4.height);
      }

      this.emailNodeBounds.put(var1, var5);
      this.zOrder.add(var1);
      this.zOrder.moveToTop(var1);
      this.fireLayoutChanged(var1);
   }

   @Override
   public void nodesRemoved(NodesRemovedEvent var1) {
      List var2 = var1.getRemoved();
      if (var2.size() == 1) {
         Object var3 = var2.get(0);
         if (var3 instanceof OperationWrapperNode) {
            OperationWrapperNode var8 = (OperationWrapperNode)var3;
            this.zOrder.removeOperation(var8);
            OperationNodeLayout var9 = this.opNodeLayouts.remove(var3);
            this.opLayoutCache.add(var9);
            this.fireLayoutChanged();
            return;
         }

         if (var3 instanceof EmailWrapperNode) {
            EmailWrapperNode var7 = (EmailWrapperNode)var3;
            this.zOrder.removeEmail(var7);
            Rectangle var5 = this.emailNodeBounds.remove(var7);
            this.emailLayoutCache.put(var7.getEmailId(), var5);
            this.fireLayoutChanged();
            return;
         }
      }

      this.zOrder.removePipelineNodes(var2);

      for (Object __etlItem434 : this.collectOperationLayouts(var2)) {
         OperationNodeLayout var4 = (OperationNodeLayout) __etlItem434;
         var4.remove(var2);
      }

      this.fireLayoutChanged();
   }

   ZOrder getZOrderList() {
      return this.zOrder;
   }

   public void setDefaultExpansionState() {
      OperationWrapperNode var1 = this.dataModel.getStartNode();

      for (OperationWrapperNode var3 : this.dataModel.getOperationNodes()) {
         if (var3 == var1) {
            this.expand(var3);
         } else {
            this.collapse(var3);
         }
      }
   }

   public void writeTo(Persistor var1) {
      this.writeVersion(var1);
      this.persistOperationNodeLayouts(var1);
      this.persistEmailNodeLayouts(var1);
      this.persistCachedLayouts(var1);
   }

   private void writeVersion(Persistor var1) {
      var1.putString("version", ClientInfo.getCurrentVersion().toString());
   }

   private void persistOperationNodeLayouts(Persistor var1) {
      for (OperationNodeLayout var3 : this.opNodeLayouts.values()) {
         OperationNodeLayoutMemento var4 = new OperationNodeLayoutMemento(var3);
         var4.writeTo(var1.createChild("Operation"));
      }
   }

   private void persistEmailNodeLayouts(Persistor var1) {
      for (Entry var3 : this.emailNodeBounds.entrySet()) {
         Persistor var4 = var1.createChild("Email");
         var4.putString("emailId", ((EmailWrapperNode)var3.getKey()).getDataObject().getActivity().getID().toString());
         PersistUtils.writeBounds(var4, (Rectangle)var3.getValue());
      }
   }

   private void persistCachedLayouts(Persistor var1) {
      Persistor var2 = var1.createChild("Cache");
      this.opLayoutCache.writeTo(var2);
      this.emailLayoutCache.writeTo(var2);
   }

   public void readFrom(Persistor var1) {
      try {
         this.restoreOperationLayouts(var1);
         this.restoreEmailLayouts(var1);
         this.restoreCachedLayouts(var1);
      } catch (Exception var3) {
         ErrorLog.attention(OperationGraphLayout.class, "Failed to restore the graph layout.", var3);
         this.buildInitialLayout();
      }

      this.fireLayoutChanged();
   }

   private void restoreOperationLayouts(Persistor var1) {
      Map var2 = this.readOperationLayoutMementos(var1);

      for (OperationWrapperNode var4 : this.dataModel.getOperationNodes()) {
         OperationNodeLayoutMemento var5 = (OperationNodeLayoutMemento)var2.get(var4.getOperationId());
         Object var6 = null;
         if (var5 != null) {
            var6 = new OperationNodeLayout(this, var4, null, var5);
            if (var5.isCollapsed()) {
               this.collapse(var4);
            } else {
               this.expand(var4);
            }
         } else {
            var6 = new OperationNodeLayout(this, var4);
         }

         this.opNodeLayouts.put(var4, (OperationNodeLayout)var6);
      }
   }

   private Map<OperationId, OperationNodeLayoutMemento> readOperationLayoutMementos(Persistor var1) {
      HashMap var2 = Maps.newHashMap();

      for (Persistor var4 : var1.getChildren("Operation")) {
         OperationNodeLayoutMemento var5 = new OperationNodeLayoutMemento(var4);
         var2.put(var5.getOperationId(), var5);
      }

      return var2;
   }

   private void restoreEmailLayouts(Persistor var1) {
      for (Persistor var3 : var1.getChildren("Email")) {
         EmailMessageId var4 = new EmailMessageId(var3.getString("emailId"));
         EmailWrapperNode var5 = this.getEmailNodeWithId(var4);
         if (var5 != null) {
            this.emailNodeBounds.put(var5, PersistUtils.readBounds(var3));
         }
      }
   }

   private EmailWrapperNode getEmailNodeWithId(EmailMessageId var1) {
      for (EmailWrapperNode var3 : this.emailNodeBounds.keySet()) {
         if (var3.getDataObject().getActivity().getID().equals(var1)) {
            return var3;
         }
      }

      return null;
   }

   private void restoreCachedLayouts(Persistor var1) {
      Persistor var2 = var1.getFirstChild("Cache");
      if (var2 != null) {
         this.opLayoutCache = new OperationNodeLayoutCache(var2);
         this.emailLayoutCache = new EmailNodeLayoutCache(var2);
      }
   }

   public void printToConsole() {
      StringBuilder var1 = new StringBuilder();

      for (OperationNodeLayout var3 : this.opNodeLayouts.values()) {
         var3.reportTo(var1);
      }

      System.out.println(var1);
   }

   @Override
   protected void fireLayoutChanged() {
      super.fireLayoutChanged();
   }

   @Override
   protected void fireLayoutChanged(List var1) {
      super.fireLayoutChanged(var1);
   }

   @Override
   protected void fireLayoutChanged(Object var1) {
      super.fireLayoutChanged(var1);
   }

   private Collection<OperationNodeLayout> collectOperationLayouts(List<Object> var1) {
      HashSet var2 = new HashSet();

      for (Object var4 : var1) {
         OperationNodeLayout var5 = this.getOperationLayoutFor(var4);
         if (var5 != null) {
            var2.add(var5);
         }
      }

      return var2;
   }

   @Override
   public boolean isCollapsed(Object var1) {
      return this.getEffectiveNodeExpansionState(var1) == NodeExpansionState.COLLAPSED;
   }

   private NodeExpansionState getEffectiveNodeExpansionState(Object var1) {
      NodeExpansionState var2 = super.getExpansionState(var1);
      if (var2 == null) {
         var2 = var1 == this.dataModel.getStartNode() ? NodeExpansionState.EXPANDED : NodeExpansionState.COLLAPSED;
      }

      return var2;
   }

   @Override
   public boolean isExpanded(Object var1) {
      return this.getEffectiveNodeExpansionState(var1) == NodeExpansionState.EXPANDED;
   }

   private void setInitialExpansionState() {
      for (OperationWrapperNode var2 : this.dataModel.getOperationNodes()) {
         NodeExpansionState var3 = var2 == this.dataModel.getStartNode() ? NodeExpansionState.EXPANDED : NodeExpansionState.COLLAPSED;
         this.setExpansionState(var2, var3);
      }
   }

   private void buildInitialLayout() {
      for (OperationWrapperNode var2 : this.dataModel.getOperationNodes()) {
         this.opNodeLayouts.put(var2, new OperationNodeLayout(this, var2));
      }

      for (EmailWrapperNode var6 : this.dataModel.getEmailNodes()) {
         Dimension var3 = this.getGraph().getNodeUi(var6).getPreferredSize();
         this.emailNodeBounds.put(var6, new Rectangle(5, 5, var3.width, var3.height));
      }

      InitialLayoutPositioner var5 = new InitialLayoutPositioner(this.dataModel, this.opNodeLayouts, this.emailNodeBounds);
      var5.adjustLayout();
   }

   private void buildInitialZOrder() {
      this.zOrder.clear();

      for (OperationWrapperNode var2 : this.dataModel.getOperationNodes()) {
         if (this.isCollapsed(var2)) {
            this.zOrder.add(var2, Lists.newArrayList());
         } else {
            this.zOrder.add(var2, Lists.newArrayList(var2.getChildren()));
         }
      }

      for (EmailWrapperNode var4 : this.dataModel.getEmailNodes()) {
         this.zOrder.add(var4);
      }
   }

   private OperationNodeLayout getOperationLayoutFor(Object var1) {
      if (var1 instanceof OperationWrapperNode) {
         return this.opNodeLayouts.get(var1);
      }

      if (var1 instanceof PipelineActivityNode) {
         PipelineActivityNode var2 = (PipelineActivityNode)var1;
         OperationWrapperNode var3 = var2.getParent();
         if (var3 != null) {
            return this.opNodeLayouts.get(var3);
         }

         for (OperationNodeLayout var5 : this.opNodeLayouts.values()) {
            if (var5.contains(var2)) {
               return var5;
            }
         }

         return null;
      } else {
         return null;
      }
   }

   private void sortNodeListAfterZOrder(List<Object> var1) {
      Collections.sort(var1, new Comparator<Object>() {
         @Override
         public int compare(Object var1, Object var2) {
            return OperationGraphLayout.this.getZOrder(var1) - OperationGraphLayout.this.getZOrder(var2);
         }
      });
   }

   private int getZOrder(Object var1) {
      return this.zOrder.getZOrder(var1);
   }

   private void moveEmailNodeBy(EmailWrapperNode var1, Point var2) {
      Rectangle var3 = this.emailNodeBounds.get(var1);
      Point var4 = new Point(Math.max(var3.x + var2.x, 5), Math.max(var3.y + var2.y, 5));
      if (!var4.equals(var3.getLocation())) {
         var3.setLocation(var4);
         this.fireLayoutChanged(var1);
      }
   }

   private void moveEmailNodeTo(EmailWrapperNode var1, Point var2) {
      Rectangle var3 = this.emailNodeBounds.get(var1);
      if (!var2.equals(var3.getLocation())) {
         var3.setLocation(var2);
         this.fireLayoutChanged(var1);
      }
   }

   private void resizeEmailNode(EmailWrapperNode var1, Dimension var2) {
      Rectangle var3 = this.emailNodeBounds.get(var1);
      var3.setSize(var2);
      this.fireLayoutChanged(var1);
   }

   private void moveOperationNodeTo(OperationWrapperNode var1, Point var2) {
      OperationNodeLayout var3 = this.getOperationLayoutFor(var1);
      if (var3 != null) {
         var3.moveTo(var2);
      } else {
         throw new RuntimeException("This should not happen");
      }
   }

   private void updateEdges(Object var1) {
      HashSet var2 = Sets.newHashSet(this.dataModel.getIncomingEdges(var1));
      var2.addAll(this.dataModel.getOutgoingEdges(var1));

      for (Object __etlItem729 : var2) {
         GraphEdge var4 = (GraphEdge) __etlItem729;
         this.updateEdge(var4);
      }
   }
}
