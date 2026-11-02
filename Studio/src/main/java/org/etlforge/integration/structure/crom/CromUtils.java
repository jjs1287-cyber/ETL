package org.etlforge.integration.structure.crom;

import java.util.StringTokenizer;
import org.apache.commons.lang.StringUtils;

public final class CromUtils {
   public static String convertIllegalCharacters(String var0) {
      StringTokenizer var1 = new StringTokenizer(var0, " +-*/^$.#'&()", true);
      StringBuilder var2 = new StringBuilder();

      while (var1.hasMoreTokens()) {
         String var3 = var1.nextToken();
         switch (var3.charAt(0)) {
            case ' ':
            case '#':
            case '$':
            case '&':
            case '\'':
            case '(':
            case ')':
            case '*':
            case '+':
            case '-':
            case '.':
            case '/':
            case '^':
               var3 = "?";
            default:
               var2.append(var3);
         }
      }

      return var2.toString();
   }

   public static String removeDataTypeFromDisplayName(String var0) {
      String var1 = StringUtils.substringBefore(var0, "(").trim();
      return StringUtils.removeEnd(var1, "?");
   }

   public static long getTypeCodeFromString(String var0) {
      String var1 = StringUtils.removeStartIgnoreCase(var0, "0x");
      return StringUtils.isEmpty(var1) ? 0L : Long.parseLong(var1, 16);
   }

   public static String getDataTypeForDisplay(String var0, int var1) {
      return StringUtils.isEmpty(var0) ? "" : " (" + var0 + ")";
   }

   private CromUtils() {
   }
}
