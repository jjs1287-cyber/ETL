package org.etlforge.integration.structure.reader;

public class CSVUtils {
   public static String stripQualifierandTrim(String var0, char var1) {
      if (isQualified(var0, var1)) {
         var0 = var0.substring(1, var0.length() - 1);
      }

      return var0.trim();
   }

   public static boolean isQualified(String var0, char var1) {
      return var0 != null && var0.length() > 1 && var0.charAt(0) == var1 && var0.charAt(var0.length() - 1) == var1;
   }

   public static String decode(String var0, char var1) {
      if (isQualified(var0, var1)) {
         String var4 = "";
         int var3 = 1;
         int var2 = 1;

         while (var3 < var0.length() - 1) {
            if (var0.charAt(var3) == var1) {
               var4 = var4 + var0.substring(var2, ++var3);
               var2 = var3 + 1;
            }

            var3++;
         }

         if (var2 < var3) {
            var4 = var4 + var0.substring(var2, var3);
         }

         return var4;
      } else {
         return var0;
      }
   }

   public static String encode(String var0, char var1) {
      String var4 = "";
      int var3 = 0;
      int var2 = 0;

      while (var3 < var0.length()) {
         if (var0.charAt(var3) == var1) {
            var4 = var4 + var0.substring(var2, ++var3);
            var4 = var4 + var1;
            var2 = var3;
         }

         var3++;
      }

      if (var2 < var3) {
         var4 = var4 + var0.substring(var2, var3);
      }

      return var1 + var4 + var1;
   }
}
