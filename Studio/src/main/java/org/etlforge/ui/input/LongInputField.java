package org.etlforge.ui.input;

import javax.swing.JLabel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;
import org.etlforge.ui.widget.label.KongaLabel;

public class LongInputField extends TextInputField2 {
   private long minValue;
   private long maxValue;

   public LongInputField(String var1, JTextComponent var2, long var3, long var5, String var7) {
      this(var1, var2, var3, var5, toKongaLabel(var7));
   }

   private static KongaLabel toKongaLabel(String var0) {
      return var0 == null ? null : KongaLabel.withMnemonic(var0);
   }

   public LongInputField(String var1, JTextComponent var2, long var3, long var5, JLabel var7) {
      super(var1, var2, var7);
      if (var3 > var5) {
         throw new IllegalArgumentException("minVal > maxVal (" + var3 + " > " + var5 + ").");
      }

      this.minValue = var3;
      this.maxValue = var5;
   }

   @Override
   public String getInvalidMessage(String var1) {
      return PackageResources.LongInputField.getInvalidRange(this.minValue, this.maxValue);
   }

   @Override
   public boolean isInputValid(String var1) {
      if (!this.isEnabled()) {
         return true;
      }

      if (var1 != null && var1.length() != 0) {
         try {
            long var2 = Long.parseLong(var1);
            return var2 >= this.minValue && var2 <= this.maxValue;
         } catch (NumberFormatException var4) {
            return false;
         }
      } else {
         return !this.isRequired();
      }
   }

   public void setMinimumValue(long var1) {
      this.minValue = var1;
   }

   public long getMinimumValue() {
      return this.minValue;
   }

   public void setMaximumValue(long var1) {
      this.maxValue = var1;
   }

   public long getMaximumValue() {
      return this.maxValue;
   }

   public long getLongValue() throws NumberFormatException {
      return Long.parseLong(this.getValue().trim());
   }

   public long getLongValue(long var1) {
      try {
         return this.getLongValue();
      } catch (NumberFormatException var4) {
         return var1;
      }
   }

   public static class LongDocument extends PlainDocument {
      private final boolean allowNegativeNumbers;

      public LongDocument(boolean var1) {
         this.allowNegativeNumbers = var1;
      }

      @Override
      public void insertString(int var1, String var2, AttributeSet var3) throws BadLocationException {
         for (int var4 = var2.length() - 1; var4 >= 0; var4--) {
            char var5 = var2.charAt(var4);
            if (!this.isAllowed(var5, var4, var1)) {
               return;
            }
         }

         super.insertString(var1, var2, var3);
      }

      private boolean isAllowed(char var1, int var2, int var3) {
         return !this.allowNegativeNumbers ? Character.isDigit(var1) : Character.isDigit(var1) || var1 == '-' && var2 == 0 && var3 == 0;
      }
   }
}
