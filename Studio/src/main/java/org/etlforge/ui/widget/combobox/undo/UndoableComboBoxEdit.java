package org.etlforge.ui.widget.combobox.undo;

import javax.swing.JComboBox;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.etlforge.ui.undo.PostUpdateEditJob;
import org.etlforge.ui.undo.PreUpdateEditJob;

public class UndoableComboBoxEdit extends AbstractUndoableEdit {
   private JComboBox combo;
   private Object newValue;
   private Object previousValue;
   private PreUpdateEditJob preUpdateJob;
   private PostUpdateEditJob postUpdateJob;

   public UndoableComboBoxEdit(JComboBox var1, Object var2, Object var3, PreUpdateEditJob var4, PostUpdateEditJob var5) {
      this.combo = var1;
      this.newValue = var2;
      this.previousValue = var3;
      this.preUpdateJob = var4;
      this.postUpdateJob = var5;
   }

   @Override
   public boolean canRedo() {
      return this.canBeAppliedToCombo(this.newValue);
   }

   @Override
   public boolean canUndo() {
      return this.canBeAppliedToCombo(this.previousValue);
   }

   @Override
   public void redo() throws CannotRedoException {
      super.redo();
      this.undoOrRedo(this.newValue);
   }

   @Override
   public void undo() throws CannotUndoException {
      super.undo();
      this.undoOrRedo(this.previousValue);
   }

   private void undoOrRedo(Object var1) {
      if (this.preUpdateJob != null) {
         this.preUpdateJob.beforeUpdate();
      }

      this.combo.setSelectedItem(var1);
      if (this.postUpdateJob != null) {
         this.postUpdateJob.afterUpdate();
      }
   }

   private boolean canBeAppliedToCombo(Object var1) {
      if (var1 == null) {
         return true;
      }

      int var2 = 0;

      for (int var3 = this.combo.getItemCount(); var2 < var3; var2++) {
         if (var1 == this.combo.getItemAt(var2)) {
            return true;
         }
      }

      return false;
   }
}
