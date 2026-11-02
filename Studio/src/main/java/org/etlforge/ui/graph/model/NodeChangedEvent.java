package org.etlforge.ui.graph.model;

import com.google.common.base.Preconditions;

public class NodeChangedEvent extends GraphModelEvent {
   private final Object node;

   public NodeChangedEvent(GraphModel var1, Object var2) {
      super(var1);
      Preconditions.checkNotNull(var2, "node");
      this.node = var2;
   }

   public Object getNode() {
      return this.node;
   }
}
