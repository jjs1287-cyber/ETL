package org.etlforge.integration.client.ui.script.text;

import javax.swing.text.JTextComponent;
import org.etlforge.integration.data.script.Function;
import org.etlforge.ui.text.contentassist.CompletionToken;

public interface FunctionArgumentSuggestion {
   FunctionArgumentSuggestion NONE = new FunctionArgumentSuggestion() {
      @Override
      public String[] getFunctionNames() {
         return new String[0];
      }

      @Override
      public CompletionToken[] getCompletionTokens(Function var1, int var2) {
         return new CompletionToken[0];
      }

      @Override
      public void insertArgument(Function var1, JTextComponent var2) {
      }
   };

   String[] getFunctionNames();

   CompletionToken[] getCompletionTokens(Function var1, int var2);

   void insertArgument(Function var1, JTextComponent var2);
}
