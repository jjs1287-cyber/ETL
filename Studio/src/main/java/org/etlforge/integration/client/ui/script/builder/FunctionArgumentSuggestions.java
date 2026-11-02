package org.etlforge.integration.client.ui.script.builder;

import com.google.common.collect.Maps;
import java.util.Map;
import org.etlforge.integration.client.ui.script.text.FunctionArgumentSuggestion;
import org.etlforge.integration.data.script.Function;

public final class FunctionArgumentSuggestions {
   private final Map<String, FunctionArgumentSuggestion> suggestions = Maps.newHashMap();

   public void addSuggestion(FunctionArgumentSuggestion var1) {
      for (String var5 : var1.getFunctionNames()) {
         this.suggestions.put(var5.toLowerCase(), var1);
      }
   }

   public FunctionArgumentSuggestion getSuggestion(Function var1) {
      FunctionArgumentSuggestion var2 = this.suggestions.get(var1.getName().toLowerCase());
      if (var2 == null) {
         var2 = FunctionArgumentSuggestion.NONE;
      }

      return var2;
   }
}
