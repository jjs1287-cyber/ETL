package org.etlforge.ui.text.partition;

public abstract class AbstractTokenScanner implements TokenScanner {
   private Token whitespaceToken;
   private String input;
   private int offset;
   private Token lastToken;

   public AbstractTokenScanner(String var1) {
      this.input = var1;
   }

   @Override
   public void setWhitespaceAware(boolean var1) {
      this.whitespaceToken = var1 ? new WhitespaceToken() : null;
   }

   @Override
   public void setText(String var1) {
      this.input = var1;
   }

   @Override
   public void setPosition(int var1) {
      this.offset = var1;
   }

   protected final int getPosition() {
      return this.offset;
   }

   protected final Token getLastToken() {
      return this.lastToken;
   }

   @Override
   public Token next() {
      if (this.offset >= this.input.length()) {
         return null;
      }

      if (this.whitespaceToken == null) {
         while (Character.isWhitespace(this.input.charAt(this.offset))) {
            this.offset++;
            if (this.offset == this.input.length()) {
               return null;
            }
         }
      } else if (this.whitespaceToken.matches(this.input, this.offset)) {
         this.offset = this.offset + this.whitespaceToken.lexeme().length();
         this.lastToken = this.whitespaceToken;
         return this.whitespaceToken;
      }

      for (Token var4 : this.getPossibleTokens()) {
         if (var4.matches(this.input, this.offset)) {
            this.lastToken = var4;
            this.offset = this.offset + var4.lexeme().length();
            return var4;
         }
      }

      return null;
   }

   protected abstract Token[] getPossibleTokens();

   protected final String getInput() {
      return this.input;
   }
}
