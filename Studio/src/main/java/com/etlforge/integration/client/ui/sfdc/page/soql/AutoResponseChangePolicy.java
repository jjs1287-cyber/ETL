package com.etlforge.integration.client.ui.sfdc.page.soql;

import org.etlforge.integration.salesforce.parser.ParsedQuery;

public final class AutoResponseChangePolicy implements SoqlChangePolicy {
   @Override
   public void verifyChange(ParsedQuery var1) throws EditSoqlException {
      if (!var1.hasFieldInfo()) {
         throw new EditSoqlException("Not Allowed", "This change is not allowed. You need to create a new Query.");
      }
   }
}
