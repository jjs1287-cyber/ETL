package org.etlforge.ui.widget.toggle;

import javax.swing.JToggleButton;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.etlforge.ui.undo.PostUpdateEditJob;
import org.etlforge.ui.undo.PreUpdateEditJob;

public class ToggleButtonUndoableEdit extends AbstractUndoableEdit {
   private final JToggleButton toggle;
   private boolean value;
   private PreUpdateEditJob preUpdateJob;
   private PostUpdateEditJob postUpdateJob;

   public ToggleButtonUndoableEdit(JToggleButton var1) {
      this(var1, null, null);
   }

   public ToggleButtonUndoableEdit(JToggleButton var1, PreUpdateEditJob var2, PostUpdateEditJob var3) {
      this.toggle = var1;
      this.preUpdateJob = var2;
      this.postUpdateJob = var3;
      this.value = var1.isSelected();
   }

   @Override
   public void redo() throws CannotRedoException {
      super.redo();
      this.applyValue(this.value);
   }

   @Override
   public void undo() throws CannotUndoException {
      super.undo();
      this.applyValue(!this.value);
   }

   private void applyValue(boolean var1) {
      if (this.preUpdateJob != null) {
         this.preUpdateJob.beforeUpdate();
      }

      this.toggle.setSelected(var1);
      if (this.postUpdateJob != null) {
         this.postUpdateJob.afterUpdate();
      }
   }
}
