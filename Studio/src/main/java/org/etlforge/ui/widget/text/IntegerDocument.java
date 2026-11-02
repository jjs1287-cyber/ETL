package org.etlforge.ui.widget.text;

import com.google.common.annotations.VisibleForTesting;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public final class IntegerDocument extends PlainDocument {
   private final IntegerDocument.Mode mode;

   public static IntegerDocument strictlyPositive() {
      return new IntegerDocument(IntegerDocument.Mode.STRICTLY_POSITIVE);
   }

   public static IntegerDocument noNegatives() {
      return new IntegerDocument(IntegerDocument.Mode.NOT_NEGATIVE);
   }

   public IntegerDocument() {
      this(IntegerDocument.Mode.ALL);
   }

   private IntegerDocument(IntegerDocument.Mode var1) {
      this.mode = var1;
   }

   @Override
   public void insertString(int var1, String var2, AttributeSet var3) throws BadLocationException {
      for (int var4 = var2.length() - 1; var4 >= 0; var4--) {
         char var5 = var2.charAt(var4);
         boolean var6 = this.isCharacterAllowed(var5, var4 == 0 && var1 == 0);
         if (!var6) {
            return;
         }
      }

      super.insertString(var1, var2, var3);
   }

   @VisibleForTesting
   boolean isCharacterAllowed(char var1, boolean var2) {
      return var2 ? this.mode.isValidFirstCharacter(var1) : Character.isDigit(var1);
   }

   private enum Mode {
      ALL {
         @Override
         public boolean isValidFirstCharacter(char var1) {
            return Character.isDigit(var1) || var1 == '-';
         }
      },
      NOT_NEGATIVE {
         @Override
         public boolean isValidFirstCharacter(char var1) {
            return Character.isDigit(var1);
         }
      },
      STRICTLY_POSITIVE {
         @Override
         public boolean isValidFirstCharacter(char var1) {
            return Character.isDigit(var1) && var1 != '0';
         }
      };

      Mode() {
      }

      public abstract boolean isValidFirstCharacter(char var1);
   }
}
