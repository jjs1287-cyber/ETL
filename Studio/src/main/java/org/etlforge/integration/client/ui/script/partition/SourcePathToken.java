package org.etlforge.integration.client.ui.script.partition;

import org.etlforge.integration.structure.mapping.PathSeparator;
import org.etlforge.ui.text.partition.Token;

class SourcePathToken implements Token {
   private String lexeme;
   private int offset;

   @Override
   public String lexeme() {
      return this.lexeme;
   }

   @Override
   public boolean isWhitespace() {
      return false;
   }

   @Override
   public boolean matches(String var1, int var2) {
      if (Character.isJavaIdentifierStart(var1.charAt(var2))) {
         int var3 = this.getEndPosition(var1, var2 + 1);
         this.lexeme = var1.substring(var2, var3);
         this.offset = var2;
         return true;
      } else {
         this.lexeme = null;
         this.offset = -1;
         return false;
      }
   }

   @Override
   public int offset() {
      return this.offset;
   }

   private int getEndPosition(String var1, int var2) {
      int var3 = var2;

      for (int var4 = var1.length(); var3 < var4; var3++) {
         char var5 = var1.charAt(var3);
         if (!Character.isJavaIdentifierPart(var5) && !PathSeparator.isSeparator(var5)) {
            return var3;
         }
      }

      return var1.length();
   }

   @Override
   public String toString() {
      return this.lexeme + " @" + this.offset;
   }
}
