package org.etlforge.ui.input;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoableEdit;
import org.etlforge.ui.event.UndoableEditListeners;

public final class ChangeSourceManager implements ChangeSource {
   private final InputChangeListeners changeListeners = new InputChangeListeners();
   private final UndoableEditListeners undoListeners = new UndoableEditListeners();
   private boolean isSendingInputChangeEvents;
   private boolean isSendingUndoEvent;

   @Override
   public boolean isSendingInputChangeEvents() {
      return this.isSendingInputChangeEvents;
   }

   @Override
   public void setSendInputChangeEvents(boolean var1) {
      this.isSendingInputChangeEvents = var1;
   }

   @Override
   public void addInputChangeListener(InputChangeListener var1) {
      this.changeListeners.add(var1);
   }

   @Override
   public void removeInputChangeListener(InputChangeListener var1) {
      this.changeListeners.remove(var1);
   }

   @Override
   public boolean isSendingUndoEvents() {
      return this.isSendingUndoEvent;
   }

   @Override
   public void setSendUndoEvents(boolean var1) {
      this.isSendingUndoEvent = var1;
   }

   @Override
   public void addUndoableEditListener(UndoableEditListener var1) {
      this.undoListeners.add(var1);
   }

   @Override
   public void removeUndoableEditListener(UndoableEditListener var1) {
      this.undoListeners.remove(var1);
   }

   public void sendUndoableEdit(Object var1, UndoableEdit var2) {
      if (this.isSendingUndoEvent && !this.undoListeners.isEmpty()) {
         this.sendUndoableEditImpl(new UndoableEditEvent(var1, var2));
      }
   }

   public void sendUndoableEdit(UndoableEditEvent var1) {
      if (this.isSendingUndoEvent && !this.undoListeners.isEmpty()) {
         this.sendUndoableEditImpl(var1);
      }
   }

   private void sendUndoableEditImpl(UndoableEditEvent var1) {
      this.undoListeners.call().undoableEditHappened(var1);
   }

   public void sendChangeEvent(InputChangeEvent var1) {
      if (this.isSendingInputChangeEvents) {
         this.changeListeners.call().inputChanged(var1);
      }
   }
}
