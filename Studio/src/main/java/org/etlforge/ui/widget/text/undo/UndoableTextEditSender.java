package org.etlforge.ui.widget.text.undo;

import com.google.common.base.Preconditions;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.JTextComponent;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoableEdit;
import org.etlforge.ui.undo.AbstractUndoableEditSender;
import org.etlforge.ui.undo.UndoableEditSender;

public class UndoableTextEditSender extends AbstractUndoableEditSender implements UndoableEditSender, UndoableEditListener {
   private final JTextComponent textComponent;
   private CompoundEdit compountEdit;
   private UndoableTextEditSender.FocusHandler focusHandler;

   public UndoableTextEditSender(JTextComponent var1) {
      this.textComponent = Preconditions.checkNotNull(var1, "c");
      this.installListeners();
   }

   @Override
   public void dispose() {
      if (!this.isDisposed()) {
         this.focusHandler.uninstall(this.textComponent);
         this.textComponent.getDocument().removeUndoableEditListener(this);
         super.dispose();
      }
   }

   private void installListeners() {
      this.focusHandler = new UndoableTextEditSender.FocusHandler();
      this.focusHandler.install(this.textComponent);
      this.textComponent.getDocument().addUndoableEditListener(this);
   }

   private void updateEdit(UndoableEdit var1) {
      if (this.compountEdit == null) {
         this.compountEdit = new CompoundEdit();
      }

      this.compountEdit.addEdit(var1);
   }

   @Override
   public synchronized void sendEdit() {
      if (this.compountEdit != null) {
         this.compountEdit.end();
         this.fireUndoableEditHappened(this.compountEdit);
         this.compountEdit = null;
      }
   }

   @Override
   public void undoableEditHappened(UndoableEditEvent var1) {
      if (var1.getEdit().isSignificant()) {
         synchronized (this) {
            UndoableEditTimer.getTimer().undoableEditHappened(var1);
            this.updateEdit(var1.getEdit());
         }
      }
   }

   private class FocusHandler implements FocusListener {
      private FocusHandler() {
      }

      @Override
      public void focusGained(FocusEvent var1) {
         UndoableEditTimer.getTimer().registerSender(UndoableTextEditSender.this);
      }

      @Override
      public void focusLost(FocusEvent var1) {
         UndoableEditTimer.getTimer().removeSender(UndoableTextEditSender.this);
      }

      public void install(JTextComponent var1) {
         var1.addFocusListener(this);
         if (var1.hasFocus()) {
            this.focusGained(null);
         }
      }

      public void uninstall(JTextComponent var1) {
         var1.removeFocusListener(this);
      }
   }
}
