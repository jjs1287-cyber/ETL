package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.List;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationSourceDataRunner;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.SourceActivityNode;

final class FileDropBehavior {
   private final OperationGraphController controller;
   private final OperationWrapperNode opNode;
   private final SourceActivityNode sourceNode;

   public FileDropBehavior(OperationGraphController var1, SourceActivityNode var2) {
      this.controller = var1;
      this.opNode = var2.getParent();
      this.sourceNode = var2;
   }

   public FileDropBehavior(OperationGraphController var1, OperationWrapperNode var2) {
      this.controller = var1;
      this.opNode = var2;
      this.sourceNode = this.getSourceNode(var2);
   }

   private SourceActivityNode getSourceNode(OperationWrapperNode var1) {
      for (PipelineActivityNode var3 : var1.getChildren()) {
         if (var3 instanceof SourceActivityNode) {
            return (SourceActivityNode)var3;
         }
      }

      return null;
   }

   public boolean isDropAllowed(DropTargetDragEvent var1) {
      return this.sourceNode != null && this.getDraggedFile(var1) != null;
   }

   private File getDraggedFile(DropTargetDragEvent var1) {
      try {
         if (var1.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            List var2 = (List)var1.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
            if (var2.size() == 1) {
               return (File)var2.get(0);
            }
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      return null;
   }

   public boolean handleDrop(DropTargetDropEvent var1) {
      File var2 = this.getDroppedFile(var1);
      return var2 != null ? this.startOperationWithDroppedFile(var2) : false;
   }

   private File getDroppedFile(DropTargetDropEvent var1) {
      try {
         if (var1.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            var1.acceptDrop(1);
            List var2 = (List)var1.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
            if (var2.size() == 1) {
               return (File)var2.get(0);
            }
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      return null;
   }

   private boolean startOperationWithDroppedFile(File var1) {
      OperationSourceDataRunner var2 = new OperationSourceDataRunner(this.controller.getRunnerFactory(), this.opNode);
      if (var2.isApplicableOperation()) {
         var2.runOperation(var1);
         Object var3 = null;
         return true;
      } else {
         return false;
      }
   }
}
