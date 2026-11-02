package org.etlforge.lang;

public final class LongParser {
   public static long parse(String var0, long var1) {
      if (var0 != null && !var0.isEmpty()) {
         try {
            return Long.parseLong(var0);
         } catch (NumberFormatException var4) {
         }
      }

      return var1;
   }

   private LongParser() {
   }
}
