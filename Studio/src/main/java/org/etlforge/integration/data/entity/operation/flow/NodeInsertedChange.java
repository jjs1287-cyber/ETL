package org.etlforge.integration.data.entity.operation.flow;

public final class NodeInsertedChange extends FlowChange {
   private final OperationActivityWrapper inserted;
   private final OperationActivityWrapper previous;

   public NodeInsertedChange(OperationActivityWrapper var1, OperationActivityWrapper var2) {
      super(2);
      this.inserted = var1;
      this.previous = var2;
   }

   @Override
   public void dispatchTo(FlowChangeProcessor var1) {
      var1.process(this);
   }

   public OperationActivityWrapper getInsertedNode() {
      return this.inserted;
   }

   public OperationActivityWrapper getPreviousNode() {
      return this.previous;
   }

   @Override
   public String toString() {
      return this.inserted.toString();
   }
}
