package com.etlforge.integration.client.ui.sfdc.page.soql;

import com.google.common.base.Preconditions;
import org.etlforge.integration.salesforce.parser.ParsedQuery;

public final class CannotChangeFieldListPolicy implements SoqlChangePolicy {
   private final ParsedQuery originalQueryInfo;

   public CannotChangeFieldListPolicy(ParsedQuery var1) {
      this.originalQueryInfo = Preconditions.checkNotNull(var1);
   }

   @Override
   public void verifyChange(ParsedQuery var1) throws EditSoqlException {
      if (this.originalQueryInfo.hasFieldInfo() && !this.originalQueryInfo.hasSameFieldsAs(var1)) {
         throw new EditSoqlException("Not Supported", "The current version only supports changing the WHERE clause and/or LIMIT.");
      }
   }
}
