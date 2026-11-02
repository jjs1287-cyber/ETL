package org.etlforge.integration.client.ui.entity.search;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.string.RegExPredicate;
import org.etlforge.string.StringPredicates;

public final class SearchOptions {
   private final boolean caseSensitive;
   private final boolean regex;
   private final SearchFieldCombineMode fieldCombineMode;
   private final boolean emptyStringMatchesAll;

   public SearchOptions(boolean var1, boolean var2, SearchFieldCombineMode var3, boolean var4) {
      this.caseSensitive = var1;
      this.regex = var2;
      this.fieldCombineMode = var3;
      this.emptyStringMatchesAll = var4;
   }

   public Predicate<String> getPredicate(String var1) {
      if (var1.isEmpty()) {
         return this.emptyStringMatchesAll ? Predicates.alwaysTrue() : Predicates.equalTo(var1);
      } else if (this.regex) {
         int var2 = this.caseSensitive ? 0 : 2;
         return new RegExPredicate(var1, false, var2);
      } else {
         return this.caseSensitive ? StringPredicates.contains(var1) : StringPredicates.containsIgnoreCase(var1);
      }
   }

   public Predicate<IntegrationEntity> getCombinedSearch(Iterable<Predicate<IntegrationEntity>> var1) {
      switch (this.fieldCombineMode) {
         case AND:
            return Predicates.and(var1);
         case OR:
            return Predicates.or(var1);
         default:
            throw new AssertionError("Unexpected field combine mode: " + this.fieldCombineMode);
      }
   }

   public String replaceValue(String var1, String var2, String var3) {
      if (!var2.isEmpty() || !this.emptyStringMatchesAll && !var1.isEmpty()) {
         String var4;
         if (this.regex) {
            int var5 = this.caseSensitive ? 0 : 2;
            Pattern var6 = Pattern.compile(var2, var5);
            Matcher var7 = var6.matcher(var1);
            var4 = var7.replaceAll(Matcher.quoteReplacement(var3));
         } else if (this.caseSensitive) {
            var4 = var1.replace(var2, var3);
         } else {
            Pattern var8 = Pattern.compile(Pattern.quote(var2), 66);
            var4 = var8.matcher(var1).replaceAll(Matcher.quoteReplacement(var3));
         }

         return var4;
      } else {
         return var3;
      }
   }

   public String getOldValueRepresentation(String var1) {
      if (var1.isEmpty()) {
         return this.emptyStringMatchesAll ? "[anything]" : "[empty]";
      }

      String var2 = var1;
      if (this.regex) {
         var2 = var2 + " [regex]";
      }

      if (this.caseSensitive) {
         var2 = var2 + " [case-sensitive]";
      }

      return var2;
   }

   @Override
   public String toString() {
      return "case sensitive: " + this.caseSensitive + ", regex: " + this.regex;
   }
}
