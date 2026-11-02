package org.etlforge.xml;

import org.apache.commons.lang.StringUtils;

public final class ElementNameSanitizer {
   private final char illegalCharReplacement;

   public static boolean isValid(String var0) {
      if (StringUtils.isEmpty(var0)) {
         return false;
      }

      char var1 = 0;
      ElementNameSanitizer var2 = new ElementNameSanitizer(var1);
      String var3 = var2.getSanitizedName(var0);
      return var0.equals(var3);
   }

   public ElementNameSanitizer(char var1) {
      this.illegalCharReplacement = var1;
   }

   public String getSanitizedName(String var1) {
      StringBuilder var2 = new StringBuilder();
      boolean var3 = true;

      for (char var7 : var1.toCharArray()) {
         boolean var8 = var3 ? isValidStart(var7) : isValidNonStart(var7);
         if (var8) {
            var2.append(var7);
         } else if (this.illegalCharReplacement != 0) {
            var2.append(this.illegalCharReplacement);
         }

         var3 = false;
      }

      return var2.toString();
   }

   public static boolean isValidStart(char var0) {
      return inRange(var0, 'A', 'Z')
         || inRange(var0, 'a', 'z')
         || var0 == ':'
         || var0 == '_'
         || inRange(var0, '\u00c0', '\u00d6')
         || inRange(var0, '\u00d8', '\u00f6')
         || inRange(var0, '\u00f8', '\u02ff')
         || inRange(var0, '\u0370', '\u037d')
         || inRange(var0, '\u037f', '\u1fff')
         || inRange(var0, '\u200c', '\u200d')
         || inRange(var0, '\u2070', '\u2187')
         || inRange(var0, '\u2c00', '\u2fef')
         || inRange(var0, '\u3001', '\ud7ff')
         || inRange(var0, '\uf900', '\ufdcf')
         || inRange(var0, '\ufdf0', '\ufffd');
   }

   private static boolean isValidNonStart(char var0) {
      return isValidStart(var0)
         || var0 == '-'
         || var0 == '.'
         || inRange(var0, '0', '9')
         || var0 == 183
         || inRange(var0, '\u0300', '\u036f')
         || inRange(var0, '\u203f', '\u2040');
   }

   private static boolean inRange(char var0, char var1, char var2) {
      return var0 >= var1 && var0 <= var2;
   }
}
