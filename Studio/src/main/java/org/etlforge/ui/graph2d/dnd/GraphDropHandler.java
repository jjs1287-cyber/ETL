package org.etlforge.ui.graph2d.dnd;

import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.util.Collection;
import org.etlforge.ui.graph.edge.GraphEdge;

public interface GraphDropHandler {
   boolean isCanvasDropAllowed(DropTargetDragEvent var1);

   boolean handleCanvasDrop(DropTargetDropEvent var1);

   boolean isNodeDropAllowed(Object var1, DropTargetDragEvent var2);

   boolean handleNodeDrop(Object var1, DropTargetDropEvent var2);

   Collection<?> getNodeSelection();

   boolean isEdgeDropAllowed(GraphEdge var1, DropTargetDragEvent var2);

   boolean handleEdgeDrop(GraphEdge var1, DropTargetDropEvent var2);
}
