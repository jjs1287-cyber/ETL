package org.etlforge.integration.salesforce.parser;

import org.apache.commons.lang.StringUtils;
import org.etlforge.lang.KongaStringUtils;

final class SubQueryToken implements SoqlToken {
   private int offset;
   private String lexeme;

   @Override
   public SoqlTokenId getId() {
      return SoqlTokenId.SUB_QUERY;
   }

   @Override
   public boolean matches(String var1, int var2) {
      if (var1.charAt(var2) == '(') {
         int var3 = this.locateEnd(var1, var2 + 1);
         if (var3 >= 0) {
            String var4 = var1.substring(var2 + 1, var3);
            if (StringUtils.isNotBlank(var4)) {
               SoqlTokenScanner var5 = new SoqlTokenScanner(var4);
               SoqlTokenScanner.Result var6 = var5.doScan();
               if (var6.getLocationOfError() == -1) {
                  this.offset = var2;
                  this.lexeme = var1.substring(var2, var3 + 1);
                  return true;
               }
            }
         }
      }

      this.offset = -1;
      this.lexeme = null;
      return false;
   }

   private int locateEnd(String var1, int var2) {
      SubQueryToken.EndLocator var3 = new SubQueryToken.EndLocator(var1, var2);
      return var3.locateEnd();
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

   public ParsedQuery parse(SoqlParser var1) {
      String var2 = KongaStringUtils.removeStartAndEnd(this.lexeme, "(", ")");
      return var1.parse(var2);
   }

   private static class EndLocator {
      private int level;
      private final String input;
      private int position;
      private boolean escape;
      private boolean insideString;

      public EndLocator(String var1, int var2) {
         this.input = var1;
         this.position = var2;
         this.level = 1;
      }

      public int locateEnd() {
         while (true) {
            label24: {
               if (this.position < this.input.length()) {
                  char var1 = this.input.charAt(this.position);
                  if (this.insideString) {
                     this.handleCharInsideString(var1);
                     break label24;
                  }

                  if (!this.isClosingParantheses(var1)) {
                     break label24;
                  }
               }

               return this.level == 0 ? this.position : -1;
            }

            this.position++;
         }
      }

      private void handleCharInsideString(char var1) {
         if (var1 == '\\') {
            this.escape = !this.escape;
         } else {
            if (var1 == '\'' && !this.escape) {
               this.insideString = false;
            }

            this.escape = false;
         }
      }

      private boolean isClosingParantheses(char var1) {
         if (var1 == '(') {
            this.level++;
         } else if (var1 == ')') {
            this.level--;
            if (this.level == 0) {
               return true;
            }
         } else if (var1 == '\'') {
            this.insideString = true;
            this.escape = false;
         }

         return false;
      }
   }
}
