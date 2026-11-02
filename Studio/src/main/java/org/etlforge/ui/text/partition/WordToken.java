package org.etlforge.ui.text.partition;

import java.text.BreakIterator;

public class WordToken implements Token {
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
      BreakIterator var3 = BreakIterator.getWordInstance();
      String var4 = var1.substring(var2, var1.length());
      var3.setText(var4);
      int var5 = var3.first();
      int var6 = var3.next();
      if (var6 == -1) {
         this.offset = -1;
         this.lexeme = null;
         return false;
      } else {
         this.offset = var2;
         this.lexeme = var4.substring(var5, var6);
         return true;
      }
   }

   @Override
   public String toString() {
      return this.lexeme + " @" + this.offset;
   }
}
