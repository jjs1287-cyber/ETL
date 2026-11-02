package org.etlforge.ui.input.undo;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.etlforge.ui.input.SliderInputField;

public class SliderUndoableEdit extends AbstractUndoableEdit {
   private final SliderInputField inputField;
   private final int oldValue;
   private final int newValue;

   public SliderUndoableEdit(SliderInputField var1, int var2, int var3) {
      this.inputField = var1;
      this.oldValue = var2;
      this.newValue = var3;
   }

   @Override
   public void redo() throws CannotRedoException {
      super.redo();
      this.setValue(this.newValue);
   }

   @Override
   public void undo() throws CannotUndoException {
      super.undo();
      this.setValue(this.oldValue);
   }

   private void setValue(int var1) {
      this.inputField.setValueSilently(var1);
   }

   @Override
   public String getPresentationName() {
      return this.inputField.getUndoRedoPresentation();
   }
}
