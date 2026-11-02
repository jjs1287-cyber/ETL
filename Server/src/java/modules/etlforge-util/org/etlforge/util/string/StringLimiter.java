package org.etlforge.util.string;

import java.text.BreakIterator;

public class StringLimiter {
   private final String input;
   private boolean breakAtWordBoundaries;

   public static String limit(String var0, int var1) {
      StringLimiter var2 = new StringLimiter(var0);
      return var2.getLimitedString(var1);
   }

   public StringLimiter(String var1) {
      if (var1 == null) {
         throw new NullPointerException("input");
      }

      this.input = var1;
      this.breakAtWordBoundaries = true;
   }

   public final String getInputString() {
      return this.input;
   }

   public void setBreakAtWordBoundaries(boolean var1) {
      this.breakAtWordBoundaries = var1;
   }

   public final String getLimitedString(int var1) {
      if (var1 <= 0) {
         throw new IllegalArgumentException("maxLength must be > 0 (was " + var1 + ")");
      } else {
         return this.getLimitedString(this.input, var1);
      }
   }

   private String getLimitedString(String var1, int var2) {
      assert var2 > 0;
      if (var1.length() <= var2) {
         return var1;
      } else if (var2 <= 3) {
         return var1.substring(0, var2);
      } else {
         return this.breakAtWordBoundaries ? this.getWordBoundaryLimitedString(var2) : this.getBruteForceDelimitedString(var2);
      }
   }

   private String getWordBoundaryLimitedString(int var1) {
      StringBuilder var2 = new StringBuilder();
      BreakIterator var3 = this.getWordIterator();
      var3.setText(this.input);
      int var4 = 0;

      while (true) {
         int var5 = var3.next();
         if (var5 == -1) {
            break;
         }

         String var6 = this.input.substring(var4, var5);
         if (var2.length() + var6.length() + 3 >= var1) {
            if (var2.length() == 0) {
               return this.getBruteForceDelimitedString(var1);
            }

            var2.append("...");
            break;
         }

         var2.append(var6);
         var4 = var5;
      }

      return var2.length() == 0 ? this.getBruteForceDelimitedString(var1) : var2.toString();
   }

   private String getBruteForceDelimitedString(int var1) {
      int var2 = var1 - 3;
      assert var2 < this.input.length();
      return this.input.substring(0, var2) + "...";
   }

   protected BreakIterator getWordIterator() {
      return BreakIterator.getWordInstance();
   }
}
