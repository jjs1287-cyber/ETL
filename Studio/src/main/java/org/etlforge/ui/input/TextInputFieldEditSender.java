package org.etlforge.ui.input;

import javax.swing.event.UndoableEditEvent;
import javax.swing.text.JTextComponent;
import javax.swing.undo.UndoableEdit;
import org.etlforge.ui.widget.text.undo.UndoableTextEditSender;

public class TextInputFieldEditSender extends UndoableTextEditSender {
   private InputField inputField;
   private boolean blocked;

   TextInputFieldEditSender(InputField var1) {
      super((JTextComponent)var1.getInputComponent());
      this.inputField = var1;
   }

   @Override
   public void dispose() {
      super.dispose();
      this.inputField = null;
   }

   @Override
   public void undoableEditHappened(UndoableEditEvent var1) {
      if (this.inputField.isSendingUndoEvents() && !this.blocked) {
         InputFieldUndoableEdit var2 = new InputFieldUndoableEdit(this.inputField, var1.getEdit());
         super.undoableEditHappened(new UndoableEditEvent(var1.getSource(), var2));
      }
   }

   @Override
   protected void fireUndoableEditHappened(UndoableEdit var1) {
      if (!this.isDisposed()) {
         this.inputField.fireUndoableEditHappened(var1);
      }
   }

   public void setBlocked(boolean var1) {
      this.blocked = var1;
   }
}
