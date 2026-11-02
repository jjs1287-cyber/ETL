package org.etlforge.ui.widget.combobox.undo;

import javax.swing.Action;
import javax.swing.JComboBox;
import org.etlforge.ui.action.ActionContainer;
import org.etlforge.ui.action.ActionDecorator;
import org.etlforge.ui.undo.UndoRedoController;
import org.etlforge.ui.undo.UndoRedoControllerBase;

public final class ComboBoxUndoRedoController implements UndoRedoController {
   private UndoRedoControllerBase delegate = new UndoRedoControllerBase();

   public ComboBoxUndoRedoController(JComboBox var1) {
      this.delegate.addEditSender(new ComboBoxEditSender(var1));
      this.delegate.installKeyBindings(var1, 1);
   }

   @Override
   public boolean canRedo() {
      return this.delegate.canRedo();
   }

   @Override
   public boolean canUndo() {
      return this.delegate.canUndo();
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

   public void addTo(ActionContainer var1) {
      this.delegate.addTo(var1);
   }

   public void removeFrom(ActionContainer var1) {
      this.delegate.removeFrom(var1);
   }

   public void decorateUndoAction(ActionDecorator var1) {
      this.delegate.decorateUndoAction(var1);
   }

   public void decorateRedoAction(ActionDecorator var1) {
      this.delegate.decorateRedoAction(var1);
   }
}
