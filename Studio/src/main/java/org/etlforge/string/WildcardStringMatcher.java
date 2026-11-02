package org.etlforge.string;

import com.google.common.collect.Maps;
import java.io.Serializable;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class WildcardStringMatcher implements StringMatcher, Serializable {
   private final boolean caseSensitive;
   private Map<String, Pattern> cache;
   private boolean leadingMatchSufficient = true;

   public WildcardStringMatcher() {
      this(true);
   }

   public WildcardStringMatcher(boolean var1) {
      this.caseSensitive = var1;
   }

   public void setLeadingMatchSufficient(boolean var1) {
      this.leadingMatchSufficient = var1;
   }

   @Override
   public boolean matches(String var1, String var2) {
      String var3 = this.caseSensitive ? var2 : var2.toLowerCase();
      Pattern var4 = this.getPattern(var1);
      Matcher var5 = var4.matcher(var3);
      if (var5.find(0)) {
         int var6 = var5.start();
         return var6 >= 0;
      } else {
         return false;
      }
   }

   public void setCachingEnabled(boolean var1) {
      if (var1) {
         if (this.cache == null) {
            this.cache = Maps.newHashMap();
         }
      } else {
         this.cache = null;
      }
   }

   private Pattern getPattern(String var1) {
      if (!this.caseSensitive) {
         var1 = var1.toLowerCase();
      }

      if (this.cache != null) {
         Pattern var2 = this.cache.get(var1);
         if (var2 != null) {
            return var2;
         }
      }

      return this.createPattern(var1);
   }

   private Pattern createPattern(String var1) {
      String var2 = this.createRegEx(var1);
      Pattern var3 = Pattern.compile(var2, 32);
      if (this.cache != null) {
         this.cache.put(var1, var3);
      }

      return var3;
   }

   private String createRegEx(String var1) {
      boolean var2 = false;
      if (this.leadingMatchSufficient) {
         if (var1.endsWith(" ")) {
            var1 = var1.substring(0, var1.length() - 1);
            var2 = true;
         }
      } else {
         var2 = true;
      }

      StringBuilder var3 = new StringBuilder("\\A");
      StringTokenizer var4 = new StringTokenizer(var1, "*?", true);

      while (var4.hasMoreTokens()) {
         String var5 = var4.nextToken();
         char var6 = var5.charAt(0);
         if (var6 == '*') {
            var3.append(".*");
         } else if (var6 == '?') {
            var3.append(".");
         } else {
            var3.append(Pattern.quote(var5));
         }
      }

      if (var2) {
         var3.append("\\z");
      }

      return var3.toString();
   }
}
