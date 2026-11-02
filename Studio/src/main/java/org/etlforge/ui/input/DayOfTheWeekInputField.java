package org.etlforge.ui.input;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.etlforge.ui.util.time.DayOfTheWeekCheckBoxPanel;
import org.etlforge.util.time.DayOfTheWeek;

public class DayOfTheWeekInputField extends InputField implements PropertyChangeListener {
   public DayOfTheWeekInputField(String var1, DayOfTheWeekCheckBoxPanel var2) {
      super(var1, var2, null);
      var2.addPropertyChangeListener("SelectionChanged", this);
   }

   public DayOfTheWeekCheckBoxPanel getInputComponent() {
      return (DayOfTheWeekCheckBoxPanel)super.getInputComponent();
   }

   @Override
   public void makeReadOnly() {
      this.getInputComponent().makeReadOnly();
   }

   @Override
   public void propertyChange(PropertyChangeEvent var1) {
      if (this.isSendingInputChangeEvents()) {
         this.fireInputChangeEvent(var1.getOldValue(), var1.getNewValue());
      }

      if (this.isSendingUndoEvents()) {
         this.fireUndoableEditHappened(
            new DayOfTheWeekInputField.DaysSelectedEdit(this, (DayOfTheWeek[])var1.getOldValue(), (DayOfTheWeek[])var1.getNewValue())
         );
      }
   }

   @Override
   public void dispose() {
      this.getInputComponent().removePropertyChangeListener("SelectionChanged", this);
   }

   private static class DaysSelectedEdit extends AbstractUndoableEdit {
      private DayOfTheWeekInputField sourceField;
      private DayOfTheWeek[] previousValue;
      private DayOfTheWeek[] newValue;

      public DaysSelectedEdit(DayOfTheWeekInputField var1, DayOfTheWeek[] var2, DayOfTheWeek[] var3) {
         this.sourceField = var1;
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

      private void setSelection(final DayOfTheWeek[] var1) {
         this.sourceField.updateSilently(new Runnable() {
            @Override
            public void run() {
               DaysSelectedEdit.this.sourceField.getInputComponent().setSelection(var1);
            }
         });
      }
   }
}
