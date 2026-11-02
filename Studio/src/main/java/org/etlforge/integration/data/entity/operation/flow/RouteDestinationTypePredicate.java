package org.etlforge.integration.data.entity.operation.flow;

import com.google.common.base.Predicate;

public final class RouteDestinationTypePredicate implements Predicate<Route> {
   private final Class<? extends OperationActivityWrapper> clazz;

   public RouteDestinationTypePredicate(Class<? extends OperationActivityWrapper> var1) {
      this.clazz = var1;
   }

   public boolean apply(Route var1) {
      return this.clazz.isInstance(var1.getTo());
   }
}
