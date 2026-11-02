package org.etlforge.ui.input;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import org.etlforge.ui.event.UndoableEditListeners;

public abstract class AbstractChangeSource implements ChangeSource {
   private final InputChangeListeners changeListeners = new InputChangeListeners();
   private final UndoableEditListeners undoRedoListeners = new UndoableEditListeners();
   private boolean sendUndoRedoEvents;
   private boolean sendChangeEvents;

   @Override
   public final void addInputChangeListener(InputChangeListener var1) {
      this.changeListeners.add(var1);
   }

   @Override
   public final void removeInputChangeListener(InputChangeListener var1) {
      this.changeListeners.remove(var1);
   }

   @Override
   public final void addUndoableEditListener(UndoableEditListener var1) {
      this.undoRedoListeners.add(var1);
   }

   @Override
   public final void removeUndoableEditListener(UndoableEditListener var1) {
      this.undoRedoListeners.remove(var1);
   }

   @Override
   public final boolean isSendingInputChangeEvents() {
      return this.sendChangeEvents;
   }

   @Override
   public final void setSendInputChangeEvents(boolean var1) {
      this.sendChangeEvents = var1;
   }

   @Override
   public final boolean isSendingUndoEvents() {
      return this.sendUndoRedoEvents;
   }

   @Override
   public final void setSendUndoEvents(boolean var1) {
      this.sendUndoRedoEvents = var1;
   }

   protected final void fireChangeEvent(InputChangeEvent var1) {
      this.changeListeners.call().inputChanged(var1);
   }

   protected final void fireUndoableEditHappened(UndoableEditEvent var1) {
      this.undoRedoListeners.call().undoableEditHappened(var1);
   }
}
