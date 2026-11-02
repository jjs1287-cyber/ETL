package org.etlforge.ui.input.undo;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoableEdit;
import org.etlforge.ui.input.ChangeSource;
import org.etlforge.ui.input.ChangeSourceManager;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.input.RadioButtonInputField;

public class UndoableButtonGroup extends ButtonGroup implements ChangeSource {
   private ButtonModel selectedModel;
   private final ChangeSourceManager changeManager = new ChangeSourceManager();
   private String undoRedoPresentationName = "";

   public void add(AbstractButton var1, boolean var2) {
      super.add(var1);
      if (var2) {
         this.selectedModel = var1.getModel();
      }
   }

   @Override
   public void setSelected(ButtonModel var1, boolean var2) {
      super.setSelected(var1, var2);
      if (var2 && var1 != this.selectedModel) {
         if (this.isSendingInputChangeEvents()) {
            this.changeManager.sendChangeEvent(new InputChangeEvent(this, ""));
         }

         if (this.isSendingUndoEvents()) {
            this.changeManager.sendUndoableEdit(this, this.createUndoableEdit(this.selectedModel, var1));
         }

         this.selectedModel = this.getSelection();
      }
   }

   @Override
   public final void setSendInputChangeEvents(boolean var1) {
      this.changeManager.setSendInputChangeEvents(var1);
   }

   @Override
   public final boolean isSendingInputChangeEvents() {
      return this.changeManager.isSendingInputChangeEvents();
   }

   @Override
   public final void setSendUndoEvents(boolean var1) {
      this.changeManager.setSendUndoEvents(var1);
   }

   @Override
   public final boolean isSendingUndoEvents() {
      return this.changeManager.isSendingUndoEvents();
   }

   @Override
   public void addInputChangeListener(InputChangeListener var1) {
      this.changeManager.addInputChangeListener(var1);
   }

   @Override
   public void removeInputChangeListener(InputChangeListener var1) {
      this.changeManager.removeInputChangeListener(var1);
   }

   @Override
   public void addUndoableEditListener(UndoableEditListener var1) {
      this.changeManager.addUndoableEditListener(var1);
   }

   @Override
   public void removeUndoableEditListener(UndoableEditListener var1) {
      this.changeManager.removeUndoableEditListener(var1);
   }

   public final RadioButtonInputField createInputField(JRadioButton var1, String var2, JLabel var3) {
      return new RadioButtonInputField(var2, var1, var3) {
         @Override
         public boolean isSendingUndoEvents() {
            return false;
         }
      };
   }

   public void setUndoRedoPresentationName(String var1) {
      this.undoRedoPresentationName = var1;
   }

   public String getUndoRedoPresentationName() {
      return this.undoRedoPresentationName;
   }

   protected UndoableEdit createUndoableEdit(ButtonModel var1, ButtonModel var2) {
      ButtonGroupUndoableEdit var3 = new ButtonGroupUndoableEdit(this, var1, var2);
      var3.setPresentationName(this.undoRedoPresentationName);
      return var3;
   }
}
