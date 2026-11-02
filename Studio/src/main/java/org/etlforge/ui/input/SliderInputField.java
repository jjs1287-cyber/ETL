package org.etlforge.ui.input;

import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.etlforge.ui.input.undo.SliderUndoableEdit;

public class SliderInputField extends InputField {
   private final ChangeListener changeListener;

   public SliderInputField(String var1, JSlider var2, JLabel var3) {
      super(var1, var2, var3);
      this.changeListener = new SliderInputField.SliderListener(var2.getValue());
      var2.getModel().addChangeListener(this.changeListener);
   }

   public JSlider getInputComponent() {
      return (JSlider)super.getInputComponent();
   }

   public Integer getValue() {
      return this.getInputComponent().getValue();
   }

   public void setValue(int var1) {
      this.getInputComponent().setValue(var1);
   }

   public void setValueSilently(final int var1) {
      this.updateSilently(new Runnable() {
         @Override
         public void run() {
            SliderInputField.this.setValue(var1);
         }
      });
   }

   @Override
   public void makeReadOnly() {
      this.getInputComponent().setEnabled(false);
   }

   @Override
   public void dispose() {
      this.getInputComponent().getModel().removeChangeListener(this.changeListener);
   }

   private class SliderListener implements ChangeListener {
      private int previousValue;

      public SliderListener(int var2) {
         this.previousValue = var2;
      }

      @Override
      public void stateChanged(ChangeEvent var1) {
         if (!SliderInputField.this.getInputComponent().getValueIsAdjusting()) {
            int var2 = SliderInputField.this.getInputComponent().getValue();
            if (var2 != this.previousValue) {
               if (SliderInputField.this.isSendingInputChangeEvents()) {
                  SliderInputField.this.fireInputChangeEvent(this.previousValue, var2);
               }

               if (SliderInputField.this.isSendingUndoEvents()) {
                  SliderUndoableEdit var3 = new SliderUndoableEdit(SliderInputField.this, this.previousValue, var2);
                  SliderInputField.this.fireUndoableEditHappened(var3);
               }

               this.previousValue = var2;
            }
         }
      }
   }
}
