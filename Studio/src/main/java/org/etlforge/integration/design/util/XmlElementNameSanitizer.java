package org.etlforge.integration.design.util;

import org.etlforge.xml.ElementNameSanitizer;

public class XmlElementNameSanitizer {
   private static final char underscore = '_';
   private static final ElementNameSanitizer saniter = new ElementNameSanitizer('_');

   public static String getSanitizedName(String var0) {
      return var0 != null && !var0.isEmpty() ? saniter.getSanitizedName(var0) : var0;
   }

   public static String getSanitizedName(String var0, char var1, int var2) {
      return getSanitizedXmlName(var0, var1, var2);
   }

   public static String getSanitizedXmlName(String var0, char var1, int var2) {
      if (var0 != null && !var0.isEmpty()) {
         if (!ElementNameSanitizer.isValidStart(var0.charAt(0))) {
            var0 = var1 + var0;
         }

         if (var0.length() > var2) {
            var0 = var0.substring(0, var2);
         }

         return saniter.getSanitizedName(var0);
      } else {
         return var0;
      }
   }
}
