package com.etlforge.integration.client.ui.script.fcn.db.sql;

import java.util.StringTokenizer;

final class StringDetector {
   public static boolean isInString(String var0, int var1) {
      if (var1 == 0) {
         return false;
      }

      boolean var2 = false;
      boolean var3 = false;
      int var4 = 0;
      StringTokenizer var5 = new StringTokenizer(var0, "\\\"", true);

      while (var5.hasMoreTokens()) {
         String var6 = var5.nextToken();
         char var7 = var6.charAt(0);
         switch (var7) {
            case '"':
               if (var3) {
                  var3 = false;
               } else {
                  var2 = !var2;
               }
               break;
            case '\\':
               var3 = !var3;
         }

         var4 += var6.length();
         if (var4 >= var1) {
            return var2;
         }
      }

      return false;
   }

   private StringDetector() {
   }
}
