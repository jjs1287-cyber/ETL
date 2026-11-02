package org.etlforge.lang;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.StringTokenizer;
import org.apache.commons.lang.StringUtils;

public final class KongaStringUtils {
   public static final Comparator<String> NATURAL_ORDER = new KongaStringUtils.NaturalOrder();

   public static int compareLength(String var0, String var1) {
      return Ints.compare(var0.length(), var1.length());
   }

   public static boolean areEqual(String var0, String var1) {
      return var0 == null ? var1 == null : var0.equals(var1);
   }

   public static boolean stringsDiffer(String var0, String var1) {
      return !areEqual(var0, var1);
   }

   public static String replaceStringIgnoringCase(String var0, String var1, String var2) {
      int var4 = 0;
      int var5 = var1.length();
      StringBuffer var6 = new StringBuffer(1000);
      String var7 = var1.toUpperCase();

      int var3;
      for (String var8 = var0.toUpperCase(); (var3 = var8.indexOf(var7, var4)) != -1; var4 += var5) {
         if (var3 > var4) {
            var6.append(var0.substring(var4, var3));
         }

         var6.append(var2);
      }

      if (var4 == 0) {
         return var0;
      }

      if (var4 < var0.length()) {
         var6.append(var0.substring(var4));
      }

      return var6.toString();
   }

   public static int compare(String var0, String var1) {
      if (var0 == null) {
         return var1 == null ? 0 : -1;
      } else {
         return var1 == null ? 1 : var0.compareTo(var1);
      }
   }

   public static String unescape(String var0, char var1) {
      StringTokenizer var2 = new StringTokenizer(var0, new String(new char[]{var1}), true);
      StringBuffer var3 = new StringBuffer();
      boolean var4 = true;

      while (var2.hasMoreTokens()) {
         String var5 = var2.nextToken();
         if (var5.charAt(0) == var1) {
            if (var4) {
               var4 = false;
               continue;
            }

            var4 = true;
         }

         var3.append(var5);
      }

      return var3.toString();
   }

   public static boolean CSV_RequireQualifier(String var0, String var1) {
      String var2 = var1 + "\r\n";
      if (var0.isEmpty()) {
         return true;
      }

      if (var0.charAt(0) == '"') {
         return true;
      }

      int var3 = 0;

      for (int var4 = var0.length(); var3 < var4; var3++) {
         char var5 = var0.charAt(var3);
         if (var2.indexOf(var5) >= 0) {
            return true;
         }
      }

      return false;
   }

   public static String CSV_EscapeDoubleQuote(String var0) {
      StringBuffer var1 = new StringBuffer();
      StringTokenizer var2 = new StringTokenizer(var0, "\"", true);

      while (var2.hasMoreTokens()) {
         String var3 = var2.nextToken();
         if (var3.charAt(0) == '"') {
            var1.append(var3);
         }

         var1.append(var3);
      }

      return var1.toString();
   }

   public static String[] split(String var0, String var1, char var2) {
      Preconditions.checkNotNull(var0, "inputString");
      Preconditions.checkNotNull(var1, "delimiter");
      Preconditions.checkArgument(var2 != 0, "escapeChar cannot be '\\0'");
      String var3 = var1 + var2;
      StringTokenizer var4 = new StringTokenizer(var0, var3, true);
      boolean var5 = false;
      StringBuffer var6 = new StringBuffer();
      ArrayList var7 = new ArrayList();

      while (var4.hasMoreTokens()) {
         String var8 = var4.nextToken();
         if (var8.charAt(0) == var2) {
            var5 = !var5;
            if (var5) {
               continue;
            }
         } else if (var1.indexOf(var8) >= 0) {
            if (!var5) {
               var7.add(unescape(var6.toString(), var2));
               var6.setLength(0);
               continue;
            }

            var5 = false;
         }

         var6.append(var8);
      }

      if (var6.length() > 0) {
         var7.add(var6.toString());
      }

      return (String[]) var7.toArray(new String[0]);
   }

