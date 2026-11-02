package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.undo;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.etlforge.application.ui.Alert;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.resources.GraphResources;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.id.ScheduleId;
import org.etlforge.integration.data.entity.operation.flow.OperationWrapper;

public class OperationScheduleEdit extends AbstractUndoableEdit {
   private final OperationGraphController controller;
   private final OperationId opId;
   private final ScheduleId prevScheduleId;
   private final ScheduleId newScheduleId;

   public OperationScheduleEdit(OperationGraphController var1, OperationWrapperNode var2, ScheduleId var3, ScheduleId var4) {
      this.controller = var1;
      this.opId = var2.getOperationId();
      this.prevScheduleId = var3;
      this.newScheduleId = var4;
   }

   @Override
   public void redo() throws CannotRedoException {
      super.redo();
      OperationWrapper var1 = this.locateOperation();
      if (var1 != null) {
         var1.setScheduleId(this.newScheduleId);
      } else {
         alertNotRedoable();
      }
   }

   @Override
   public void undo() throws CannotUndoException {
      super.undo();
      OperationWrapper var1 = this.locateOperation();
      if (var1 != null) {
         var1.setScheduleId(this.prevScheduleId);
      } else {
         alertNotUndoable();
      }
   }

   @Override
   public String getPresentationName() {
      return GraphResources.getString("SelectedScheduleEdit");
   }

   private OperationWrapper locateOperation() {
      for (OperationWrapperNode var2 : this.controller.getGraph().getModel().getOperationNodes()) {
         if (this.opId.equals(var2.getDataObject().getActivity().getID())) {
            return var2.getDataObject();
         }
      }

      return null;
   }

   private static void alertNotUndoable() {
      Alert.error("Cannot undo this edit.", "Not Undoable");
   }

   private static void alertNotRedoable() {
      Alert.error("Cannot redo this edit.", "Not Redoable");
   }
}
