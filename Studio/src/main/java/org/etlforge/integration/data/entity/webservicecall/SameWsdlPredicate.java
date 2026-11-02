package org.etlforge.integration.data.entity.webservicecall;

import com.google.common.base.Predicate;
import org.etlforge.integration.data.entity.WebServiceCall;

public final class SameWsdlPredicate implements Predicate<WebServiceCall> {
   private final String locator;

   public SameWsdlPredicate(WebServiceCall var1) {
      this.locator = var1.getWsdlFile().getLocator();
   }

   public boolean apply(WebServiceCall var1) {
      return equals(this.locator, var1.getWsdlFile().getLocator());
   }

   private static boolean equals(String var0, String var1) {
      return var0 != null ? var0.equals(var1) : var1 == null;
   }
}
