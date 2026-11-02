package org.etlforge.integration.data.script;

import java.util.StringTokenizer;

public final class ScriptUtils {
   public static String stripInstanceTag(String var0) {
      if (!var0.contains("#")) {
         return var0;
      }

      String var1 = "#$.";
      StringTokenizer var2 = new StringTokenizer(var0, var1, true);
      boolean var3 = false;
      StringBuilder var4 = new StringBuilder();

      while (var2.hasMoreTokens()) {
         String var5 = var2.nextToken();
         switch (var5.charAt(0)) {
            case '#':
               var3 = true;
               break;
            case '$':
            case '.':
               var3 = false;
            default:
               if (!var3) {
                  var4.append(var5);
               }

               var3 = false;
         }
      }

      return var4.toString();
   }

   public static String getInstanceTag(String var0) {
      int var1 = var0.lastIndexOf(35);
      int var2 = var0.indexOf(".", var1 + 1);
      return var1 > 0 && var2 > var1 ? var0.substring(var1, var2) : null;
   }

   private ScriptUtils() {
   }
}
