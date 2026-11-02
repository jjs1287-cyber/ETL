package org.etlforge.ui.graph2d.edge;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D.Float;

public class SingleLineSegmentFactory implements LineSegmentFactory {
   @Override
   public Line2D[] createSegments(Point2D var1, Point2D var2) {
      return new Line2D[]{new Float(var1, var2)};
   }
}
