package org.etlforge.ui.text.partition;

public class StringLiteralToken implements Token {
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
      return false;
   }

   @Override
   public boolean matches(String var1, int var2) {
      char var3 = var1.charAt(var2);
      if (var3 != '"' && var3 != '\'') {
         return false;
      }

      this.lexeme = this.extractLexeme(var1, var2 + 1, var3);
      this.offset = var2;
      return true;
   }

   private String extractLexeme(String var1, int var2, char var3) {
      boolean var4 = false;
      int var5 = var2;

      for (int var6 = var1.length(); var5 < var6; var5++) {
         char var7 = var1.charAt(var5);
         if (var7 == var3) {
            if (!var4) {
               return var1.substring(var2 - 1, var5 + 1);
            }

            var4 = false;
         } else if (var7 == '\\') {
            var4 = !var4;
         } else {
            var4 = false;
         }
      }

      return var1.substring(var2 - 1);
   }

   @Override
   public String toString() {
      return this.lexeme + " @" + this.offset;
   }
}
