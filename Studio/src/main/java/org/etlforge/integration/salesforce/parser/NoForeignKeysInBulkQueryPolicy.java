package org.etlforge.integration.salesforce.parser;

public final class NoForeignKeysInBulkQueryPolicy implements SupportedQueryPolicy {
   @Override
   public boolean isSupported(ParsedQuery var1) {
      return !var1.isChildToParentQuery();
   }

   @Override
   public String whyNot() {
      return "Foreign Key Relationships not supported in Bulk Query.";
   }
}
