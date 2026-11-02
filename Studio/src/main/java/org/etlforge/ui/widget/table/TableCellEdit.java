package org.etlforge.ui.widget.table;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

class TableCellEdit extends AbstractUndoableEdit {
   private final AbstractKongaTableModel model;
   private final int rowIndex;
   private final int columnIndex;
   private final Object oldValue;
   private final Object newValue;

   public TableCellEdit(AbstractKongaTableModel var1, int var2, int var3, Object var4, Object var5) {
      this.model = var1;
      this.rowIndex = var2;
      this.columnIndex = var3;
      this.oldValue = var4;
      this.newValue = var5;
   }

   @Override
   public void undo() throws CannotUndoException {
      super.undo();
      this.model.undoOrRedoCellEdit(this.rowIndex, this.columnIndex, this.oldValue);
   }

   @Override
   public void redo() throws CannotRedoException {
      super.redo();
      this.model.undoOrRedoCellEdit(this.rowIndex, this.columnIndex, this.newValue);
   }
}
