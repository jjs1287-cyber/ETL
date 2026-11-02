package org.etlforge.ui.graph2d.edge;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import org.etlforge.ui.graph.edge.GraphEdgeUi;
import org.etlforge.ui.graph2d.dnd.DropStatus;

public interface GraphEdgeUi2D extends GraphEdgeUi {
   void setStartPoint(Point2D var1);

   Point2D getStartPoint();

   void setEndPoint(Point2D var1);

   Point2D getEndPoint();

   void setDropStatus(DropStatus var1);

   void paint(Graphics2D var1);
}
