package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.undo;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.etlforge.application.ui.Alert;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.resources.GraphResources;
import org.etlforge.integration.data.entity.EmailMessage;
import org.etlforge.integration.data.entity.id.EmailMessageId;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.operation.flow.OperationWrapper;
import org.etlforge.integration.data.entity.operation.flow.RouteType;

public class NextEmailEdit extends AbstractUndoableEdit {
   private final OperationGraphController controller;
   private final OperationId from;
   private final RouteType type;
   private final EmailMessageId oldEmail;
   private final EmailMessageId newEmail;

   public NextEmailEdit(OperationGraphController var1, OperationWrapper var2, RouteType var3, EmailMessage var4) {
      if (var1 == null) {
         throw new NullPointerException("controller");
      }

      if (var3 != RouteType.SUCCESS && var3 != RouteType.FAILURE) {
         throw new IllegalArgumentException("Invalid route type (only SUCCESS and FAILURE acceptable): " + var3);
      }

      this.controller = var1;
      this.from = var2.getActivity().getID();
      this.type = var3;
      this.oldEmail = var3 == RouteType.SUCCESS ? var2.getSuccessEmailId() : var2.getFailureEmailId();
      this.newEmail = var4 != null ? var4.getID() : null;
   }

   @Override
   public void redo() throws CannotRedoException {
      super.redo();
      this.setNextEmail(this.newEmail);
   }

   @Override
   public void undo() throws CannotUndoException {
      super.undo();
      this.setNextEmail(this.oldEmail);
   }

   @Override
   public String getPresentationName() {
      return GraphResources.format("NextEmailEdit", this.type);
   }

   private void setNextEmail(EmailMessageId var1) {
      OperationWrapper var2 = this.locateStartOp();
      if (var2 != null) {
         if (this.type == RouteType.SUCCESS) {
            var2.setSuccessEmailId(var1);
         } else {
            var2.setFailureEmailId(var1);
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
