package org.etlforge.integration.data.location;

import org.etlforge.integration.data.validation.ValidationIssue;
import org.etlforge.integration.data.validation.ValidationMessageCollector;

public abstract class AbstractLocationValidator {
   private ValidationMessageCollector collector;

   public final void setMessageCollector(ValidationMessageCollector var1) {
      this.collector = var1;
   }

   protected final void collectMessages(Iterable<? extends ValidationIssue> var1) {
      if (this.collector != null) {
         for (ValidationIssue var3 : var1) {
            var3.report(this.collector);
         }
      }
   }
}
