package org.etlforge.ui.input.undo;

import com.google.common.base.Preconditions;
import javax.swing.ButtonModel;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

public class ButtonGroupUndoableEdit extends AbstractUndoableEdit {
   private final UndoableButtonGroup buttonGroup;
   private final ButtonModel previousSelection;
   private final ButtonModel newSelection;
   private String presentationName;

   public ButtonGroupUndoableEdit(UndoableButtonGroup var1, ButtonModel var2, ButtonModel var3) {
      Preconditions.checkNotNull(var1, "group");
      this.buttonGroup = var1;
      this.previousSelection = var2;
      this.newSelection = var3;
      this.presentationName = "";
   }

   @Override
   public void redo() throws CannotRedoException {
      super.redo();
      this.buttonGroup.setSendUndoEvents(false);
      this.buttonGroup.setSelected(this.newSelection, true);
      this.buttonGroup.setSendUndoEvents(true);
   }

   @Override
   public void undo() throws CannotUndoException {
      super.undo();
      this.buttonGroup.setSendUndoEvents(false);
      this.buttonGroup.setSelected(this.previousSelection, true);
      this.buttonGroup.setSendUndoEvents(true);
   }

   public void setPresentationName(String var1) {
      this.presentationName = var1;
   }

   @Override
   public String getPresentationName() {
      return this.presentationName;
   }
}
