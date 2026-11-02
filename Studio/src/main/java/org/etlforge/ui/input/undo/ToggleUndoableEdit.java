package org.etlforge.ui.input.undo;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.etlforge.ui.input.ToggleButtonInputField;

public class ToggleUndoableEdit extends AbstractUndoableEdit {
   private final ToggleButtonInputField field;
   private boolean newValue;

   public ToggleUndoableEdit(ToggleButtonInputField var1, boolean var2) {
      this.field = var1;
      this.newValue = var2;
   }

   @Override
   public void redo() throws CannotRedoException {
      super.redo();
      this.setState(this.newValue);
   }

   @Override
   public void undo() throws CannotUndoException {
      super.undo();
      this.setState(!this.newValue);
   }

   private void setState(final boolean var1) {
      this.field.updateSilently(new Runnable() {
         @Override
         public void run() {
            if (var1) {
               ToggleUndoableEdit.this.field.getInputComponent().doClick();
            } else {
               ToggleUndoableEdit.this.field.setSelected(false);
            }
         }
      });
   }

   @Override
   public String getPresentationName() {
      return this.field.getUndoRedoPresentation();
   }
}
