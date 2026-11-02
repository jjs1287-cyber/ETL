package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.undo;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.etlforge.application.ui.Alert;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.resources.GraphResources;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.operation.flow.OperationWrapper;
import org.etlforge.integration.data.entity.operation.options.OperationOptions;

public class OperationOptionsEdit extends AbstractUndoableEdit {
   private final OperationGraphController controller;
   private final OperationId opId;
   private final OperationOptions oldOptions;
   private final OperationOptions newOptions;

   public OperationOptionsEdit(OperationGraphController var1, OperationWrapper var2, OperationOptions var3) {
      this.controller = var1;
      this.opId = var2.getActivity().getID();
      this.oldOptions = var2.getOptions();
      this.newOptions = var3;
   }

   @Override
   public void redo() throws CannotRedoException {
      super.redo();
      this.setOptions(this.newOptions);
   }

   @Override
   public void undo() throws CannotUndoException {
      super.undo();
      this.setOptions(this.oldOptions);
   }

   @Override
   public String getPresentationName() {
      return GraphResources.getString("OptionsEdit");
   }

   private void setOptions(OperationOptions var1) {
      OperationWrapper var2 = this.locateOperation();
      if (var2 != null) {
         if (this.controller.isChangeAllowed(var2)) {
            var2.setOptions(var1);
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
