package org.etlforge.application.ui.widget;

import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;

public abstract class TextFieldInputValidator {
   public static final TextFieldInputValidator ALWAYS_VALID = new TextFieldInputValidator() {
      @Override
      public boolean validate(String var1) {
         return true;
      }

      @Override
      public String getErrorMessage(String var1) {
         return null;
      }
   };
   public static final TextFieldInputValidator INTEGER = new TextFieldInputValidator() {
      @Override
      public boolean validate(String var1) {
         try {
            Integer.parseInt(var1);
            return true;
         } catch (NumberFormatException var3) {
            return false;
         }
      }

      @Override
      public String getErrorMessage(String var1) {
         return StringUtils.isBlank(var1) ? "Must provide a value." : "'" + var1 + "' is not a valid integer.";
      }
   };

   public abstract boolean validate(String var1);

   public abstract String getErrorMessage(String var1);

   public static TextFieldInputValidator integerInRange(int var0, int var1) {
      Preconditions.checkArgument(var1 >= var0, String.format("max must be >= min (max = %d, min = %d)", var1, var0));
      return new TextFieldInputValidator.IntegerRange(var0, var1);
   }

   private static class IntegerRange extends TextFieldInputValidator {
      private String error;
      private final int min;
      private final int max;

      public IntegerRange(int var1, int var2) {
         this.min = var1;
         this.max = var2;
      }

      @Override
      public boolean validate(String var1) {
         if (!INTEGER.validate(var1)) {
            this.error = INTEGER.getErrorMessage(var1);
            return false;
         }

         int var2 = Integer.parseInt(var1);
         if (var2 >= this.min && var2 <= this.max) {
            return true;
         }

         this.error = "Value out of range";
         return false;
      }

      @Override
      public String getErrorMessage(String var1) {
         return this.error;
      }
   }
}
