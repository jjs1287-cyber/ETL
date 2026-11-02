package org.etlforge.integration.data.entity.operation.flow;

import com.google.common.base.Predicate;

public enum RouteType implements Predicate<Route> {
   SUCCESS("Success"),
   FAILURE("Failure"),
   SIBLING("Sibling"),
   SCRIPTED("Scripted");

   private final String display;

   RouteType(String var3) {
      this.display = var3;
   }

   public boolean apply(Route var1) {
      return var1 != null && var1.getType() == this;
   }

   @Override
   public String toString() {
      return this.display;
   }
}
