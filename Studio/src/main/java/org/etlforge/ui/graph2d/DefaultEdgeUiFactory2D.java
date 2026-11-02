package org.etlforge.ui.graph2d;

import org.etlforge.ui.graph.edge.GraphEdge;
import org.etlforge.ui.graph2d.edge.GraphEdgeUi2D;
import org.etlforge.ui.graph2d.edge.LineSegmentGraphEdgeUi;

public class DefaultEdgeUiFactory2D implements EdgeUiFactory2D {
   @Override
   public GraphEdgeUi2D createUi(Graph2D var1, GraphEdge var2) {
      return new LineSegmentGraphEdgeUi(var1, var2);
   }
}
