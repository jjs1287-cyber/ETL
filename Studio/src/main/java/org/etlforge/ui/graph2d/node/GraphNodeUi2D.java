package org.etlforge.ui.graph2d.node;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.List;
import javax.swing.JComponent;
import org.etlforge.ui.graph.node.GraphNodeUi;
import org.etlforge.ui.graph2d.GraphNodeEditor;
import org.etlforge.ui.graph2d.dnd.DropStatus;

public interface GraphNodeUi2D extends GraphNodeUi {
   boolean isResizable();

   Dimension getSize();

   void setSize(Dimension var1);

   Dimension getPreferredSize();

   Dimension getMinimumSize();

   void setDropStatus(DropStatus var1);

   JComponent getRenderer(boolean var1);

   void addSizeListener(NodeSizeListener var1);

   void removeSizeListener(NodeSizeListener var1);

   List<Point2D> getConnectionPoints();

   GraphNodeEditor getEditor();
}
