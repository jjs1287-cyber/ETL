package org.etlforge.ui.graph.layout;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

public class GraphLayoutEvent extends EventObject {
   private final List<Object> nodes;

   public GraphLayoutEvent(GraphLayout var1) {
      super(var1);
      this.nodes = new ArrayList<>();
   }

   public GraphLayoutEvent(GraphLayout var1, List<?> var2) {
      super(var1);
      this.nodes = new ArrayList<>(var2);
   }

   public GraphLayout getSource() {
      return (GraphLayout)super.getSource();
   }

   public List getNodes() {
      return new ArrayList<>(this.nodes);
   }
}
