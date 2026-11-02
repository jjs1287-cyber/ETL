package org.etlforge.ui.graph.edge;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import org.etlforge.ui.graph.GraphUiElement;

public interface GraphEdgeUi extends GraphUiElement {
   GraphEdge getEdge();

   @Override
   boolean containsPoint(Point2D var1);

   @Override
   boolean handleMouseEvent(MouseEvent var1);

   void addGraphEdgeListener(GraphEdgeListener var1);

   void removeGraphEdgeListener(GraphEdgeListener var1);
}
