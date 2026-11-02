package org.etlforge.ui.input;

import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JLabel;
import javax.swing.text.JTextComponent;

public class UrlInputField extends TextBasedInputField {
   public UrlInputField(String var1, JTextComponent var2, JLabel var3) {
      super(var1, var2, var3);
   }

   public URL getValue() {
      try {
         return new URL(this.getText());
      } catch (MalformedURLException var2) {
         return null;
      }
   }

   @Override
   public boolean isInputValid(String var1) {
      try {
         new URL(var1);
         return true;
      } catch (MalformedURLException var3) {
         this.setErrorMessage(var3.getMessage());
         return false;
      }
   }
}
