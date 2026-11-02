package org.etlforge.integration.client.ui.interchange;

import org.etlforge.ui.text.contentassist.CompletionToken;
import org.etlforge.ui.text.contentassist.DefaultCompletionToken;

public class DefaultPredefinedVariable implements PredefinedVariable {
   private final String name;
   private final String htmlInfo;

   public DefaultPredefinedVariable(String var1, String var2) {
      this.name = var1;
      this.htmlInfo = var2;
   }

   @Override
   public CompletionToken asCompletionToken(int var1, int var2) {
      DefaultCompletionToken var3 = new DefaultCompletionToken(this.name, var1, var2);
      var3.setInfo(this.htmlInfo);
      return var3;
   }

   @Override
   public String getName() {
      return this.name;
   }
}
