package org.etlforge.ui.graph.model;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.etlforge.collections.KongaSetUtils;
import org.etlforge.ui.graph.edge.GraphEdge;

public final class GraphModelState {
   private final GraphModel model;
   private Set<? extends GraphEdge> previousEdges;
   private Map<Object, Object> previousNodesMappedToGroup;
   private Set<? extends GraphEdge> currentEdges;
   private Map<Object, Object> currentNodesMappedToGroup;
   private Set<? extends Object> addedNodes;
   private Set<? extends Object> removedNodes;
   private Set<? extends GraphEdge> addedEdges;
   private Set<? extends GraphEdge> removedEdges;
   private Function<Iterable<? extends GraphEdge>, Iterable<? extends GraphEdge>> addEdgesOrderingStrategy;

   public GraphModelState(GraphModel var1) {
      this.model = var1;
      this.previousEdges = Sets.newHashSet(var1.getEdges());
      this.previousNodesMappedToGroup = this.buildMemberToGroupMap();
      this.addEdgesOrderingStrategy = Functions.identity();
   }

   private Map<Object, Object> buildMemberToGroupMap() {
      HashMap var1 = Maps.newHashMap();

      for (Object var3 : this.model.getNodes()) {
         Object var4 = this.model.getParentOf(var3);
         var1.put(var3, var4);
      }

      return var1;
   }

   public void calculateDiffAndSendEvents(GraphModelEventSupport var1, boolean var2) {
      this.currentEdges = Sets.newHashSet(this.model.getEdges());
      this.currentNodesMappedToGroup = this.buildMemberToGroupMap();
      this.collectDifferences(var2);
      this.previousEdges = this.currentEdges;
      this.previousNodesMappedToGroup = this.currentNodesMappedToGroup;
      this.sendEvents(var1, var2);
   }

   public void setAddEdgesOrderingStrategy(Function<Iterable<? extends GraphEdge>, Iterable<? extends GraphEdge>> var1) {
      this.addEdgesOrderingStrategy = var1;
   }

   private void collectDifferences(boolean var1) {
      this.findAddedNodes();
      this.findRemovedNodes(var1);
      this.findAddedEdges();
      this.findRemovedEdges();
   }

   private void findAddedNodes() {
      this.addedNodes = KongaSetUtils.createAssymetricSetDifference(this.currentNodesMappedToGroup.keySet(), this.previousNodesMappedToGroup.keySet());
   }

   private void findRemovedNodes(boolean var1) {
      this.removedNodes = KongaSetUtils.createAssymetricSetDifference(this.previousNodesMappedToGroup.keySet(), this.currentNodesMappedToGroup.keySet());
      if (var1) {
         this.removeMemberNodesFromRemovedNodes();
      }
   }

   private void removeMemberNodesFromRemovedNodes() {
      Iterator var1 = this.removedNodes.iterator();

      while (var1.hasNext()) {
         Object var2 = var1.next();
         if (this.belongsToRemovedGroup(var2)) {
            var1.remove();
         }
      }
   }

   private boolean belongsToRemovedGroup(Object var1) {
      Object var2 = this.previousNodesMappedToGroup.get(var1);
      return var2 != null && this.removedNodes.contains(var2);
   }

   private void findAddedEdges() {
      this.addedEdges = KongaSetUtils.createAssymetricSetDifference(this.currentEdges, this.previousEdges);
   }

   private void findRemovedEdges() {
      this.removedEdges = KongaSetUtils.createAssymetricSetDifference(this.previousEdges, this.currentEdges);
      this.removeMemberNodeEdgesFromRemovedEdges();
   }

   private void removeMemberNodeEdgesFromRemovedEdges() {
      Iterator var1 = this.removedEdges.iterator();

      while (var1.hasNext()) {
         GraphEdge var2 = (GraphEdge)var1.next();
         if (this.belongsToRemovedGroup(var2.to()) || this.belongsToRemovedGroup(var2.from())) {
            var1.remove();
         }
      }
   }

   private void sendEvents(GraphModelEventSupport var1, boolean var2) {
      for (GraphEdge var4 : this.removedEdges) {
         if (!this.removedNodes.contains(var4.from()) && !this.removedNodes.contains(var4.to())) {
            var1.fireNodesDisconnected(var4);
         }
      }

      if (var2) {
         for (Object var9 : this.removedNodes) {
            var1.fireNodeRemoved(var9);
         }
      } else if (!this.removedNodes.isEmpty()) {
         var1.fireNodesRemoved(Lists.newArrayList(this.removedNodes));
      }

      if (!this.addedNodes.isEmpty()) {
         GraphModelState.NodesInsertedEventGenerator var7 = new GraphModelState.NodesInsertedEventGenerator(this.addedNodes, this.addedEdges);

         for (NodesInsertedEvent var5 : var7.events()) {
            var1.fireNodesInserted(var5);
         }
      }

      for (GraphEdge var11 : this.addEdgesOrderingStrategy.apply(this.addedEdges)) {
         var1.fireNodesConnected(var11);
      }
   }

