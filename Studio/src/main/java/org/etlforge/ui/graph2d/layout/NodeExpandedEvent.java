package org.etlforge.ui.graph2d.layout;

import java.util.ArrayList;
import java.util.List;
import org.etlforge.ui.graph.layout.GraphLayoutEvent;

public final class NodeExpandedEvent extends GraphLayoutEvent {
   private final Object groupNode;
   private final List<Object> memberNodes;

   public NodeExpandedEvent(GraphLayout2D var1, Object var2) {
      this(var1, var2, null);
   }

   public NodeExpandedEvent(GraphLayout2D var1, Object var2, List<? extends Object> var3) {
      super(var1);
      if (var2 == null) {
         throw new NullPointerException("groupNode");
      }

      this.groupNode = var2;
      this.memberNodes = var3 != null ? new ArrayList<>(var3) : new ArrayList<>();
   }

   public GraphLayout2D getSource() {
      return (GraphLayout2D)super.getSource();
   }

   public Object getGroupNode() {
      return this.groupNode;
   }

   public List<Object> getMemberNodes() {
      return new ArrayList<>(this.memberNodes);
   }
}
