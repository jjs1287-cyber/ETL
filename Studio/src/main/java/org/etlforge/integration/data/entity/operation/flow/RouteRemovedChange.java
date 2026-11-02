package org.etlforge.integration.data.entity.operation.flow;

public final class RouteRemovedChange extends FlowChange {
   private final Route route;

   public RouteRemovedChange(Route var1) {
      super(0);
      this.route = var1;
   }

   @Override
   public void dispatchTo(FlowChangeProcessor var1) {
      var1.process(this);
   }

   public Route getRemovedRoute() {
      return this.route;
   }

   @Override
   public String toString() {
      return this.route.toString();
   }
}
