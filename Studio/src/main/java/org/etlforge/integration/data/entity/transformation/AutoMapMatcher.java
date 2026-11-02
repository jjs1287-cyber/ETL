package org.etlforge.integration.data.entity.transformation;

import com.google.common.base.CaseFormat;
import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.etlforge.collections.Collections2;
import org.etlforge.collections.Pair;

final class AutoMapMatcher {
   private final List<HashSet<String>> standardAbbrevs = getStandardAbbreviations();

   private static List<HashSet<String>> getStandardAbbreviations() {
      return Arrays.asList(Sets.newHashSet("number", "no"));
   }

   public boolean isMatch(String var1, String var2) {
      String var3 = var1.toLowerCase();
      String var4 = var2.toLowerCase();
      if (!var3.equals("id") && !var4.equals("id")) {
         boolean var5 = this.doLowerCaseMatching(var3, var4);
         return var5 ? true : this.doSplitMatching(var1, var2);
      } else {
         return var3.equals(var4);
      }
   }

   private boolean doLowerCaseMatching(String var1, String var2) {
      if (var1.equals(var2)) {
         return true;
      } else {
         return !var1.endsWith("__c") && !var2.endsWith("__c") ? false : this.isMatch(this.remove__c(var1), this.remove__c(var2));
      }
   }

   private String remove__c(String var1) {
      return StringUtils.substringBeforeLast(var1, "__c");
   }

   private boolean doSplitMatching(String var1, String var2) {
      if (!var1.endsWith("__c") && !var2.endsWith("__c")) {
         String var3 = toLowerUnderscoreFormat(var1);
         if (var3 == null) {
            return false;
         }

         String var4 = toLowerUnderscoreFormat(var2);
         if (var4 == null) {
            return false;
         }

         List var5 = splitOnUnderscore(var3);
         List var6 = splitOnUnderscore(var4);
         if (var5.size() != var6.size()) {
            return false;
         }

         for (Object __etlItem64 : Collections2.zip(var5, var6)) {
            Pair var8 = (Pair) __etlItem64;
            if (!((String)var8.first).equals(var8.second) && !this.isStandardAbbreviation((String)var8.first, (String)var8.second)) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private static String toLowerUnderscoreFormat(String var0) {
      String var1 = var0.toLowerCase();
      if (var1.contains("_")) {
         return var1;
      }

      String var2 = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, var0);
      return var2.contains("_") ? var2.toLowerCase() : null;
   }

   private static List<String> splitOnUnderscore(String var0) {
      CharMatcher var1 = CharMatcher.is('_');
      return Lists.newArrayList(Splitter.on(var1).split(var0));
   }

   private boolean isStandardAbbreviation(String var1, String var2) {
      for (Set var4 : this.standardAbbrevs) {
         if (var4.contains(var1) && var4.contains(var2)) {
            return true;
         }
      }

      return false;
   }
}
