package org.etlforge.integration.data.entity.operation.flow;

public final class RouteInsertedChange extends FlowChange {
   private final Route route;

   public RouteInsertedChange(Route var1) {
      super(3);
      this.route = var1;
   }

   @Override
   public void dispatchTo(FlowChangeProcessor var1) {
      var1.process(this);
   }

   public Route getInsertedRoute() {
      return this.route;
   }

   @Override
   public String toString() {
      return this.route.toString();
   }
}
