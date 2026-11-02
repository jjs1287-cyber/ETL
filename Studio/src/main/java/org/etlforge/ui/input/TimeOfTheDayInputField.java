package org.etlforge.ui.input;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.etlforge.ui.util.time.TimeOfTheDayControl;

public class TimeOfTheDayInputField extends InputField implements PropertyChangeListener {
   private final Date originalDate;

   public TimeOfTheDayInputField(String var1, TimeOfTheDayControl var2) {
      super(var1, var2, null);
      var2.addPropertyChangeListener("Displayed_Time", this);
      this.originalDate = var2.getDate();
   }

   public TimeOfTheDayControl getInputComponent() {
      return (TimeOfTheDayControl)super.getInputComponent();
   }

   @Override
   public void dispose() {
      this.getInputComponent().removePropertyChangeListener("Displayed_Time", this);
   }

   @Override
   public void makeReadOnly() {
      this.getInputComponent().setEditable(false);
   }

   @Override
   public void propertyChange(PropertyChangeEvent var1) {
      if (this.isSendingInputChangeEvents()) {
         this.fireInputChangeEvent(var1.getOldValue(), var1.getNewValue());
      }

      if (this.isSendingUndoEvents()) {
         Date var2 = (Date)var1.getOldValue();
         if (var2 == null) {
            var2 = this.originalDate;
         }

         this.fireUndoableEditHappened(new TimeOfTheDayInputField.TimeOfTheDayEdit(this, var2, (Date)var1.getNewValue()));
      }
   }

   private static class TimeOfTheDayEdit extends AbstractUndoableEdit {
      private final TimeOfTheDayInputField sourceField;
      private final Date previousDate;
      private final Date newDate;

      public TimeOfTheDayEdit(TimeOfTheDayInputField var1, Date var2, Date var3) {
         this.sourceField = var1;
         this.previousDate = var2;
         this.newDate = var3;
      }

      @Override
      public void redo() throws CannotRedoException {
         super.redo();
         this.setDate(this.newDate);
      }

      @Override
      public void undo() throws CannotUndoException {
         super.undo();
         this.setDate(this.previousDate);
      }

      @Override
      public String getPresentationName() {
         return this.sourceField.getUndoRedoPresentation();
      }

      private void setDate(final Date var1) {
         this.sourceField.updateSilently(new Runnable() {
            @Override
            public void run() {
               TimeOfTheDayEdit.this.sourceField.getInputComponent().setDate(var1);
            }
         });
      }
   }
}
