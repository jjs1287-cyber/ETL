package org.etlforge.integration.json.xml;

import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Map;

final class XmlElementNameWriter {
   private final String replacement;
   private final Map<String, String> safeNames = Maps.newHashMap();

   public XmlElementNameWriter(String var1) {
      this.replacement = var1;
   }

   public void appendElementName(Appendable var1, String var2) throws IOException {
      String var3 = this.getSafeElementName(var2);
      var1.append(var3);
   }

   private String getSafeElementName(String var1) {
      String var2 = this.safeNames.get(var1);
      if (var2 == null) {
         var2 = this.createSafeName(var1);
         this.safeNames.put(var1, var2);
      }

      return var2;
   }

   private String createSafeName(String var1) {
      if (isNumeric(var1)) {
         return this.replacement + var1;
      }

      StringBuilder var2 = new StringBuilder();
      boolean var3 = true;

      for (char var7 : var1.toCharArray()) {
         boolean var8 = true;
         if (var3) {
            var8 = isValidBeginningCharacter(var7);
            var3 = false;
         } else {
            var8 = isValidNameCharacter(var7);
         }

         if (var8) {
            var2.append(var7);
         } else {
            var2.append(this.replacement);
         }
      }

      String var9 = var2.toString();
      if (var9.toLowerCase().startsWith("xml")) {
         var9 = this.replacement + this.replacement + this.replacement + var9.substring(3);
      }

      return var9;
   }

   private static boolean isNumeric(String var0) {
      int var1 = 0;

      for (int var2 = var0.length(); var1 < var2; var1++) {
         char var3 = var0.charAt(var1);
         if (!Character.isDigit(var3)) {
            return false;
         }
      }

      return true;
   }

   private static boolean isValidBeginningCharacter(char var0) {
      return Character.isLetter(var0) || var0 == '_';
   }

   private static boolean isValidNameCharacter(char var0) {
      return Character.isLetter(var0) || Character.isDigit(var0) || var0 == '.' || var0 == '_' || var0 == '-';
   }
}
