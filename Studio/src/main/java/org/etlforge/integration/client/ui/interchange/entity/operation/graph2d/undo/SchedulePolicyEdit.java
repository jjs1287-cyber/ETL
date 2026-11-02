package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.undo;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.etlforge.application.ui.Alert;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.resources.GraphResources;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.operation.SchedulePolicy;
import org.etlforge.integration.data.entity.operation.flow.OperationWrapper;

public class SchedulePolicyEdit extends AbstractUndoableEdit {
   private final OperationGraphController controller;
   private final OperationId opId;
   private final SchedulePolicy oldPolicy;
   private final SchedulePolicy newPolicy;

   public SchedulePolicyEdit(OperationGraphController var1, OperationWrapper var2, SchedulePolicy var3) {
      this.controller = var1;
      this.opId = var2.getActivity().getID();
      this.oldPolicy = var2.getSchedulePolicy();
      this.newPolicy = var3;
   }

   @Override
   public void redo() throws CannotRedoException {
      super.redo();
      this.setPolicy(this.newPolicy);
   }

   @Override
   public void undo() throws CannotUndoException {
      super.undo();
      this.setPolicy(this.oldPolicy);
   }

   @Override
   public String getPresentationName() {
      return GraphResources.getString("SchedulePolicyEdit");
   }

   private void setPolicy(SchedulePolicy var1) {
      OperationWrapper var2 = this.locateOperation();
      if (var2 != null) {
         if (this.controller.isChangeAllowed(var2)) {
            var2.setSchedulePolicy(var1);
         }
      } else {
         Alert.error("This change cannot be undone/redone.", "Not Undoable/Redoable");
      }
   }

   private OperationWrapper locateOperation() {
      for (OperationWrapperNode var2 : this.controller.getGraph().getModel().getOperationNodes()) {
         if (var2.getOperationId().equals(this.opId)) {
            return var2.getDataObject();
         }
      }

      return null;
   }
}
