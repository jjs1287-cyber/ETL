package org.etlforge.integration.server.engine.connector.util;

import com.google.common.base.Strings;

public class HttpCallUrlUtils {
   static final char forwardslash = '/';
   static final char backwardslash = '\\';

   private static boolean isSlash(char var0) {
      return var0 == '/' || var0 == '\\';
   }

   public static String getFullUrl(String var0, String var1) {
      if (!Strings.isNullOrEmpty(var0) && !Strings.isNullOrEmpty(var1)) {
         if (isSlash(var1.charAt(0))) {
            var1 = var1.substring(1);
         }

         char var2 = var0.charAt(var0.length() - 1);
         return isSlash(var2) ? var0 + var1 : var0 + '/' + var1;
      } else {
         return var0 + var1;
      }
   }

   public static void main(String[] var0) {
   }
}
