package org.etlforge.ui.input.undo;

import java.util.Date;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.etlforge.ui.input.DateInputField;

public class DateUndoableEdit extends AbstractUndoableEdit {
   private final DateInputField field;
   private final Date oldDate;
   private final Date newDate;

   public DateUndoableEdit(DateInputField var1, Date var2, Date var3) {
      this.field = var1;
      this.oldDate = var2;
      this.newDate = var3;
   }

   @Override
   public String getPresentationName() {
      return this.field.getUndoRedoPresentation();
   }

   @Override
   public void redo() throws CannotRedoException {
      super.redo();
      this.setDate(this.newDate);
   }

   @Override
   public void undo() throws CannotUndoException {
      super.undo();
      this.setDate(this.oldDate);
   }

   private void setDate(final Date var1) {
      this.field.updateSilently(new Runnable() {
         @Override
         public void run() {
            DateUndoableEdit.this.field.setDate(var1);
         }
      });
   }
}