   public static String[] split(String var0) {
      Preconditions.checkNotNull(var0, "inputString");
      StringTokenizer var1 = new StringTokenizer(var0, "\t", true);
      ArrayList var2 = new ArrayList();
      String var3 = "";

      while (var1.hasMoreTokens()) {
         String var4 = var1.nextToken();
         if (var4.equals("\t")) {
            if (var3 == null) {
               var3 = "";
            }

            var2.add(var3);
            var3 = null;
         } else {
            int var5 = var4.indexOf(92);
            if (var5 <= 0) {
               var3 = var4;
            } else {
               int var6 = 0;
               int var7 = var4.length();
               StringBuilder var8 = new StringBuilder(var3);

               while (true) {
                  if (var5 > 0) {
                     var8.append(var4.substring(var6, var5));
                     switch (var4.charAt(var5 + 1)) {
                        case 'n':
                           var8.append('\n');
                           break;
                        case 'r':
                           var8.append('\r');
                           break;
                        case 't':
                           var8.append('\t');
                     }

                     var6 = var5 + 2;
                     if (var6 < var7) {
                        var5 = var4.indexOf(92, var6);
                        continue;
                     }
                  }

                  var3 = var8.toString();
                  break;
               }
            }
         }
      }

      if (var3 != null) {
         var2.add(var3);
      }

      return (String[]) var2.toArray(new String[0]);
   }

   public static String[] splitWithFirstTab(String var0) {
      ArrayList var1 = new ArrayList();
      int var2 = var0.indexOf(9);
      if (var2 >= 0) {
         var1.add(var0.substring(0, var2));
         String var3 = var0.substring(var2 + 1);
         var1.add(var3);
      }

      return (String[]) var1.toArray(new String[var1.size()]);
   }

   public static int parseInt(String var0, int var1) {
      return IntParser.parse(var0, var1);
   }

   public static String collapseSpaces(String var0) {
      if (var0.indexOf("  ") == -1) {
         return var0;
      }

      StringBuilder var1 = new StringBuilder();
      int var2 = var0.length();
      boolean var3 = false;

      for (int var4 = 0; var4 < var2; var4++) {
         char var5 = var0.charAt(var4);
         if (var5 == ' ') {
            if (var3) {
               continue;
            }

            var3 = true;
         } else {
            var3 = false;
         }

         var1.append(var5);
      }

      return var1.toString();
   }

   public static String unquote(String var0) {
      if (var0 == null) {
         return null;
      } else {
         int var1 = var0.length();
         if (var1 < 2) {
            return var0;
         } else {
            return (var0.charAt(0) != '\'' || var0.charAt(var1 - 1) != '\'') && (var0.charAt(0) != '"' || var0.charAt(var1 - 1) != '"')
               ? var0
               : var0.substring(1, var1 - 1);
         }
      }
   }

   public static String findLongestCommonStartFragment(Iterable<String> var0, boolean var1) {
      String var2 = null;

      for (String var4 : var0) {
         if (var2 == null) {
            var2 = var4;
         } else {
            StringBuilder var5 = new StringBuilder();
            int var6 = 0;

            while (true) {
               if (var6 < var2.length() && var6 < var4.length()) {
                  char var7 = var2.charAt(var6);
                  char var8 = var4.charAt(var6);
                  if (isSame(var7, var8, var1)) {
                     var5.append(var7);
                     var6++;
                     continue;
                  }
               }

               if (var5.length() == 0) {
                  return "";
               }

               var2 = var5.toString();
               break;
            }
         }
      }

      return var2;
   }

   private static boolean isSame(char var0, char var1, boolean var2) {
      return var0 == var1 || var2 && Character.toLowerCase(var0) == Character.toLowerCase(var1);
   }

   public static String escapeDoubleQuotes(String var0) {
      int var1 = var0.indexOf(34);
      if (var1 == -1) {
         return var0;
      }

      StringBuilder var2 = new StringBuilder();
      int var3 = 0;

      while (var1 >= 0) {
         var2.append(var0.substring(var3, var1 + 1));
         var3 = var1;
         var1 = var0.indexOf(34, var1 + 1);
      }

      var2.append(var0.substring(var3));
      return var2.toString();
   }

   public static String removeStartAndEnd(String var0, String var1) {
      return removeStartAndEnd(var0, var1, var1);
   }

