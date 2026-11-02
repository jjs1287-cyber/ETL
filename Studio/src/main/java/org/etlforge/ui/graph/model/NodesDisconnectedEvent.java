package org.etlforge.ui.graph.model;

import org.etlforge.ui.graph.edge.GraphEdge;

public class NodesDisconnectedEvent extends GraphModelEvent {
   private final GraphEdge removedEdge;

   public NodesDisconnectedEvent(GraphModel var1, GraphEdge var2) {
      super(var1);
      this.removedEdge = var2;
   }

   public GraphEdge getRemovedEdge() {
      return this.removedEdge;
   }
}
