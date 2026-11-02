package org.etlforge.ui.input;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.label.KongaLabel;

public final class CheckBoxInputField extends ToggleButtonInputField {
   public CheckBoxInputField(String var1, String var2) {
      this(var1, new KongaCheckBox(), var2);
   }

   public CheckBoxInputField(String var1, JCheckBox var2) {
      this(var1, var2, (JLabel)null);
   }

   public CheckBoxInputField(String var1, JCheckBox var2, String var3) {
      this(var1, var2, KongaLabel.withMnemonic(var3));
   }

   public CheckBoxInputField(String var1, JCheckBox var2, JLabel var3) {
      super(var1, var2, var3);
   }

   public JCheckBox getInputComponent() {
      return (JCheckBox)super.getInputComponent();
   }
}
