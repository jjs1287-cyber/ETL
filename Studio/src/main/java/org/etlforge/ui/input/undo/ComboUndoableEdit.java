package org.etlforge.ui.input.undo;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.etlforge.ui.input.ComboBoxInputField;

public class ComboUndoableEdit extends AbstractUndoableEdit {
   private final ComboBoxInputField field;
   private Object previousValue;
   private Object newValue;

   public ComboUndoableEdit(ComboBoxInputField var1, Object var2, Object var3) {
      this.field = var1;
      this.previousValue = var2;
      this.newValue = var3;
   }

   @Override
   public void redo() throws CannotRedoException {
      super.redo();
      this.setSelection(this.newValue);
   }

   @Override
   public void undo() throws CannotUndoException {
      super.undo();
      this.setSelection(this.previousValue);
   }

   private void setSelection(Object var1) {
      this.field.setSelectedItemSilently(var1);
   }

   @Override
   public String getPresentationName() {
      return this.field.getUndoRedoPresentation();
   }
}
