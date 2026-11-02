package org.etlforge.ui.graph.node;

import java.awt.geom.Point2D;
import org.etlforge.system.Disposable;
import org.etlforge.ui.graph.GraphUiElement;

public interface GraphNodeUi extends GraphUiElement, Disposable {
   Object getNodeObject();

   void setMoving(boolean var1);

   @Override
   boolean containsPoint(Point2D var1);
}