   private class MemberNodesOrder {
      private final Set<Object> nodes;
      private final List<Object> order;
      private final Set<GraphEdge> edges;

      public MemberNodesOrder(Collection<Object> var2) {
         this.nodes = Sets.newHashSet(var2);
         this.order = Lists.newArrayList();
         this.edges = Sets.newHashSet();
         this.collectInterestingEdges();
         this.calculateOrder();
      }

      public List<Object> getOrder() {
         return this.order;
      }

      private void collectInterestingEdges() {
         for (GraphEdge var2 : GraphModelState.this.addedEdges) {
            if (this.isInterestingEdge(var2)) {
               this.edges.add(var2);
            }
         }
      }

      private boolean isInterestingEdge(GraphEdge var1) {
         return this.nodes.contains(var1.from()) && this.nodes.contains(var1.to());
      }

      private void calculateOrder() {
         int var1 = this.nodes.size();

         while (this.order.size() < var1) {
            int var2 = this.order.size();
            Iterator var3 = this.nodes.iterator();

            while (var3.hasNext()) {
               Object var4 = var3.next();
               if (this.canBeAdded(var4)) {
                  this.order.add(var4);
                  var3.remove();
               }
            }

            int var5 = this.order.size();
            if (var5 == var2) {
               this.addRemainingNodesInAnyOrder();
            }
         }
      }

      private boolean canBeAdded(Object var1) {
         GraphEdge var2 = this.findIncomingEdge(var1);
         return var2 == null || this.order.contains(var2.from());
      }

      private GraphEdge findIncomingEdge(Object var1) {
         for (GraphEdge var3 : this.edges) {
            if (var3.to() == var1) {
               return var3;
            }
         }

         return null;
      }

      private void addRemainingNodesInAnyOrder() {
         for (Object var2 : this.nodes) {
            this.order.add(var2);
         }
      }
   }

   private class NodesInsertedEventGenerator {
      private final Multimap<Object, Object> fromNodeMappedToNewNodes = HashMultimap.create();
      private final Map<Object, Object> newNodeMappedToNextNode = Maps.newHashMap();
      private final Multimap<Object, Object> parentNodeMappedToMembers = HashMultimap.create();

      public NodesInsertedEventGenerator(Set<? extends Object> var2, Set<? extends GraphEdge> var3) {
         for (Object var5 : var2) {
            Object var6 = GraphModelState.this.model.getParentOf(var5);
            if (var6 != null && var2.contains(var6)) {
               this.parentNodeMappedToMembers.put(var6, var5);
            } else {
               Object var7 = this.getFromNode(var3, var5);
               this.fromNodeMappedToNewNodes.put(var7, var5);
               Object var8 = this.getNextNode(var3, var5);
               this.newNodeMappedToNextNode.put(var5, var8);
            }
         }
      }

      private Object getFromNode(Set<? extends GraphEdge> var1, Object var2) {
         for (GraphEdge var4 : var1) {
            if (var4.to() == var2) {
               return var4.from();
            }
         }

         return null;
      }

      private Object getNextNode(Set<? extends GraphEdge> var1, Object var2) {
         for (GraphEdge var4 : var1) {
            if (var4.from() == var2) {
               return var4.to();
            }
         }

         return null;
      }

      private Iterable<NodesInsertedEvent> events() {
         ArrayList var1 = Lists.newArrayList();
         this.createEventsForNonMemberNodes(var1);
         this.createEventsForMemberNodes(var1);
         return var1;
      }

      private void createEventsForNonMemberNodes(List<NodesInsertedEvent> var1) {
         for (Object var3 : this.fromNodeMappedToNewNodes.keySet()) {
            ArrayList var4 = Lists.newArrayList();
            ArrayList var5 = Lists.newArrayList();

            for (Object var7 : this.fromNodeMappedToNewNodes.get(var3)) {
               var4.add(var7);
               var4.addAll(this.parentNodeMappedToMembers.removeAll(var7));
               Object var8 = this.newNodeMappedToNextNode.get(var7);
               if (var8 != null) {
                  var5.add(var8);
               }
            }

            Object var9 = var5.size() == 1 ? var5.get(0) : null;
            NodesInsertedEvent var10 = new NodesInsertedEvent(GraphModelState.this.model, var3, var9, var4);
            var1.add(var10);
         }
      }

      private void createEventsForMemberNodes(List<NodesInsertedEvent> var1) {
         for (Object var3 : this.parentNodeMappedToMembers.keySet()) {
            GraphModelState.MemberNodesOrder var4 = GraphModelState.this.new MemberNodesOrder(this.parentNodeMappedToMembers.get(var3));
            NodesInsertedEvent var5 = new NodesInsertedEvent(GraphModelState.this.model, null, null, var4.getOrder());
            var1.add(var5);
         }
      }
   }
}
