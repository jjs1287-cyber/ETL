package org.etlforge.integration.salesforce.parser;

final class EverythingAfterObjectToken implements SoqlToken {
   private int offset;
   private String lexeme;

   @Override
   public boolean matches(String var1, int var2) {
      this.offset = var2;
      this.lexeme = var1.substring(var2);
      return true;
   }

   @Override
   public SoqlTokenId getId() {
      return SoqlTokenId.EVERYTHING_AFTER_OBJECT;
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
