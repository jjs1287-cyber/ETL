package org.etlforge.ui.input;

import javax.swing.JLabel;
import javax.swing.JRadioButton;

public class RadioButtonInputField extends ToggleButtonInputField {
   public RadioButtonInputField(String var1, JRadioButton var2) {
      this(var1, var2, null);
   }

   public RadioButtonInputField(String var1, JRadioButton var2, JLabel var3) {
      super(var1, var2, var3);
   }

   public JRadioButton getInputComponent() {
      return (JRadioButton)super.getInputComponent();
   }
}
