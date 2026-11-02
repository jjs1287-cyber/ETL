package org.etlforge.ui.graph.edge;

import java.util.EventListener;

public interface GraphEdgeListener extends EventListener {
   void edgeLayoutChanged(GraphEdgeEvent var1);

   void edgeStateChanged(GraphEdgeEvent var1);
}
