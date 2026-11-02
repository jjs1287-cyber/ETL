package org.etlforge.ui.text.contentassist;

import javax.swing.text.Document;

public class StringValueHistoryCompletionContext implements CompletionContext {
   private final StringValueHistory history;
   private final TokenListCompletionContext delegate;

   public StringValueHistoryCompletionContext(StringValueHistory var1) {
      this.history = var1;
      this.delegate = new TokenListCompletionContext(var1.values());
   }

   @Override
   public CompletionToken[] getAvailableTokens(Document var1, int var2) {
      return this.delegate.getAvailableTokens(var1, var2);
   }

   @Override
   public boolean delete(CompletionToken var1) {
      boolean var2 = this.delegate.delete(var1);
      if (var2) {
         this.history.delete(var1.getDisplayValue());
      }

      return var2;
   }

   public void setCaseSensitive(boolean var1) {
      this.delegate.setCaseSensitive(var1);
   }

   public void setTokenDecorator(TokenDecorator var1) {
      this.delegate.setTokenDecorator(var1);
   }

   public void refresh() {
      this.delegate.setTokens(this.history.values());
   }
}
