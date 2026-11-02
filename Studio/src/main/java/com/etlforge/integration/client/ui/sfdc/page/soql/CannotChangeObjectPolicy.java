package com.etlforge.integration.client.ui.sfdc.page.soql;

import org.etlforge.integration.salesforce.parser.ParsedQuery;
import org.etlforge.system.ArgCheck;

public final class CannotChangeObjectPolicy implements SoqlChangePolicy {
   private final String objectName;

   public CannotChangeObjectPolicy(String var1) {
      this.objectName = ArgCheck.checkNotEmpty(var1);
   }

   @Override
   public void verifyChange(ParsedQuery var1) throws EditSoqlException {
      String var2 = var1.getObjectName();
      if (!var2.equals(this.objectName)) {
         String var3 = String.format("Changing the queried object from %s to %s is not supported.", this.objectName, var2);
         throw new EditSoqlException("Cannot change object", var3);
      }
   }
}
