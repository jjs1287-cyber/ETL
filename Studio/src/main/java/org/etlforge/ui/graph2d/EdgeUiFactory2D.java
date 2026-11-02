package org.etlforge.ui.graph2d;

import org.etlforge.ui.graph.edge.GraphEdge;
import org.etlforge.ui.graph2d.edge.GraphEdgeUi2D;

public interface EdgeUiFactory2D {
   GraphEdgeUi2D createUi(Graph2D var1, GraphEdge var2);
}
