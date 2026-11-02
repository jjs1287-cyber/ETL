package org.etlforge.ui.graph.model;

import org.etlforge.ui.graph.edge.GraphEdge;

public final class NodesConnectedEvent extends GraphModelEvent {
   private final GraphEdge newEdge;

   public NodesConnectedEvent(GraphModel var1, GraphEdge var2) {
      super(var1);
      this.newEdge = var2;
   }

   public GraphEdge getNewEdge() {
      return this.newEdge;
   }
}
