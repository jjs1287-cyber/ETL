package org.etlforge.integration.data.entity.operation.flow;

public final class FlowRebuiltChange extends FlowChange {
   public FlowRebuiltChange() {
      super(Integer.MIN_VALUE);
   }

   @Override
   public void dispatchTo(FlowChangeProcessor var1) {
      var1.process(this);
   }
}
