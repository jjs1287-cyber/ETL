package org.etlforge.lang;

public final class DoubleParser {
   public static double parse(String var0, double var1) {
      if (var0 != null && !var0.isEmpty()) {
         try {
            return Double.parseDouble(var0);
         } catch (NumberFormatException var4) {
         }
      }

      return var1;
   }

   private DoubleParser() {
   }
}
