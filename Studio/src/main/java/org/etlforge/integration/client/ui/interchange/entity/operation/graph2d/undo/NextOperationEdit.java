package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.undo;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.etlforge.application.ui.Alert;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.resources.GraphResources;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.operation.flow.OperationWrapper;
import org.etlforge.integration.data.entity.operation.flow.RouteType;

public class NextOperationEdit extends AbstractUndoableEdit {
   private final OperationGraphController controller;
   private final OperationId from;
   private final RouteType type;
   private final OperationId oldOp;
   private final OperationId newOp;

   public NextOperationEdit(OperationGraphController var1, OperationWrapper var2, RouteType var3, Operation var4) {
      if (var1 == null) {
         throw new NullPointerException("controller");
      }

      if (var3 != RouteType.SUCCESS && var3 != RouteType.FAILURE) {
         throw new IllegalArgumentException("Invalid route type (only SUCCESS and FAILURE acceptable): " + var3);
      }

      this.controller = var1;
      this.from = var2.getActivity().getID();
      this.type = var3;
      this.oldOp = var3 == RouteType.SUCCESS ? var2.getSuccessOperationId() : var2.getFailureOperationId();
      this.newOp = var4 != null ? var4.getID() : null;
   }

   @Override
   public void redo() throws CannotRedoException {
      super.redo();
      this.setNextOp(this.newOp);
   }

   @Override
   public void undo() throws CannotUndoException {
      super.undo();
      this.setNextOp(this.oldOp);
   }

   @Override
   public String getPresentationName() {
      return GraphResources.format("NextOpEdit", this.type);
   }

   private void setNextOp(OperationId var1) {
      OperationWrapper var2 = this.locateStartOp();
      if (var2 != null) {
         if (this.type == RouteType.SUCCESS) {
            var2.setSuccessOperationId(var1);
         } else {
            var2.setFailureOperationId(var1);
         }
      } else {
         Alert.error("This change cannot be undone/redone.", "Not Undoable/Redoable");
      }
   }

   private OperationWrapper locateStartOp() {
      for (OperationWrapperNode var2 : this.controller.getGraph().getModel().getOperationNodes()) {
         if (var2.getOperationId().equals(this.from)) {
            return var2.getDataObject();
         }
      }

      return null;
   }
}
