package org.etlforge.ui.text.partition;

public class WhitespaceToken implements Token {
   private String lexeme;
   private int offset;

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
      return true;
   }

   @Override
   public boolean matches(String var1, int var2) {
      char var3 = var1.charAt(var2);
      if (Character.isWhitespace(var3)) {
         this.lexeme = this.extractLexeme(var1, var2 + 1);
         this.offset = var2;
         return true;
      } else {
         return false;
      }
   }

   private String extractLexeme(String var1, int var2) {
      int var3 = var2;

      for (int var4 = var1.length(); var3 < var4; var3++) {
         if (!Character.isWhitespace(var1.charAt(var3))) {
            return var1.substring(var2 - 1, var3);
         }
      }

      return var1.substring(var2 - 1);
   }

   @Override
   public String toString() {
      return this.lexeme + " @" + this.offset;
   }
}
