package org.etlforge.ui.graph2d.edge;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public interface LineSegmentFactory {
   Line2D[] createSegments(Point2D var1, Point2D var2);
}
