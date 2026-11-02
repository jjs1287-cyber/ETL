package org.etlforge.integration.client.ui.structure.editor.ldap.source;

import org.etlforge.ui.text.partition.AbstractTokenScanner;
import org.etlforge.ui.text.partition.Token;

public class FilterTokenScanner extends AbstractTokenScanner {
   private final Token[] tokens;

   public FilterTokenScanner(FilterExpressionTokens var1) {
      super("");
      this.tokens = var1.getAllTokens();
   }

   @Override
   protected Token[] getPossibleTokens() {
      return this.tokens;
   }
}
