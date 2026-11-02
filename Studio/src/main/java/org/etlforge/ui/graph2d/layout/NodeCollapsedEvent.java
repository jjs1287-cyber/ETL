package org.etlforge.ui.graph2d.layout;

import org.etlforge.ui.graph.layout.GraphLayoutEvent;

public final class NodeCollapsedEvent extends GraphLayoutEvent {
   private final Object groupNode;

   public NodeCollapsedEvent(GraphLayout2D var1, Object var2) {
      super(var1);
      if (var2 == null) {
         throw new NullPointerException("groupNode");
      }

      this.groupNode = var2;
   }

   public GraphLayout2D getSource() {
      return (GraphLayout2D)super.getSource();
   }

   public Object getGroupNode() {
      return this.groupNode;
   }
}
