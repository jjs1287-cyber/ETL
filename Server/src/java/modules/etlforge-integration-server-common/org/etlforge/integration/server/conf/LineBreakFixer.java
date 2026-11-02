package org.etlforge.integration.server.conf;

import java.util.StringTokenizer;
import org.apache.commons.lang.SystemUtils;

final class LineBreakFixer {
   public static String ensureCorrectLineBreaks(String var0) {
      return SystemUtils.IS_OS_WINDOWS ? ensureWindowsLinebreaks(var0) : ensureLinuxLinebreaks(var0);
   }

   static String ensureWindowsLinebreaks(String var0) {
      StringBuilder var1 = new StringBuilder();
      StringTokenizer var2 = new StringTokenizer(var0, "\r\n", true);
      boolean var3 = false;

      while (var2.hasMoreTokens()) {
         String var4 = var2.nextToken();
         if (var4.equals("\r")) {
            if (var3) {
               var1.append("\r");
            } else {
               var3 = true;
            }
         } else if (var4.equals("\n")) {
            if (var3) {
               var3 = false;
            }

            var1.append("\r\n");
         } else {
            if (var3) {
               var1.append("\r");
               var3 = false;
            }

            var1.append(var4);
         }
      }

      if (var3) {
         var1.append("\r");
      }

      return var1.toString();
   }

   static String ensureLinuxLinebreaks(String var0) {
      return var0.replaceAll("\\r\\n", "\n");
   }

   private LineBreakFixer() {
   }
}
