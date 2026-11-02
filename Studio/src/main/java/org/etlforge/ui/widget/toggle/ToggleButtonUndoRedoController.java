package org.etlforge.ui.widget.toggle;

import com.google.common.base.Preconditions;
import javax.swing.Action;
import javax.swing.JToggleButton;
import org.etlforge.ui.action.ActionContainer;
import org.etlforge.ui.action.ActionDecorator;
import org.etlforge.ui.undo.UndoRedoController;
import org.etlforge.ui.undo.UndoRedoControllerBase;

public final class ToggleButtonUndoRedoController implements UndoRedoController {
   private final UndoRedoControllerBase delegate;

   public ToggleButtonUndoRedoController(JToggleButton var1) {
      Preconditions.checkNotNull(var1, "toggle");
      this.delegate = new UndoRedoControllerBase();
      this.delegate.addEditSender(new ToggleButtonEditSender(var1));
      this.delegate.installKeyBindings(var1, 0);
   }

   @Override
   public boolean canRedo() {
      return this.delegate.canRedo();
   }

   @Override
   public boolean canUndo() {
      return this.delegate.canRedo();
   }

   @Override
   public Action getRedoAction() {
      return this.delegate.getRedoAction();
   }

   @Override
   public Action getUndoAction() {
      return this.delegate.getUndoAction();
   }

   @Override
   public void alwaysConsumeKeyboardTriggers() {
      this.delegate.alwaysConsumeKeyboardTriggers();
   }

   public void decorateRedoAction(ActionDecorator var1) {
      this.delegate.decorateRedoAction(var1);
   }

   public void decorateUndoAction(ActionDecorator var1) {
      this.delegate.decorateUndoAction(var1);
   }

   @Override
   public void dispose() {
      this.delegate.dispose();
   }

   @Override
   public void redo() {
      this.delegate.redo();
   }

   @Override
   public void undo() {
      this.delegate.undo();
   }

   public void addTo(ActionContainer var1) {
      this.delegate.addTo(var1);
   }

   public void removeFrom(ActionContainer var1) {
      this.delegate.removeFrom(var1);
   }
}
