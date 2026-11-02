package org.etlforge.ui.input;

import javax.swing.JLabel;
import javax.swing.JPasswordField;

public class PasswordInputField extends TextBasedInputField {
   public PasswordInputField(String var1, JPasswordField var2, JLabel var3) {
      super(var1, var2, var3);
   }

   public JPasswordField getInputComponent() {
      return (JPasswordField)super.getInputComponent();
   }

   public final char[] getValue() {
      return this.getInputComponent().getPassword();
   }

   public final void setValue(char[] var1) {
      this.getInputComponent().setText(new String(var1));
   }

   public final char[] getPassword() {
      return this.getInputComponent().getPassword();
   }

   public final void setPassword(String var1) {
      this.getInputComponent().setText(var1);
   }

   @Override
   public boolean isInputValid(String var1) {
      return true;
   }
}
