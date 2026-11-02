package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.undo;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.layout.NodeExpansionMemento;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.resources.GraphResources;

public class NodeExpansionEdit extends AbstractUndoableEdit {
   private final NodeExpansionMemento oldState;
   private final NodeExpansionMemento newState;

   public NodeExpansionEdit(NodeExpansionMemento var1, NodeExpansionMemento var2) {
      this.oldState = var1;
      this.newState = var2;
   }

   @Override
   public void redo() throws CannotRedoException {
      super.redo();
      this.setExpandedNodes(this.newState);
   }

   @Override
   public void undo() throws CannotUndoException {
      super.undo();
      this.setExpandedNodes(this.oldState);
   }

   @Override
   public String getPresentationName() {
      return GraphResources.getString("NodeExpansionEdit");
   }

   private void setExpandedNodes(NodeExpansionMemento var1) {
      var1.restore();
   }
}
