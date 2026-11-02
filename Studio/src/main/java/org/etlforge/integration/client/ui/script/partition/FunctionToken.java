package org.etlforge.integration.client.ui.script.partition;

import java.util.ArrayList;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.data.script.Function;
import org.etlforge.integration.data.script.FunctionCategory;
import org.etlforge.integration.data.script.FunctionRegistry;
import org.etlforge.ui.text.partition.OneOfToken;
import org.etlforge.ui.text.partition.Token;

class FunctionToken extends OneOfToken implements Token {
   private static final String[] TOKENS = createTokens();

   public FunctionToken() {
      super(TOKENS, false);
   }

   @Override
   public String lexeme() {
      return StringUtils.removeEnd(super.lexeme(), "(");
   }

   private static String[] createTokens() {
      ArrayList var0 = new ArrayList();
      FunctionRegistry var1 = FunctionRegistry.getRegistry();

      for (FunctionCategory var3 : var1.getCategories()) {
         for (Function var5 : var3.getFunctions()) {
            var0.add(var5.getName() + "(");
         }
      }

      for (FunctionCategory category : org.etlforge.integration.data.script.JavaScriptFunctions.categories()) {
         for (Function function : category.getFunctions()) var0.add(function.getName() + "(");
      }

      return (String[]) var0.toArray(new String[var0.size()]);
   }
}
