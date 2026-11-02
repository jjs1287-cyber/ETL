package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.undo;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.resources.GraphResources;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;

public class PipelineActivityInsertedEdit extends PipelineActivityInsertedRemovedEdit {
   public PipelineActivityInsertedEdit(OperationGraphController var1, PipelineActivity var2, OperationWrapperNode var3) {
      super(var1, var2, var3);
   }

   @Override
   public void redo() throws CannotRedoException {
      super.redo();
      if (!this.insertActivity()) {
         alertNotRedoable();
      }
   }

   @Override
   public void undo() throws CannotUndoException {
      super.undo();
      if (!this.removeActivity()) {
         alertNotUndoable();
      }
   }

   @Override
   public String getPresentationName() {
      return GraphResources.format("ActivityInsertedEdit", this.getActivityType());
   }
}
