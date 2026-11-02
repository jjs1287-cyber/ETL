package org.etlforge.integration.data.entity.operation.flow;

import com.google.common.base.Function;

public final class RouteFunctions {
   public static final Function<Route, OperationWrapper> FROM = new Function<Route, OperationWrapper>() {
      public OperationWrapper apply(Route var1) {
         return var1.getFrom();
      }
   };

   private RouteFunctions() {
   }
}
