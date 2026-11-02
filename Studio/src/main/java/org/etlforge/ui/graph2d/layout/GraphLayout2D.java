package org.etlforge.ui.graph2d.layout;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.List;
import org.etlforge.ui.graph.edge.GraphEdge;
import org.etlforge.ui.graph.layout.GraphLayout;
import org.etlforge.ui.graph2d.node.GraphNodeUi2D;

public interface GraphLayout2D extends GraphLayout {
   Object getNodeAt(Point var1);

   Collection<Object> getNodesWithin(Rectangle var1);

   Rectangle getBounds(Object var1);

   void resize(Object var1, int var2, int var3);

   void resize(Object var1, Dimension var2);

   void move(Object var1, Point var2);

   void moveBy(Object var1, Point var2);

   void moveToTop(Object var1);

   List<Object> getZOrder();

   boolean shouldBePainted(GraphEdge var1);

   void nodeSizeChanged(GraphNodeUi2D var1, Dimension var2);

   boolean isExpanded(Object var1);

   boolean isCollapsed(Object var1);

   void expand(Object var1);

   void collapse(Object var1);

   void addNodeExpansionListener(NodeExpansionListener var1);

   void removeNodeExpansionListener(NodeExpansionListener var1);
}
