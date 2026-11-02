package org.etlforge.integration.salesforce.parser;

import com.google.common.base.Preconditions;
import org.etlforge.system.ArgCheck;

public final class FixedObjectPolicy implements SupportedQueryPolicy {
   private final String objectName;
   private boolean valid;

   public FixedObjectPolicy(String var1) {
      this.objectName = ArgCheck.checkNotBlank(var1);
   }

   @Override
   public boolean isSupported(ParsedQuery var1) {
      this.valid = this.objectName.equalsIgnoreCase(var1.getObjectName());
      return this.valid;
   }

   @Override
   public String whyNot() {
      Preconditions.checkState(!this.valid, "whyNot should not be called for a valid query.");
      return "Can only query " + this.objectName + ".";
   }
}
