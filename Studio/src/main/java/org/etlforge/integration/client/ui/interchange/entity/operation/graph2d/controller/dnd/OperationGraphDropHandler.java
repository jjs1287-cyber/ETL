package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.dnd;

import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.util.Collection;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.PipelineActivityEdge;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
import org.etlforge.ui.graph.edge.GraphEdge;
import org.etlforge.ui.graph2d.dnd.GraphDropHandler;

public final class OperationGraphDropHandler implements GraphDropHandler {
   private final OperationGraphController controller;
   private Collection<?> newSelection;

   public OperationGraphDropHandler(OperationGraphController var1) {
      this.controller = var1;
   }

   @Override
   public boolean isCanvasDropAllowed(DropTargetDragEvent var1) {
      return false;
   }

   @Override
   public boolean handleCanvasDrop(DropTargetDropEvent var1) {
      return false;
   }

   @Override
   public boolean isNodeDropAllowed(Object var1, DropTargetDragEvent var2) {
      DropBehavior var3 = this.getNodeDropBehavior(var1);
      return var3.isDropAllowed(var2);
   }

   private DropBehavior getNodeDropBehavior(Object var1) {
      if (var1 instanceof PipelineActivityNode) {
         return ((PipelineActivityNode)var1).getDropBehavior(this.controller);
      } else {
         return var1 instanceof OperationWrapperNode ? new OperationDropBehavior(this.controller, (OperationWrapperNode)var1) : new RejectDrop();
      }
   }

   @Override
   public boolean handleNodeDrop(Object var1, DropTargetDropEvent var2) {
      DropBehavior var3 = this.getNodeDropBehavior(var1);
      return this.handleDrop(var2, var3);
   }

   private boolean handleDrop(DropTargetDropEvent var1, DropBehavior var2) {
      boolean var3 = var2.handleDrop(var1);
      this.newSelection = var3 ? var2.getNewSelection() : null;
      return var3;
   }

   @Override
   public boolean handleEdgeDrop(GraphEdge var1, DropTargetDropEvent var2) {
      DropBehavior var3 = this.getEdgeDropBehavior(var1);
      return this.handleDrop(var2, var3);
   }

   private DropBehavior getEdgeDropBehavior(GraphEdge var1) {
      if (var1 instanceof PipelineActivityEdge) {
         PipelineActivityEdge var2 = (PipelineActivityEdge)var1;
         return new ActivityConnectionDropBehavior(this.controller, var2.from().getParent(), var2.asConnectionPoint());
      } else {
         return new RejectDrop();
      }
   }

   @Override
   public boolean isEdgeDropAllowed(GraphEdge var1, DropTargetDragEvent var2) {
      DropBehavior var3 = this.getEdgeDropBehavior(var1);
      return var3.isDropAllowed(var2);
   }

   @Override
   public Collection<?> getNodeSelection() {
      return this.newSelection;
   }
}
