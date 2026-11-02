package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.dnd;

import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.SourceActivityNode;

public final class SourceActivityDropBehavior extends PipelineActivityDropBehavior {
   private final FileDropBehavior fileDrop;

   public SourceActivityDropBehavior(OperationGraphController var1, SourceActivityNode var2) {
      super(var1, var2);
      this.fileDrop = new FileDropBehavior(var1, var2);
   }

   @Override
   public boolean isDropAllowed(DropTargetDragEvent var1) {
      if (this.fileDrop.isDropAllowed(var1)) {
         return true;
      } else {
         return this.getNode().isReadOnly() ? false : super.isDropAllowed(var1);
      }
   }

   @Override
   public boolean handleDrop(DropTargetDropEvent var1) {
      return super.handleDrop(var1) || this.fileDrop.handleDrop(var1);
   }
}
