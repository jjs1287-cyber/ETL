package org.etlforge.integration.salesforce.parser;

import com.google.common.collect.Sets;
import java.util.Set;

final class AggregateFunctionToken implements SoqlToken {
   private static final Set<String> FUNCTIONS = Sets.newHashSet("avg", "count", "count_distinct", "min", "max", "sum");
   private int offset;
   private String lexeme;

   @Override
   public SoqlTokenId getId() {
      return SoqlTokenId.AGGREGATE_FUNCTION;
   }

   @Override
   public String lexeme() {
      return this.lexeme;
   }

   @Override
   public int offset() {
      return this.offset;
   }

   @Override
   public boolean matches(String var1, int var2) {
      this.reset();
      int var3 = var1.indexOf(40, var2);
      if (var3 == -1) {
         return false;
      }

      String var4 = var1.substring(var2, var3).toLowerCase();
      if (!FUNCTIONS.contains(var4)) {
         return false;
      }

      int var5 = var1.indexOf(41, var3 + 1);
      if (var5 == -1) {
         return false;
      }

      boolean var6 = false;
      if (var5 == var3 + 1) {
         var6 = var4.equals("count");
      } else {
         var6 = this.isArgumentPossibleFieldName(var1, var3 + 1, var5);
      }

      if (var6) {
         this.offset = var2;
         this.lexeme = var1.substring(var2, var5 + 1);
      }

      return var6;
   }

   private void reset() {
      this.offset = -1;
      this.lexeme = null;
   }

   private boolean isArgumentPossibleFieldName(String var1, int var2, int var3) {
      assert var3 - var2 > 0 : "This method should not be called for empty argument";
      String var4 = var1.substring(var2, var3).trim();
      FieldToken var5 = new FieldToken();
      return var5.matches(var4, 0) ? var4.length() == var5.lexeme().length() : false;
   }

   @Override
   public boolean isWhitespace() {
      return false;
   }
}
