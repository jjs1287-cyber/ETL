package org.etlforge.integration.salesforce.parser;

import org.etlforge.ui.text.partition.OneOfToken;

final class OneOfSoqlToken extends OneOfToken implements SoqlToken {
   private final SoqlTokenId id;

   public OneOfSoqlToken(SoqlTokenId var1, String[] var2) {
      this(var1, var2, true);
   }

   public OneOfSoqlToken(SoqlTokenId var1, String[] var2, boolean var3) {
      super(var2, false, var3);
      this.id = var1;
   }

   @Override
   public SoqlTokenId getId() {
      return this.id;
   }
}
