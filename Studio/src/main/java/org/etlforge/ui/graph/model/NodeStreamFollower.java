package org.etlforge.ui.graph.model;

import com.google.common.collect.Sets;
import java.util.Set;
import org.etlforge.ui.graph.edge.GraphEdge;

public final class NodeStreamFollower {
   private final GraphModel model;
   private Set<Object> connected;
   private boolean includeImmediateNeighborsOnly;

   public NodeStreamFollower(GraphModel var1) {
      this.model = var1;
   }

   public void setInlcudeImmediateNeighborsOnly(boolean var1) {
      this.includeImmediateNeighborsOnly = var1;
   }

   public Set<Object> getDownstreamAndUpstreamNodes(Object var1) {
      Set var2 = this.getDownstreamNodes(var1);
      var2.addAll(this.getUpstreamNodes(var1));
      return var2;
   }

   public Set<Object> getDownstreamNodes(Object var1) {
      this.connected = Sets.newHashSet();
      this.connected.add(var1);
      this.followOutgoingEdges(var1);
      return this.connected;
   }

   public Set<Object> getUpstreamNodes(Object var1) {
      this.connected = Sets.newHashSet();
      this.connected.add(var1);
      this.followIncomingEdges(var1);
      return this.connected;
   }

   private void followOutgoingEdges(Object var1) {
      for (GraphEdge var3 : this.model.getOutgoingEdges(var1)) {
         Object var4 = var3.to();
         if (!this.connected.contains(var4)) {
            this.connected.add(var4);
            if (!this.includeImmediateNeighborsOnly) {
               this.followOutgoingEdges(var4);
            }
         }
      }
   }

   private void followIncomingEdges(Object var1) {
      for (GraphEdge var3 : this.model.getIncomingEdges(var1)) {
         Object var4 = var3.from();
         if (!this.connected.contains(var4)) {
            this.connected.add(var4);
            if (!this.includeImmediateNeighborsOnly) {
               this.followIncomingEdges(var4);
            }
         }
      }
   }
}
