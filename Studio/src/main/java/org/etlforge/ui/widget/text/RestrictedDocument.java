package org.etlforge.ui.widget.text;

import java.awt.Toolkit;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class RestrictedDocument extends PlainDocument {
   private String m_allowedCharacters;

   public RestrictedDocument(String var1) {
      this.m_allowedCharacters = var1;
   }

   public static RestrictedDocument digitsOnly() {
      return new RestrictedDocument("0123456789");
   }

   public static RestrictedDocument hexNumbersOnly() {
      return new RestrictedDocument("0123456789ABCDEFXabcdefx");
   }

   @Override
   public void insertString(int var1, String var2, AttributeSet var3) throws BadLocationException {
      for (int var4 = var2.length() - 1; var4 >= 0; var4--) {
         if (this.m_allowedCharacters.indexOf(var2.charAt(var4)) == -1) {
            Toolkit.getDefaultToolkit().beep();
            return;
         }
      }

      super.insertString(var1, var2, var3);
   }
}
