package org.etlforge.integration.data.structure.text;

public final class TextStructureNameUtils {
   public static boolean isFieldNameIllegal(String var0) {
      int var1 = var0.length();

      for (int var2 = 0; var2 < var1; var2++) {
         char var3 = var0.charAt(var2);
         if (isIllegal(var3, var2)) {
            return true;
         }
      }

      return false;
   }

   private static boolean isIllegal(char var0, int var1) {
      return Character.isDigit(var0) && var1 == 0 ? true : !Character.isLetterOrDigit(var0) && var0 != '_' && var0 != '?';
   }

   public static String replaceIllegalCharacters(String var0) {
      StringBuilder var1 = new StringBuilder();
      int var2 = 0;

      for (int var3 = var0.length(); var2 < var3; var2++) {
         char var4 = var0.charAt(var2);
         if (isIllegal(var4, var2)) {
            var1.append("_");
         } else {
            var1.append(var4);
         }
      }

      return var1.toString();
   }
}
