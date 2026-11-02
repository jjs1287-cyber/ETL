package org.etlforge.ui.undo;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoableEdit;
import org.etlforge.ui.event.UndoableEditListeners;

public abstract class AbstractUndoableEditSender implements UndoableEditSender {
   private final UndoableEditListeners undoListeners = new UndoableEditListeners();
   private boolean disposed;
   private UndoableEdit lastEdit;

   protected AbstractUndoableEditSender() {
   }

   @Override
   public void dispose() {
      this.undoListeners.removeAll();
      this.disposed = true;
   }

   protected final boolean isDisposed() {
      return this.disposed;
   }

   public void sendEdit() {
      if (this.lastEdit != null) {
         this.fireUndoableEditHappened(this.lastEdit);
         this.lastEdit = null;
      }
   }

   @Override
   public final void addUndoableEditListener(UndoableEditListener var1) {
      this.undoListeners.add(var1);
   }

   @Override
   public final void removeUndoableEditListener(UndoableEditListener var1) {
      this.undoListeners.remove(var1);
   }

   protected void fireUndoableEditHappened(UndoableEdit var1) {
      this.undoListeners.call().undoableEditHappened(new UndoableEditEvent(this, var1));
   }

   protected abstract static class AbstractEditProducer implements PreUpdateEditJob, PostUpdateEditJob {
      private final AbstractUndoableEditSender editSender;
      private boolean blocked;

      protected AbstractEditProducer(AbstractUndoableEditSender var1) {
         this.editSender = var1;
      }

      protected void handleEdit(UndoableEdit var1) {
         this.editSender.lastEdit = var1;
         this.editSender.sendEdit();
      }

      @Override
      public void beforeUpdate() {
         this.blocked = true;
      }

      @Override
      public void afterUpdate() {
         this.blocked = false;
      }

      protected final boolean isBlocked() {
         return this.blocked;
      }
   }
}
