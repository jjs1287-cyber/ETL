package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.undo;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.etlforge.application.ui.Alert;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.operation.flow.OperationWrapper;

public class SiblingOperationEdit extends AbstractUndoableEdit {
   private final OperationGraphController controller;
   private final OperationId opId;
   private final List<OperationId> oldSiblings;
   private final List<OperationId> newSiblings;

   public SiblingOperationEdit(OperationGraphController var1, OperationWrapper var2, List<OperationId> var3) {
      this.controller = var1;
      this.opId = var2.getActivity().getID();
      this.oldSiblings = var2.getSiblingIds();
      this.newSiblings = var3 != null ? Lists.newArrayList(var3) : new ArrayList<>();
   }

   public static SiblingOperationEdit siblingRemoved(OperationGraphController var0, OperationWrapper var1, Operation var2) {
      List var3 = var1.getSiblingIds();
      var3.remove(var2.getID());
      return new SiblingOperationEdit(var0, var1, var3);
   }

   @Override
   public void redo() throws CannotRedoException {
      super.redo();
      this.setSiblings(this.newSiblings);
   }

   @Override
   public void undo() throws CannotUndoException {
      super.undo();
      this.setSiblings(this.oldSiblings);
   }

   private void setSiblings(List<OperationId> var1) {
      OperationWrapper var2 = this.locateOperation();
      if (var2 != null) {
         if (this.controller.isChangeAllowed(var2)) {
            var2.setSiblingIds(var1);
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
