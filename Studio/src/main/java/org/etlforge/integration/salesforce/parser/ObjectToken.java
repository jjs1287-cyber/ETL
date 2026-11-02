package org.etlforge.integration.salesforce.parser;

import com.google.common.base.Optional;

final class ObjectToken implements SoqlToken {
   private String lexeme;
   private int offset;

   @Override
   public SoqlTokenId getId() {
      return SoqlTokenId.OBJECT;
   }

   @Override
   public boolean matches(String var1, int var2) {
      Optional var3 = NameToken.matches(var1, var2);
      if (var3.isPresent()) {
         this.lexeme = (String)var3.get();
         this.offset = var2;
         return true;
      } else {
         this.lexeme = null;
         this.offset = -1;
         return false;
      }
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
   public boolean isWhitespace() {
      return false;
   }
}
