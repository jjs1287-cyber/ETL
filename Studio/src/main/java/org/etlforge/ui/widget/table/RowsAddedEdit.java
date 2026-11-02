package org.etlforge.ui.widget.table;

import java.util.List;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

final class RowsAddedEdit extends AbstractUndoableEdit {
   private final AbstractKongaTableModel model;
   private final int index;
   private final List<? extends TableRow> rows;

   public RowsAddedEdit(AbstractKongaTableModel var1, int var2, List<? extends TableRow> var3) {
      this.model = var1;
      this.index = var2;
      this.rows = var3;
   }

   @Override
   public void undo() throws CannotUndoException {
      super.undo();
      this.model.undoAddedRows(this.index, this.rows.size());
   }

   @Override
   public void redo() throws CannotRedoException {
      super.redo();
      this.model.redoAddedRows(this.index, this.rows);
   }
}
