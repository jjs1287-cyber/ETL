package org.etlforge.integration.salesforce.parser;

import com.google.common.base.Preconditions;

public final class NoAggregateFunctionsPolicy implements SupportedQueryPolicy {
   private boolean valid;

   @Override
   public boolean isSupported(ParsedQuery var1) {
      this.valid = !var1.callsAggregateFunction();
      return this.valid;
   }

   @Override
   public String whyNot() {
      Preconditions.checkState(!this.valid, "whyNot should not be called for a valid query.");
      return "Aggregate functions are not supported.";
   }
}
