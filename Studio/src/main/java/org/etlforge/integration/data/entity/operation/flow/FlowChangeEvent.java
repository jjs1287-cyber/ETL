package org.etlforge.integration.data.entity.operation.flow;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;
import java.util.EventObject;
import java.util.List;

public final class FlowChangeEvent extends EventObject {
   private static final Ordering<FlowChange> CHANGE_ORDER = new Ordering<FlowChange>() {
      public int compare(FlowChange var1, FlowChange var2) {
         return var1.getOrder() - var2.getOrder();
      }
   };
   private final ImmutableList<? extends FlowChange> changes;

   public FlowChangeEvent(OperationFlow var1, FlowChange var2) {
      super(var1);
      this.changes = ImmutableList.of(var2);
   }

   public FlowChangeEvent(OperationFlow var1, List<? extends FlowChange> var2) {
      super(var1);
      this.changes = CHANGE_ORDER.immutableSortedCopy(var2);
   }

   public OperationFlow getSource() {
      return (OperationFlow)super.getSource();
   }

   public void dispatchTo(FlowChangeProcessor var1) {
      for (FlowChange var3 : this.changes) {
         var3.dispatchTo(var1);
      }
   }
}
