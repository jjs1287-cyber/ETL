package org.etlforge.integration.data.entity.operation.flow;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

public final class RoutePredicates {
   public static final Predicate<Route> FROM_SCRIPT_SOURCE = new Predicate<Route>() {
      public boolean apply(Route var1) {
         return var1.getSourceActivity() != null;
      }
   };

   public static Predicate<Route> toOperation(RouteType var0) {
      return Predicates.and(var0, new RouteDestinationTypePredicate(OperationWrapper.class));
   }

   public static Predicate<Route> toEmail(RouteType var0) {
      return Predicates.and(var0, new RouteDestinationTypePredicate(EmailWrapper.class));
   }

   private RoutePredicates() {
   }
}
