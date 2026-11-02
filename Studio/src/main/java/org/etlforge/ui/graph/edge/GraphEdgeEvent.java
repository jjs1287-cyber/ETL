package org.etlforge.ui.graph.edge;

import java.util.EventObject;

public class GraphEdgeEvent extends EventObject {
   public GraphEdgeEvent(GraphEdgeUi var1) {
      super(var1);
   }

   public GraphEdgeUi getSource() {
      return (GraphEdgeUi)super.getSource();
   }
}
