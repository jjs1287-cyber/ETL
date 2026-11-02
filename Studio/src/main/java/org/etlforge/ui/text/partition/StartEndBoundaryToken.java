package org.etlforge.ui.text.partition;

public class StartEndBoundaryToken implements Token {
   private final String startDelim;
   private final int startLength;
   private final String endDelim;
   private final int endLength;
   private String lexeme;
   private int offset;

   public StartEndBoundaryToken(String var1, String var2) {
      this.startDelim = var1;
      this.startLength = var1.length();
      this.endDelim = var2;
      this.endLength = var2.length();
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

   @Override
   public boolean matches(String var1, int var2) {
      if (var1.length() - var2 >= this.startLength && var1.substring(var2, var2 + this.startLength).equals(this.startDelim)) {
         this.lexeme = this.extractLexeme(var1, var2 + this.startLength);
         this.offset = var2;
         return true;
      } else {
         this.lexeme = null;
         this.offset = -1;
         return false;
      }
   }

   private String extractLexeme(String var1, int var2) {
      int var3 = var1.indexOf(this.endDelim, var2);
      return var3 == -1 ? var1.substring(var2 - this.startLength) : var1.substring(var2 - this.startLength, var3 + this.endLength);
   }

   @Override
   public String toString() {
      return this.lexeme() + " @" + this.offset();
   }
}
