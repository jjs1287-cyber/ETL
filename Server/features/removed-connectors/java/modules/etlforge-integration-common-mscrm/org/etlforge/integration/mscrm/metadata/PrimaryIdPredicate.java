package org.etlforge.integration.mscrm.metadata;

import com.google.common.base.Predicate;
import java.io.Serializable;

public final class PrimaryIdPredicate implements Predicate<MSCrmAttribute>, Serializable {
   private final String name;

   public PrimaryIdPredicate(MSCrmObject var1) {
      this.name = var1.getPrimaryIdAttribute();
   }

   public boolean apply(MSCrmAttribute var1) {
      return var1.getName().equals(this.name);
   }
}
