package org.etlforge.integration.data.script;

import org.apache.commons.lang.StringUtils;

final class FunctionGrammarParser {
   public static FCN parse(String var0) {
      if (var0.equals("N/A")) {
         return FCN.fixed(0);
      }

      String var1 = StringUtils.substringAfter(var0, ";");
      if (StringUtils.isEmpty(var1)) {
         throw new RuntimeException("Invalid function grammar - missing argument descriptor: " + var0);
      }

      int var2 = 0;
      int var3 = 0;
      boolean var4 = false;
      if (isNumber(var1)) {
         var2 = Integer.parseInt(var1);
      } else {
         for (char var8 : var1.toCharArray()) {
            switch (var8) {
               case '.':
                  var4 = true;
                  break;
               case '?':
                  validateOptionalArgumentMarker(var0, var4);
                  var3++;
                  break;
               default:
                  validateRequiredArgumentMarker(var0, var8, var3, var4);
                  var2++;
            }
         }
      }

      return new FCN(var2, var3, var4);
   }

   private static void validateOptionalArgumentMarker(String var0, boolean var1) {
      if (var1) {
         throw new RuntimeException("Invalid function grammar - optional argument marker ('?') cannot appear after varargs marker ('.'): " + var0);
      }
   }

   private static void validateRequiredArgumentMarker(String var0, char var1, int var2, boolean var3) {
      if (var3) {
         throw new RuntimeException("Invalid function grammar - required argument marker cannot appear after varargs marker ('.'): " + var0);
      }

      if (var2 > 0) {
         throw new RuntimeException("Invalid function grammar - required argument marker cannot appear after optional argument marker ('?'): " + var0);
      }

      if (Character.isDigit(var1)) {
         throw new RuntimeException("Invalid function grammar - a number may not appear with non-numbers in the argument list: " + var0);
      }
   }

   private static boolean isNumber(String var0) {
      for (char var4 : var0.toCharArray()) {
         if (!Character.isDigit(var4)) {
            return false;
         }
      }

      return true;
   }

   private FunctionGrammarParser() {
   }
}
