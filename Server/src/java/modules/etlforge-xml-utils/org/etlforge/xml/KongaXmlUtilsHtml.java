package org.etlforge.xml;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import org.etlforge.io.KongaIoUtils;

public class KongaXmlUtilsHtml {
   private static boolean isHtmlRoot(String var0) {
      return var0.isEmpty() || var0.equalsIgnoreCase("html");
   }

   public static boolean isHtml(String var0) {
      return isHtmlRoot(getXmlRootName(var0));
   }

   public static boolean isHtmlFile(String var0, Charset var1) throws IOException {
      return isHtmlRoot(getXmlRootName(var0, 2000, var1));
   }

   private static String getXmlRootName(String var0) {
      int var1 = -1;
      int var2 = 0;

      while (var2 < var0.length()) {
         var2 = var0.indexOf(60, var2);
         var1 = var2 + 2;
         if (var2 < 0 || var1 >= var0.length()) {
            return "";
         }

         char var3 = var0.charAt(var2 + 1);
         boolean var4 = var3 != '?' && var3 != '!';
         if (var4) {
            var1 = var0.indexOf(62, var1);
            if (var1 <= var2) {
               return "";
            }

            String var5 = var0.substring(var2 + 1, var1);

            for (int var8 = 0; var8 < var5.length(); var8++) {
               if (Character.isWhitespace(var5.charAt(var8))) {
                  var5 = var5.substring(0, var8);
                  break;
               }
            }

            var2 = var5.indexOf(58);
            if (var2 >= 0) {
               var2++;
               var5 = var2 < var5.length() ? var5.substring(var2, var5.length()) : "";
            }

            return var5;
         }

         var2 = var1;
      }

      return "";
   }

   private static String getXmlRootName(String var0, int var1, Charset var2) throws IOException {
      String var3 = KongaIoUtils.read(new File(var0), var2, var1);
      return getXmlRootName(var3);
   }
}
