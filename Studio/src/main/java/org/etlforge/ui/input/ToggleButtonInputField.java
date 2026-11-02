package org.etlforge.ui.input;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import javax.swing.JLabel;
import javax.swing.JToggleButton;
import javax.swing.undo.UndoableEdit;
import org.etlforge.ui.input.undo.ToggleUndoableEdit;
import org.etlforge.ui.widget.WidgetUtils;

public abstract class ToggleButtonInputField extends InputField {
   private final ToggleButtonInputField.EditListener editListener;

   public ToggleButtonInputField(String var1, JToggleButton var2, JLabel var3) {
      super(var1, var2, var3);
      this.editListener = new ToggleButtonInputField.EditListener(var2.isSelected());
      var2.addActionListener(this.editListener);
   }

   @Override
   public void dispose() {
      this.getInputComponent().removeActionListener(this.editListener);
   }

   public boolean isSelected() {
      return this.getInputComponent().isSelected();
   }

   public void setSelected(boolean var1) {
      this.getInputComponent().setSelected(var1);
      this.editListener.previousValue = var1;
   }

   public JToggleButton getInputComponent() {
      return (JToggleButton)super.getInputComponent();
   }

   @Override
   public void makeReadOnly() {
      WidgetUtils.makeReadOnly(this.getInputComponent());
   }

   protected UndoableEdit createUndoableEdit() {
      return new ToggleUndoableEdit(this, this.isSelected());
   }

   public final void addItemListener(ItemListener var1) {
      this.getInputComponent().addItemListener(var1);
   }

   public final void removeItemListener(ItemListener var1) {
      this.getInputComponent().removeItemListener(var1);
   }

   private class EditListener implements ActionListener {
      private boolean previousValue;

      public EditListener(boolean var2) {
         this.previousValue = var2;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         boolean var2 = ToggleButtonInputField.this.isSelected();
         if (var2 != this.previousValue) {
            this.previousValue = var2;
            if (ToggleButtonInputField.this.isSendingInputChangeEvents()) {
               ToggleButtonInputField.this.fireInputChangeEvent(!var2, var2);
            }

            if (ToggleButtonInputField.this.isSendingUndoEvents()) {
               ToggleButtonInputField.this.fireUndoableEditHappened(ToggleButtonInputField.this.createUndoableEdit());
            }
         }
      }
   }
}
