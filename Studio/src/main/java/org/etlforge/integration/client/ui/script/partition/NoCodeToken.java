package org.etlforge.integration.client.ui.script.partition;

import org.etlforge.ui.text.partition.Token;

class NoCodeToken implements Token {
   private String lexeme;
   private int offset;

   @Override
   public String lexeme() {
      return this.lexeme;
   }

   @Override
   public boolean matches(String var1, int var2) {
      int var3 = org.etlforge.integration.data.script.NativeJavaScript.nextBlock(var1, var2);
      if (var3 != -1 && var3 <= var2) {
         this.offset = -1;
         this.lexeme = null;
         return false;
      } else {
         this.offset = var2;
         this.lexeme = var3 >= 0 ? var1.substring(var2, var3) : var1.substring(var2);
         return true;
      }
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
   public String toString() {
      return this.lexeme + " @" + this.offset;
   }
}
