package org.etlforge.util;

import org.apache.commons.lang.StringUtils;

public final class CSVUtils {
   public static String getValidCSVString(String var0, String var1) {
      if (var0 != null && !var0.isEmpty()) {
         String var2 = var1 + "\n";
         if (var0.charAt(0) != '"' && !StringUtils.containsAny(var0, var2)) {
            return var0;
         }

         StringBuilder var3 = new StringBuilder();
         var3.append('"');
         int var4 = 0;

         int var5;
         while ((var5 = var0.indexOf(34, var4)) >= 0) {
            var3.append(var0.substring(var4, var5 + 1));
            var3.append('"');
            var4 = var5 + 1;
         }

         var3.append(var0.substring(var4));
         var3.append('"');
         return var3.toString();
      } else {
         return var0;
      }
   }

   private CSVUtils() {
   }
}
