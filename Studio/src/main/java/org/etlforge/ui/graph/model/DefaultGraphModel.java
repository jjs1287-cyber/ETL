package org.etlforge.ui.graph.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.etlforge.ui.graph.edge.DefaultGraphEdge;
import org.etlforge.ui.graph.edge.GraphEdge;

public class DefaultGraphModel implements GraphModel {
   private final Map<Object, DefaultGraphModel.Edges> nodesAndEdges = Maps.newHashMap();
   private final GraphModelEventSupport eventSupport = new GraphModelEventSupport(this);
   private Object root;

   public void setRoot(Object var1) {
      if (var1 != this.root) {
         Preconditions.checkArgument(var1 == null || this.contains(var1), "root is not part of this model");
         this.root = var1;
         this.removeOrphans();
         this.eventSupport.fireModelChanged();
      }
   }

   public void addEdge(Object var1, Object var2) {
      GraphEdge var3 = this.createEdge(var1, var2);
      this.addEdge(var3);
   }

   protected GraphEdge createEdge(Object var1, Object var2) {
      return new DefaultGraphEdge(var1, var2);
   }

   public void addEdge(GraphEdge var1) {
      this.eventSupport.startChange();
      DefaultGraphModel.Edges var2 = this.registerNodeAndGetItsEdges(var1.from());
      var2.outgoing.add(var1);
      DefaultGraphModel.Edges var3 = this.registerNodeAndGetItsEdges(var1.to());
      var3.incoming.add(var1);
      this.eventSupport.endChangeAndSendEvents();
   }

   private DefaultGraphModel.Edges registerNodeAndGetItsEdges(Object var1) {
      DefaultGraphModel.Edges var2 = this.nodesAndEdges.get(var1);
      if (var2 == null) {
         var2 = new DefaultGraphModel.Edges();
         this.nodesAndEdges.put(var1, var2);
      }

      return var2;
   }

   public void removeEdge(GraphEdge var1) {
      this.eventSupport.startChange();
      Object var2 = var1.from();
      Object var3 = var1.to();
      if (this.contains(var2) && this.contains(var3)) {
         DefaultGraphModel.Edges var4 = this.nodesAndEdges.get(var2);
         var4.outgoing.remove(var1);
         DefaultGraphModel.Edges var5 = this.nodesAndEdges.get(var3);
         var5.incoming.remove(var1);
         this.removeOrphans();
      }

      this.eventSupport.endChangeAndSendEvents();
   }

   public void removeNode(Object var1) {
      if (this.contains(var1)) {
         this.eventSupport.startChange();
         DefaultGraphModel.Edges var2 = this.nodesAndEdges.remove(var1);

         for (GraphEdge var4 : var2.outgoing) {
            Object var5 = var4.to();
            this.nodesAndEdges.get(var5).incoming.remove(var4);
         }

         for (GraphEdge var7 : var2.incoming) {
            Object var8 = var7.from();
            this.nodesAndEdges.get(var8).outgoing.remove(var7);
         }

         this.removeOrphans();
         this.eventSupport.endChangeAndSendEvents();
      }
   }

   private void removeOrphans() {
      if (this.root != null) {
         Set var1 = new DefaultGraphModel.NodeCollector().collect(this.root);
         Iterator var2 = this.nodesAndEdges.keySet().iterator();

         while (var2.hasNext()) {
            if (!var1.contains(var2.next())) {
               var2.remove();
            }
         }
      }
   }

   @Override
   public boolean contains(Object var1) {
      return this.nodesAndEdges.containsKey(var1);
   }

   public Set<? extends Object> getNodes() {
      return Sets.newHashSet(this.nodesAndEdges.keySet());
   }

   public Set<? extends GraphEdge> getEdges() {
      HashSet var1 = Sets.newHashSet();

      for (DefaultGraphModel.Edges var3 : this.nodesAndEdges.values()) {
         var1.addAll(var3.outgoing);
      }

      return var1;
   }

   public Set<? extends GraphEdge> getIncomingEdges(Object var1) {
      HashSet var2 = Sets.newHashSet();
      DefaultGraphModel.Edges var3 = this.nodesAndEdges.get(var1);
      if (var3 != null) {
         var2.addAll(var3.incoming);
      }

      return var2;
   }

   public Set<? extends GraphEdge> getOutgoingEdges(Object var1) {
      HashSet var2 = Sets.newHashSet();
      DefaultGraphModel.Edges var3 = this.nodesAndEdges.get(var1);
      if (var3 != null) {
         var2.addAll(var3.outgoing);
      }

      return var2;
   }

   @Override
   public Object getParentOf(Object var1) {
      return null;
   }

   @Override
   public boolean isGroup(Object var1) {
      return false;
   }

   @Override
   public void addModelListener(GraphModelListener var1) {
      this.eventSupport.addListener(var1);
   }

   @Override
   public void removeModelListener(GraphModelListener var1) {
      this.eventSupport.removeListener(var1);
   }

   public final GraphModelEventSupport getEventSupport() {
      return this.eventSupport;
   }

   private static final class Edges {
      public final Set<GraphEdge> outgoing = Sets.newHashSet();
      public final Set<GraphEdge> incoming = Sets.newHashSet();

      private Edges() {
      }
   }

   private class NodeCollector {
      private Set<Object> nodes;

      private NodeCollector() {
      }

      public Set<Object> collect(Object var1) {
         this.nodes = Sets.newHashSet();
         this.collectImpl(var1);
         return this.nodes;
      }

      private void collectImpl(Object var1) {
         if (!this.nodes.contains(var1)) {
            this.nodes.add(var1);
            DefaultGraphModel.Edges var2 = DefaultGraphModel.this.nodesAndEdges.get(var1);
            if (var2 != null) {
               this.followOutgoingEdges(var2);
               this.followIncomingEdges(var2);
            }
         }
      }

      private void followIncomingEdges(DefaultGraphModel.Edges var1) {
         for (GraphEdge var3 : var1.incoming) {
            this.collectImpl(var3.from());
         }
      }

      private void followOutgoingEdges(DefaultGraphModel.Edges var1) {
         for (GraphEdge var3 : var1.outgoing) {
            this.collectImpl(var3.to());
         }
      }
   }
}
