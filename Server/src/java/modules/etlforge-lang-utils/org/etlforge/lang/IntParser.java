package org.etlforge.lang;

public final class IntParser {
   public static int parse(String var0, int var1) {
      if (var0 != null && !var0.isEmpty()) {
         try {
            return Integer.parseInt(var0);
         } catch (NumberFormatException var3) {
         }
      }

      return var1;
   }

   private IntParser() {
   }
}
