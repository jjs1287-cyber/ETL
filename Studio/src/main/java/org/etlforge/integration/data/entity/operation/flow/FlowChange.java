package org.etlforge.integration.data.entity.operation.flow;

public abstract class FlowChange {
   private final int order;

   FlowChange(int var1) {
      this.order = var1;
   }

   public abstract void dispatchTo(FlowChangeProcessor var1);

   int getOrder() {
      return this.order;
   }
}
