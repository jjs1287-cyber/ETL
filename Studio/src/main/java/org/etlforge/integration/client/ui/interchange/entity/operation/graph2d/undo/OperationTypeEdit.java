package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.undo;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.resources.GraphResources;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.operation.OperationType;
import org.etlforge.system.callback.Receiver;

public class OperationTypeEdit extends AbstractUndoableEdit {
   private final OperationGraphController controller;
   private final Receiver<OperationTypeEdit.Data> callback;
   private final OperationId opId;
   private final OperationType previousType;
   private final OperationType newType;

   public OperationTypeEdit(OperationGraphController var1, Receiver<OperationTypeEdit.Data> var2, OperationId var3, OperationType var4, OperationType var5) {
      this.controller = var1;
      this.callback = var2;
      this.opId = var3;
      this.previousType = var4;
      this.newType = var5;
   }

   @Override
   public void redo() throws CannotRedoException {
      super.redo();
      OperationWrapperNode var1 = this.locateOperation();
      if (var1 != null) {
         this.callback.handle(new OperationTypeEdit.Data(var1, this.newType));
      }
   }

   @Override
   public void undo() throws CannotUndoException {
      super.undo();
      OperationWrapperNode var1 = this.locateOperation();
      if (var1 != null) {
         this.callback.handle(new OperationTypeEdit.Data(var1, this.previousType));
      }
   }

   @Override
   public String getPresentationName() {
      return GraphResources.getString("OpTypeEdit");
   }

   private OperationWrapperNode locateOperation() {
      for (OperationWrapperNode var2 : this.controller.getGraph().getModel().getOperationNodes()) {
         if (this.opId.equals(var2.getDataObject().getActivity().getID())) {
            return var2;
         }
      }

      return null;
   }

   public static final class Data {
      public final OperationWrapperNode opNode;
      public final OperationType type;

      private Data(OperationWrapperNode var1, OperationType var2) {
         this.opNode = var1;
         this.type = var2;
      }
   }
}
