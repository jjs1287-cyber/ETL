package org.etlforge.integration.salesforce.parser;

import com.google.common.base.CharMatcher;
import com.google.common.base.Optional;

final class NameToken {
   public static Optional<String> matches(String var0, int var1) {
      char var2 = var0.charAt(var1);
      if (Character.isLetter(var2)) {
         int var3 = locateEnd(var0, var1 + 1);
         String var4 = var0.substring(var1, var3);
         if (!isReservedWord(var4)) {
            return Optional.of(var4);
         }
      }

      return Optional.absent();
   }

   private static int locateEnd(String var0, int var1) {
      CharMatcher var2 = new CharMatcher() {
         @Override
         public boolean matches(char var1) {
            return !Character.isLetterOrDigit(var1) && var1 != '.' && var1 != '_';
         }
      };
      int var3 = var2.indexIn(var0, var1);
      return var3 == -1 ? var0.length() : var3;
   }

   private static boolean isReservedWord(String var0) {
      String var1 = var0.toLowerCase();
      return var1.equals("select") || var1.equals("from");
   }

   private NameToken() {
   }
}
