package org.etlforge.integration.data.project.path;

import com.google.common.base.Predicate;
import java.util.regex.Pattern;
import org.etlforge.string.RegExPredicate;

final class Wildcards {
   static final String MARKER = "\u0001";
   static final String ZERO_OR_MORE = "\u0001*";
   static final String ONE_OR_MORE = "\u0001+";
   static final String ANY_LEVEL = "\u0002";

   public static boolean hasWildcard(String var0) {
      return var0.contains("\u0001*") || var0.contains("\u0001+") || isAnyLevel(var0);
   }

   public static boolean isAnyLevel(String var0) {
      return "\u0002".equals(var0);
   }

   public static Predicate<String> parse(String var0) {
      assert hasWildcard(var0);
      String var1 = createRegEx(var0);
      return new RegExPredicate(var1, 2);
   }

   private static String createRegEx(String var0) {
      StringBuilder var1 = new StringBuilder();
      int var2 = 0;

      while (var2 < var0.length()) {
         int var3 = var0.indexOf("\u0001*", var2);
         int var4 = var0.indexOf("\u0001+", var2);
         if (var3 == -1 && var4 == -1) {
            var1.append(Pattern.quote(var0.substring(var2)));
            break;
         }

         int var5;
         String var6;
         if (var3 != -1 && (var4 < 0 || var4 >= var3)) {
            var5 = var3;
            var6 = ".*";
         } else {
            var5 = var4;
            var6 = ".+";
         }

         if (var5 > var2) {
            var1.append(Pattern.quote(var0.substring(var2, var5)));
         }

         var1.append(var6);
         var2 = var5 + var6.length();
      }

      return var1.toString();
   }
}