   public static String removeStartAndEnd(String var0, String var1, String var2) {
      if (StringUtils.isEmpty(var0)) {
         return var0;
      }

      String var3 = StringUtils.removeStart(var0, var1);
      return StringUtils.removeEnd(var3, var2);
   }

   public static String removeStartAndEndIfBothPresent(String var0, String var1) {
      return removeStartAndEndIfBothPresent(var0, var1, var1);
   }

   public static String removeStartAndEndIfBothPresent(String var0, String var1, String var2) {
      if (StringUtils.isEmpty(var0)) {
         return var0;
      } else {
         return var0.startsWith(var1) && var0.endsWith(var2) ? var0.substring(var1.length(), var0.length() - var2.length()) : var0;
      }
   }

   public static String unwrap(String var0) {
      return var0.substring(1, var0.length() - 1);
   }

   public static int indexOfAny(String var0, String var1) {
      return indexOfAny(var0, var1, 0);
   }

   public static int indexOfAny(String var0, String var1, int var2) {
      if (var2 >= var0.length()) {
         return -1;
      }

      for (int var3 = Math.max(var2, 0); var3 < var0.length(); var3++) {
         if (var1.indexOf(var0.charAt(var3)) >= 0) {
            return var3;
         }
      }

      return -1;
   }

   public static int lastIndexOfAny(String var0, String var1) {
      return lastIndexOfAny(var0, var1, var0.length() - 1);
   }

   public static int lastIndexOfAny(String var0, String var1, int var2) {
      for (int var3 = Math.min(var2, var0.length() - 1); var3 >= 0; var3--) {
         if (var1.indexOf(var0.charAt(var3)) >= 0) {
            return var3;
         }
      }

      return -1;
   }

   public static String[] splitInTwo(String var0, String var1) {
      int var2 = var0.indexOf(var1);
      return var2 < 0 ? new String[]{var0, ""} : new String[]{var0.substring(0, var2), var0.substring(var2)};
   }

   public static String insertAt(String var0, String var1, String var2) {
      String[] var3 = splitInTwo(var0, var2);
      return var3[0] + var1 + var3[1];
   }

   public static String ensureEndsWith(String var0, String var1) {
      return var0.endsWith(var1) ? var0 : var0 + var1;
   }

   public static String toHexString(byte var0) {
      return String.format("%02x", var0 & 255);
   }

   public static String toHexString(byte[] var0) {
      StringBuilder var1 = new StringBuilder(var0.length);

      for (byte var5 : var0) {
         var1.append(toHexString(var5));
      }

      return var1.toString();
   }

   public static String toHexString(byte[] var0, int var1, int var2) {
      if (var1 == 0 && var2 == var0.length) {
         return toHexString(var0);
      }

      StringBuilder var3 = new StringBuilder(var2);
      int var4 = var1;

      for (int var5 = var1 + var2; var4 < var5; var4++) {
         var3.append(toHexString(var0[var4]));
      }

      return var3.toString();
   }

   public static int positionOfLastNoneWhitespaceBefore(String var0, int var1) {
      for (int var2 = var1 - 1; var2 >= 0; var2--) {
         char var3 = var0.charAt(var2);
         if (!Character.isWhitespace(var3)) {
            return var2;
         }
      }

      return -1;
   }

   public static boolean hasXmlEscape(String var0) {
      return var0.indexOf(38) >= 0 && var0.indexOf(59) > 0;
   }

   public static String substringAfterIfPresent(String var0, String var1) {
      int var2 = var0.indexOf(var1);
      return var2 == -1 ? var0 : var0.substring(var2 + var1.length());
   }

   public static String substringAfterLastIfPresent(String var0, String var1) {
      int var2 = var0.lastIndexOf(var1);
      return var2 == -1 ? var0 : var0.substring(var2 + var1.length());
   }

   private KongaStringUtils() {
   }

   private static final class NaturalOrder implements Comparator<String>, Serializable {
      private NaturalOrder() {
      }

      public int compare(String var1, String var2) {
         return var1.compareTo(var2);
      }

      @Override
      public String toString() {
         return "Natural order";
      }
   }
}
