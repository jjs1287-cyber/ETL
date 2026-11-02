package org.etlforge.integration.salesforce.parser;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.etlforge.integration.dataelement.DataElementResolver;

final class RegexSoqlParser implements SoqlParser {
   private final Pattern objectNamePattern = Pattern.compile("select(.+)\\s+from\\s+([^\\s]+).*", 10);
   private final Pattern aggregateFunctionPattern;

   public RegexSoqlParser() {
      String var1 = composeAggregateFunctionRegEx();
      this.aggregateFunctionPattern = Pattern.compile(var1, 10);
   }

   private static String composeAggregateFunctionRegEx() {
      String var0 = getAggregateFunctionRegex("count", false);
      StringBuilder var1 = new StringBuilder(var0);

      for (String var5 : new String[]{"avg", "count_distinct", "min", "max", "sum"}) {
         String var6 = getAggregateFunctionRegex(var5, true);
         var1.append("|").append(var6);
      }

      return var1.toString();
   }

   private static String getAggregateFunctionRegex(String var0, boolean var1) {
      String var2 = var1 ? "+" : "*";
      return var0 + "\\([^\\(]" + var2 + "?\\)";
   }

   boolean usesAggregateFunction(String var1) {
      String var2 = this.prepareQuery(var1);
      return this.usesAggegateFunctionsImpl(var2);
   }

   private boolean usesAggegateFunctionsImpl(String var1) {
      Matcher var2 = this.aggregateFunctionPattern.matcher(var1);
      if (var2.find()) {
         int var3 = var2.start();
         int var4 = var1.toLowerCase().indexOf("from");
         return var3 < var4;
      } else {
         return false;
      }
   }

   @Override
   public String extractObjectName(String var1) {
      String var2 = this.prepareQuery(var1);
      if (this.usesAggegateFunctionsImpl(var2)) {
         return "AggregateResult";
      }

      Matcher var3 = this.objectNamePattern.matcher(var2);
      return var3.find() ? var3.group(2) : null;
   }

   private String prepareQuery(String var1) {
      String var2 = DataElementResolver.resolveDefaultValues(var1);
      return var2.replace('\n', ' ').replace("\\r", "");
   }

   @Override
   public ParsedQuery parse(String var1) {
      String var2 = this.prepareQuery(var1);
      if (this.usesAggegateFunctionsImpl(var2)) {
         return new ParsedQuery("AggregateResult");
      } else {
         Matcher var3 = this.objectNamePattern.matcher(var2);
         if (var3.find()) {
            String var4 = var3.group(2);
            String var5 = var3.group(1);
            List var6 = this.extractFieldList(var5);
            return new ParsedQuery(var4, var6);
         } else {
            return ParsedQuery.invalid("Invalid query.");
         }
      }
   }

   private List<String> extractFieldList(String var1) {
      ArrayList var2 = Lists.newArrayList();
      String[] var3 = var1.trim().split(",");

      for (String var7 : var3) {
         String var8 = var7.trim();
         if (var8.contains("(") || var8.contains(" ")) {
            return null;
         }

         var2.add(var8);
      }

      return var2;
   }
}
