package org.etlforge.integration.client.ui.script.text;

import javax.swing.text.JTextComponent;
import org.etlforge.integration.client.ui.script.Icons;
import org.etlforge.integration.client.ui.script.builder.FunctionArgumentSuggestions;
import org.etlforge.integration.data.script.Function;
import org.etlforge.integration.data.script.FunctionInserter;
import org.etlforge.integration.data.script.FunctionRegistry;
import org.etlforge.ui.text.contentassist.CompletionToken;

public final class DefaultFunctionInserter implements FunctionInserter {
   private final Function function;
   private FunctionArgumentSuggestions argumentSuggestions;

   public DefaultFunctionInserter(Function var1) {
      this.function = var1;
   }

   public void setArgumentSuggestions(FunctionArgumentSuggestions var1) {
      this.argumentSuggestions = var1;
   }

   @Override
   public void insertFunction(JTextComponent var1) {
      var1.replaceSelection("");
      int var2 = var1.getCaretPosition();
      CompletionToken var3 = this.getCompletionToken(var2, var2, var1);
      var3.complete(var1.getDocument());
   }

   @Override
   public CompletionToken getCompletionToken(int var1, int var2, JTextComponent var3) {
      FunctionCompletionToken var4 = new FunctionCompletionToken(this.function, var1, var2, var3);
      if (this.argumentSuggestions != null) {
         var4.setArgumentSuggestion(this.argumentSuggestions.getSuggestion(this.function));
      }

      String var5 = FunctionRegistry.getRegistry().getFunctionInfo(this.function.getName());
      var4.setInfo(var5);
      var4.setIcon(Icons.FORMULA_ICON);
      return var4;
   }
}
