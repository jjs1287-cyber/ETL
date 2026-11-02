package org.etlforge.integration.salesforce.parser;

import com.google.common.base.CharMatcher;
import com.google.common.collect.Sets;
import java.util.Set;

final class ObjectAliasToken implements SoqlToken {
   private int offset;
   private String lexeme;
   private final Set<String> reserved = Sets.newHashSet("where", "with", "group", "having", "order", "limit", "offset");

   @Override
   public SoqlTokenId getId() {
      return SoqlTokenId.OBJECT_ALIAS;
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
      this.offset = -1;
      this.lexeme = null;
      int var3 = this.locateEnd(var1, var2);
      String var4 = var1.substring(var2, var3);
      if (var4.isEmpty()) {
         return false;
      }

      if (this.reserved.contains(var4.toLowerCase())) {
         return false;
      }

      this.lexeme = var4;
      this.offset = var2;
      return true;
   }

   private int locateEnd(String var1, int var2) {
      CharMatcher var3 = CharMatcher.WHITESPACE;
      int var4 = var3.indexIn(var1, var2);
      if (var4 == -1) {
         var4 = var1.length();
      }

      return var4;
   }

   @Override
   public boolean isWhitespace() {
      return false;
   }
}
