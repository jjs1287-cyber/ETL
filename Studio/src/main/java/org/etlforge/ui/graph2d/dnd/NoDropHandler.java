package org.etlforge.ui.graph2d.dnd;

import com.google.common.collect.Lists;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.util.Collection;
import org.etlforge.ui.graph.edge.GraphEdge;

public class NoDropHandler implements GraphDropHandler {
   @Override
   public boolean isCanvasDropAllowed(DropTargetDragEvent var1) {
      return false;
   }

   @Override
   public boolean isNodeDropAllowed(Object var1, DropTargetDragEvent var2) {
      return false;
   }

   @Override
   public boolean handleCanvasDrop(DropTargetDropEvent var1) {
      return false;
   }

   @Override
   public boolean handleNodeDrop(Object var1, DropTargetDropEvent var2) {
      return false;
   }

   @Override
   public boolean handleEdgeDrop(GraphEdge var1, DropTargetDropEvent var2) {
      return false;
   }

   @Override
   public boolean isEdgeDropAllowed(GraphEdge var1, DropTargetDragEvent var2) {
      return false;
   }

   @Override
   public Collection<?> getNodeSelection() {
      return Lists.newArrayList();
   }
}
