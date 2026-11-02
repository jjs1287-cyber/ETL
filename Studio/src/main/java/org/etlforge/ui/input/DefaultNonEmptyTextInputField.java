package org.etlforge.ui.input;

import javax.swing.JLabel;
import javax.swing.text.JTextComponent;
import org.apache.commons.lang.StringUtils;

public class DefaultNonEmptyTextInputField extends TextInputField2 {
   public DefaultNonEmptyTextInputField(String var1, JTextComponent var2) {
      super(var1, var2);
   }

   public DefaultNonEmptyTextInputField(String var1, JTextComponent var2, String var3) {
      super(var1, var2, var3);
   }

   public DefaultNonEmptyTextInputField(String var1, JTextComponent var2, JLabel var3) {
      super(var1, var2, var3);
   }

   @Override
   public boolean isInputValid(String var1) {
      return StringUtils.isNotBlank(var1);
   }

   @Override
   public String getInvalidMessage(String var1) {
      return PackageResources.VALUE_CANNOT_BE_EMPTY;
   }
}
