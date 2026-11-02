package org.etlforge.application.ui.widget;

import javax.swing.text.JTextComponent;
import org.etlforge.system.Disposable;
import org.etlforge.ui.input.TextInputField;
import org.etlforge.ui.text.contentassist.AutoCompleter;
import org.etlforge.ui.text.contentassist.CompletionContext;
import org.etlforge.ui.text.contentassist.CompletionToken;
import org.etlforge.ui.text.contentassist.StringValueHistory;
import org.etlforge.ui.text.contentassist.TokenListCompletionContext;
import org.etlforge.ui.text.contentassist.TokenListCompletionSupport;

public final class TextFieldCompletionSupport extends TokenListCompletionSupport implements Disposable {
   private final StringValueHistory history;

   public TextFieldCompletionSupport(TextInputField var1, StringValueHistory var2) {
      this(var1.getInputComponent(), var2);
   }

   public TextFieldCompletionSupport(JTextComponent var1, StringValueHistory var2) {
      super(var1, var2.values());
      this.history = var2;
      this.setCaseSensitive(false);
   }

   @Override
   public void refresh() {
      this.setTokens(this.history.values());
   }

   @Override
   protected TokenListCompletionContext createCompletionContext(String[] var1) {
      return new TokenListCompletionContext(var1) {
         @Override
         public boolean delete(CompletionToken var1) {
            boolean var2 = super.delete(var1);
            if (var2) {
               TextFieldCompletionSupport.this.history.delete(var1.getDisplayValue());
            }

            return var2;
         }
      };
   }

   @Override
   protected AutoCompleter createAutoCompleter(CompletionContext var1) {
      return AutoCompleterFactory.defaultFactory().createAutoCompleter(var1);
   }
}
