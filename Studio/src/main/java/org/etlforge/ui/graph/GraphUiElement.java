package org.etlforge.ui.graph;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

public interface GraphUiElement {
   boolean handleMouseEvent(MouseEvent var1);

   String getToolTipText(MouseEvent var1);

   boolean containsPoint(Point2D var1);
}
