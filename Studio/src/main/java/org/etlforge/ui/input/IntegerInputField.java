package org.etlforge.ui.input;

import javax.swing.JLabel;
import javax.swing.text.JTextComponent;
import org.etlforge.ui.widget.label.KongaLabel;

public class IntegerInputField extends TextInputField2 {
   private int minValue;
   private int maxValue;

   public IntegerInputField(String var1, JTextComponent var2, int var3, int var4, String var5) {
      this(var1, var2, var3, var4, KongaLabel.withMnemonic(var5));
   }

   public IntegerInputField(String var1, JTextComponent var2, int var3, int var4, JLabel var5) {
      super(var1, var2, var5);
      if (var3 > var4) {
         throw new IllegalArgumentException("minVal > maxVal (" + var3 + " > " + var4 + ").");
      }

      this.minValue = var3;
      this.maxValue = var4;
   }

   @Override
   public String getInvalidMessage(String var1) {
      return PackageResources.IntegerInputField.getInvalidRange(this.minValue, this.maxValue);
   }

   @Override
   public boolean isInputValid(String var1) {
      if (!this.isEnabled()) {
         return true;
      }

      if (var1 != null && var1.length() != 0) {
         try {
            int var2 = Integer.parseInt(var1);
            return var2 >= this.minValue && var2 <= this.maxValue;
         } catch (NumberFormatException var3) {
            return false;
         }
      } else {
         return !this.isRequired();
      }
   }

   public void setMinimumValue(int var1) {
      this.minValue = var1;
   }

   public int getMinimumValue() {
      return this.minValue;
   }

   public void setMaximumValue(int var1) {
      this.maxValue = var1;
   }

   public int getMaximumValue() {
      return this.maxValue;
   }

   public int getIntValue() throws NumberFormatException {
      return Integer.parseInt(this.getValue().trim());
   }

   public int getIntValue(int var1) {
      try {
         return this.getIntValue();
      } catch (NumberFormatException var3) {
         return var1;
      }
   }
}
