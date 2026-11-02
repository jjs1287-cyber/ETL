package org.etlforge.integration.data.entity.operation.flow;

public final class NewOperationPipelineChange extends FlowChange {
   private final OperationWrapper operation;

   public NewOperationPipelineChange(OperationWrapper var1) {
      super(-100);
      this.operation = var1;
   }

   @Override
   public void dispatchTo(FlowChangeProcessor var1) {
      var1.process(this);
   }

   public OperationWrapper getOperationWrapper() {
      return this.operation;
   }
}
