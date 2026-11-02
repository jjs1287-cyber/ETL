package org.etlforge.ui.graph.layout;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

public class GraphLayoutEdit extends AbstractUndoableEdit {
   private final GraphLayoutMemento oldState;
   private final GraphLayoutMemento newState;

   public GraphLayoutEdit(GraphLayoutMemento var1, GraphLayoutMemento var2) {
      if (var1 == null) {
         throw new NullPointerException("oldState");
      }

      if (var2 == null) {
         throw new NullPointerException("newState");
      }

      this.oldState = var1;
      this.newState = var2;
   }

   @Override
   public void redo() throws CannotRedoException {
      super.redo();
      this.newState.restore();
   }

   @Override
   public void undo() throws CannotUndoException {
      super.undo();
      this.oldState.restore();
   }

   @Override
   public String getPresentationName() {
      return "Graph Layout Change";
   }

   public GraphLayoutMemento getOldState() {
      return this.oldState;
   }

   public GraphLayoutMemento getNewState() {
      return this.newState;
   }
}
