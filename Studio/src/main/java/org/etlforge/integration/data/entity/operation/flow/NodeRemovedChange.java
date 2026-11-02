package org.etlforge.integration.data.entity.operation.flow;

public class NodeRemovedChange extends FlowChange {
   private final OperationActivityWrapper removed;

   public NodeRemovedChange(OperationActivityWrapper var1) {
      super(1);
      this.removed = var1;
   }

   @Override
   public void dispatchTo(FlowChangeProcessor var1) {
      var1.process(this);
   }

   public OperationActivityWrapper getRemovedNode() {
      return this.removed;
   }

   @Override
   public String toString() {
      return this.removed.toString();
   }
}
